package fpt.capstone.service;

import fpt.capstone.entities.*;
import fpt.capstone.form_data.ConfigGradeDetailIdListForm;
import fpt.capstone.form_data.ConfigScoreDetailIdListForm;
import fpt.capstone.form_data.GradeBookForm;
import fpt.capstone.form_data.StudentRateForm;
import fpt.capstone.mapper.GradebookSubjectsDetailsCustomMapper;
import fpt.capstone.payroll.ValidationException;
import fpt.capstone.repository.*;
import fpt.capstone.security.services.UserDetailsImpl;
import fpt.capstone.utility.DataUtil;
import fpt.capstone.vo.GradebookScoreDetailsVO;
import fpt.capstone.vo.GradebookSubjectsDetailsVO;
import fpt.capstone.vo.academicAbilityVo;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.Collator;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class GradebookServiceImpl implements GradebookService {
    @Autowired
    private SubjectsRepository subjectsRepository;
    @Autowired
    private GradebookRepository gradebookRepository;
    @Autowired
    private TeachingAssignmentRepository teachingAssignmentRepository;
    @Autowired
    private GradebookSubjectsDetailsRepository gradebookSubjectsDetailsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GradebookScoreDetailsRepository gradebookScoreDetailsRepository;
    @Autowired
    private SubjectClassRepository subjectClassRepository;
    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Autowired
    private AcademicAbilityRepository academicAbilityRepository;
    @Autowired
    private SchoolYearRespository schoolYearRespository;

    @Override
    public ServiceResult<Map<String, Object>> findScoreOfClassInSemester(String classCode, String year, Integer semester) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        Map<String, Object> output = new HashMap<>();
        List<Map<String, Object>> scoreDetail = gradebookSubjectsDetailsRepository.findScoreOfClassInSemester(classCode, year, semester);
        if (scoreDetail.size() == 0) {
            serviceResult.setMessage("Dữ liệu rỗng");
            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
        } else {
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
        }
        output.put("scoreDetail", scoreDetail);
        serviceResult.setData(output);
        return serviceResult;
    }


    @Override
    public ServiceResult<Map<String, Object>> findScoreOfClassInSemesterMonXepLoai(String classCode, String year, Integer semester) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        Map<String, Object> output = new HashMap<>();
        List<Map<String, Object>> scoreDetail = gradebookSubjectsDetailsRepository.findScoreOfClassInSemesterMonXepLoai(classCode, year, semester);
        if (scoreDetail.size() == 0) {
            serviceResult.setMessage("Dữ liệu rỗng");
            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
        } else {
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
        }
        output.put("scoreDetail", scoreDetail);
        serviceResult.setData(output);
        return serviceResult;
    }

    @Override
    public ServiceResult<Map<String, Object>> getStudentRate(String classCode, String year, Integer semester) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        Map<String, Object> output = new HashMap<>();
        List<Map<String, Object>> studenRateList = gradebookRepository.getStudentRateCodeByClassYearSemester(classCode, year, semester);

        serviceResult.setMessage("success");
        serviceResult.setStatus(HttpStatus.OK);

        output.put("studentRate", studenRateList);
        serviceResult.setData(output);
        return serviceResult;
    }

    @Override
    public ServiceResult<Boolean> updateStudentRate(StudentRateForm form) {

        List<Map<String, Object>> gradeCodeList = gradebookRepository.getCodeByClassYearSemester(form.getClassCode(), form.getYear(), form.getSemester());
        if (gradeCodeList.size() == 0) {
            return new ServiceResult<>(HttpStatus.OK, "Lỗi không tìm thấy sổ điểm", false);
        }
        String gradeBookCode = gradeCodeList.get(0).get("code") + "";

        List<academicAbilityVo> studentRateList = form.getRateList();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        String createdName = userDetails.getUsername();
        try {


            List<AcademicAbility> AcademicAbilities = new ArrayList<>();
            for (academicAbilityVo i : studentRateList) {
                if (gradebookRepository.getAcademicAbility(gradeBookCode, i.getStudentCode()).size() == 0) {
                    String acCode = "AA_" + new Timestamp(System.currentTimeMillis());
                    AcademicAbility ac = new AcademicAbility(acCode, i.getStudentCode(), gradeBookCode, java.time.LocalDate.now(), createdName, i.getAcademicAbility());

                    AcademicAbilities.add(ac);

                } else {
                    gradebookRepository.updateAcademicAbility(i.getAcademicAbility(), i.getStudentCode(), gradeBookCode);
                }
            }
            academicAbilityRepository.saveAll(AcademicAbilities);
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Cập nhật lỗi", false);
        }

        return new ServiceResult<>(HttpStatus.OK, "Cập nhật thành công", false);
    }

    @Override
    public ServiceResult<Map<String, Object>> onSearch(GradeBookForm gradeBookForm) {
        Subjects subject = subjectsRepository.findByCode(gradeBookForm.getSubjectCode());
        if (gradeBookForm.getSemester().equals("0")) {
            ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
            try {
                List<Map<String, Object>> AvgScore;
                Map<String, Object> output = new HashMap<>();
                if (subject.getSubType() == 1) {
                    AvgScore = gradebookRepository.getForSemester0Grade(gradeBookForm.getClassCode(), gradeBookForm.getSubjectCode(), gradeBookForm.getSchoolYear(), schoolYearRespository.getSemesterAmount(gradeBookForm.getSchoolYear()));
                } else {
                    AvgScore = gradebookRepository.getForSemester0Score(gradeBookForm.getClassCode(), gradeBookForm.getSubjectCode(), gradeBookForm.getSchoolYear());
                }
                AvgScore.sort((o1, o2) -> Collator.getInstance().compare(o1.get("studentName").toString().substring(o1.get("studentName").toString().lastIndexOf(' ') + 1), o2.get("studentName").toString().substring(o2.get("studentName").toString().lastIndexOf(' ') + 1)));
                output.put("AvgScore", AvgScore);
                serviceResult.setMessage("success");
                serviceResult.setStatus(HttpStatus.OK);
                serviceResult.setData(output);
            } catch (Exception e) {
                e.printStackTrace();
                serviceResult.setMessage("fail");
                serviceResult.setStatus(HttpStatus.BAD_REQUEST);
            }
            return serviceResult;
        }

        ServiceResult<Map<String, Object>> listRes = null;
        if (subject != null && subject.getSubType() == 0) {
            listRes = getStudentListWithEachScore(gradeBookForm);
        } else if (subject != null && subject.getSubType() == 1) {
            listRes = getStudentListWithClassification(gradeBookForm);
        }
        return listRes;
    }

    private ServiceResult<Map<String, Object>> getStudentListWithEachScore(GradeBookForm gradeBookForm) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        List<ConfigScoreDetail> csdList = new ArrayList<>();
        List<GradebookSubjectsDetailsVO> gsdList = new ArrayList<>();//danh sach diem 1 mon cua 1 hs
        try {
            List<GradebookScoreDetailsVO> scoreList = gradebookRepository
                    .getStudentListWithEachScore(gradeBookForm.getClassCode(), gradeBookForm.getSubjectCode(), gradeBookForm.getSchoolYear(), 0, 1, gradeBookForm.getSemester())
                    .stream()
                    .map(
                            o -> {
                                GradebookScoreDetailsVO score = new GradebookScoreDetailsVO();//chi tiet ve 1 dau diem cua 1 hs

                                ConfigScoreDetail scd = new ConfigScoreDetail();// cau hinh diem
                                scd.setId((Integer) o[3]);
                                scd.setName(DataUtil.safeToString(o[4]));
                                scd.setCode(DataUtil.safeToString(o[5]));
                                scd.setCoefficient((Integer) o[6]);
                                scd.setQuantity((Integer) o[7]);
                                scd.setMinimumScore((Integer) o[8]);
                                scd.setParentCode(DataUtil.safeToString(o[9]));

                                csdList.add(scd);
                                GradebookSubjectsDetailsVO gsd = new GradebookSubjectsDetailsVO();
                                gsd.setAvgScore(DataUtil.safeToString(o[10], "").equals("") ? null : o[10].toString());
                                gsd.setId((Integer) o[11]);
                                gsd.setCode(DataUtil.safeToString(o[12]));
                                gsd.setParentCode(DataUtil.safeToString(o[13]));
                                gsd.setEvaluate(DataUtil.safeToString(o[14]));
                                gsd.setStatus((Integer) o[16]);
                                gsd.setStudentId((Integer) o[0]);
                                gsd.setStudentCode(DataUtil.safeToString(o[2]));
                                gsd.setStudentName(DataUtil.safeToString(o[1]));
                                gsd.setSubjectCode(gradeBookForm.getSubjectCode());
                                gsd.setClassCode(gradeBookForm.getClassCode());
                                gsdList.add(gsd);

                                score.setIdStudent((Integer) o[0]);
                                score.setId(DataUtil.safeToInt(o[17]));
                                score.setCode(DataUtil.safeToString(o[18]));
                                score.setParentCode(DataUtil.safeToString(o[19]));
                                score.setTimes(DataUtil.safeToInt(o[20]));
                                score.setValue(DataUtil.safeToString(o[21]));
                                score.setCoeficient((Integer) (o[22]));
                                score.setScoreCode(scd.getCode());
                                score.setScoreName(scd.getName());
                                score.setQuantity(scd.getQuantity());
                                score.setMinimumScore(scd.getMinimumScore());
                                score.setSubjectCode(gradeBookForm.getSubjectCode());
                                score.setType(0);
                                return score;
                            }
                    )
                    .collect(Collectors.toList());
            List<ConfigScoreDetail> confScoreDetailsList = csdList.stream().distinct().collect(Collectors.toList());
            Map<Integer, List<GradebookSubjectsDetailsVO>> mapGSD = gsdList
                    .stream()
                    .collect(Collectors.groupingBy(GradebookSubjectsDetailsVO::getStudentId));

            Map<Integer, List<GradebookScoreDetailsVO>> mapScore = scoreList
                    .stream()
                    .collect(Collectors.groupingBy(GradebookScoreDetailsVO::getIdStudent));

            Set<Integer> ids = mapGSD.keySet();
            List<GradebookSubjectsDetailsVO> resultList = new ArrayList<>();
            //For each student
            for (Integer id : ids) {
                List<GradebookSubjectsDetailsVO> gradebookSubjectsDetailsDTOS = mapGSD.get(id).stream().distinct().collect(Collectors.toList());
                GradebookSubjectsDetailsVO result = new GradebookSubjectsDetailsVO();
                if (!gradebookSubjectsDetailsDTOS.isEmpty()) {
                    Optional<GradebookSubjectsDetailsVO> gsdOpt = Optional.ofNullable(gradebookSubjectsDetailsDTOS
                            .stream()
                            .distinct()
                            .filter(s -> s.getId() != null)
                            .sorted(Comparator.comparingLong(GradebookSubjectsDetailsVO::getId))
                            .findFirst()
                            .orElse(gradebookSubjectsDetailsDTOS.get(0)));
                    if (gsdOpt.isPresent()) {
                        result = gsdOpt.get();
                    }
                }
                List<GradebookScoreDetailsVO> scoreListOfStudent = mapScore.get(id).stream().distinct().collect(Collectors.toList());
                List<GradebookScoreDetailsVO> listScore = new ArrayList<>();

                for (ConfigScoreDetail x : confScoreDetailsList) {
                    for (int i = 0; i < x.getQuantity(); i++) {
                        int temp = i;

                        List<GradebookScoreDetailsVO> scores = scoreListOfStudent
                                .stream()
                                .filter(
                                        o ->
                                                o.getId() != null &&
                                                        o.getTimes() != null &&
                                                        o.getTimes().equals(temp + 1) &&
                                                        o.getScoreCode().equals(x.getCode())
                                )
                                .collect(Collectors.toList());
                        GradebookScoreDetailsVO item = new GradebookScoreDetailsVO();
                        if (!scores.isEmpty()) {
                            listScore.add(scores.get(0));
                            ;
                        } else {
                            item.setScoreName(x.getName());
                            item.setScoreCode(x.getCode());
                            item.setTimes(i + 1);
                            item.setType(0);
                            item.setCoeficient(x.getCoefficient());
                            item.setQuantity(x.getQuantity());
                            item.setMinimumScore(x.getMinimumScore());
                            listScore.add(item);
                        }
                    }
                }
                result.setListScore(listScore);
                resultList.add(result);
            }
            resultList.sort((o1, o2) -> Collator.getInstance().compare(o1.getStudentName().substring(o1.getStudentName().lastIndexOf(' ') + 1), o2.getStudentName().substring(o2.getStudentName().lastIndexOf(' ') + 1)));
            Map<String, Object> output = new HashMap<>();
            output.put("ScoreList", resultList);
            output.put("ScoreConfig", confScoreDetailsList);
            SchoolYear sy = schoolYearRespository.getSchoolYearOptional(gradeBookForm.getSchoolYear(), gradeBookForm.getSemester()).get();
            if (gradeBookForm.getSemester().equals("1")) {
                if (teachingAssignmentRepository.isTeachingforSemester1(gradeBookForm.getSubjectCode(), gradeBookForm.getClassCode(), username) && LocalDateTime.now().isBefore(sy.getToDate()) && LocalDateTime.now().isAfter(sy.getFromDate())) {
                    output.put("canUpdate", 1);
                } else {
                    output.put("canUpdate", 0);
                }
            } else {
                if (teachingAssignmentRepository.isTeachingforSemester2(gradeBookForm.getSubjectCode(), gradeBookForm.getClassCode(), username) && LocalDateTime.now().isBefore(sy.getToDate()) && LocalDateTime.now().isAfter(sy.getFromDate())) {
                    output.put("canUpdate", 1);
                } else {
                    output.put("canUpdate", 0);
                }
            }
            if (confScoreDetailsList.size() == 0) {
                serviceResult.setMessage("Chưa cấu hình điểm cho lớp học");
                serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                serviceResult.setData(output);
                return serviceResult;
            }
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(output);
        } catch (Exception e) {
            e.printStackTrace();
            serviceResult.setMessage("fail");
            serviceResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return serviceResult;
    }

    private ServiceResult<Map<String, Object>> getStudentListWithClassification(GradeBookForm gradeBookForm) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        List<ConfigGradeDetail> cgdList = new ArrayList<>();
        List<GradebookSubjectsDetailsVO> gsdList = new ArrayList<>();
        try {
            List<GradebookScoreDetailsVO> scoreList = gradebookRepository
                    .getStudentListWithEachClassification(gradeBookForm.getClassCode(), gradeBookForm.getSubjectCode(), gradeBookForm.getSchoolYear(), 0, 1, gradeBookForm.getSemester())
                    .stream()
                    .map(
                            o -> {
                                GradebookScoreDetailsVO score = new GradebookScoreDetailsVO();
                                score.setIdStudent((Integer) o[0]);

                                ConfigGradeDetail cgd = new ConfigGradeDetail();
                                cgd.setId((Integer) o[3]);
                                cgd.setCode(DataUtil.safeToString(o[4]));
                                cgd.setName(DataUtil.safeToString(o[5]));
                                cgd.setTypeChoose((Integer) o[6]);
                                cgd.setSelectedValue(DataUtil.safeToString(o[7]));
                                cgdList.add(cgd);

                                GradebookSubjectsDetailsVO gsd = new GradebookSubjectsDetailsVO();
                                gsd.setId((Integer) o[8]);
                                gsd.setCode(DataUtil.safeToString(o[9]));
                                gsd.setParentCode(DataUtil.safeToString(o[10]));
                                gsd.setEvaluate(DataUtil.safeToString(o[11]));
                                gsd.setStatus((Integer) o[13]);
                                gsd.setStudentId(score.getIdStudent());
                                gsd.setStudentCode(DataUtil.safeToString(o[2]));
                                gsd.setStudentName(DataUtil.safeToString(o[1]));
                                gsd.setSubjectCode(gradeBookForm.getSubjectCode());
                                gsd.setClassCode(gradeBookForm.getClassCode());
                                gsdList.add(gsd);

                                score.setId((Integer) o[14]);
                                score.setCode(DataUtil.safeToString(o[15]));
                                score.setParentCode(DataUtil.safeToString(o[16]));
                                score.setTimes((Integer) o[17]);
                                score.setValue(DataUtil.safeToString(o[18]));
                                score.setCoeficient((Integer) o[19]);
                                score.setScoreCode(cgd.getCode());
                                score.setScoreName(cgd.getName());
                                score.setSubjectCode(gradeBookForm.getSubjectCode());
                                score.setType(1);
                                score.setTypeChoose(cgd.getTypeChoose());
                                if (cgd.getTypeChoose().equals(1)) {
                                    String selectedValue = cgd.getSelectedValue();
                                    List<String> selectList = Arrays
                                            .stream(selectedValue.split(","))
                                            .map(str -> str.trim())
                                            .collect(Collectors.toList());
                                    score.setSelectedValue(selectList);
                                }
                                return score;
                            }
                    )
                    .collect(Collectors.toList());

            Map<Integer, List<GradebookSubjectsDetailsVO>> mapGSD = gsdList
                    .stream()
                    .collect(Collectors.groupingBy(GradebookSubjectsDetailsVO::getStudentId));

            Map<Integer, List<GradebookScoreDetailsVO>> mapScore = scoreList
                    .stream()
                    .collect(Collectors.groupingBy(GradebookScoreDetailsVO::getIdStudent));

            Set<Integer> ids = mapGSD.keySet();
            List<GradebookSubjectsDetailsVO> resultList = new ArrayList<>();

            for (Integer id : ids) {
                GradebookSubjectsDetailsVO result = mapGSD.get(id).isEmpty() ? new GradebookSubjectsDetailsVO() : mapGSD.get(id).get(0);
                List<GradebookScoreDetailsVO> scoreListOfStudent = mapScore.get(id).stream().distinct().collect(Collectors.toList());
                result.setListScore(scoreListOfStudent);
                resultList.add(result);
            }
            resultList.sort((o1, o2) -> Collator.getInstance().compare(o1.getStudentName().substring(o1.getStudentName().lastIndexOf(' ') + 1), o2.getStudentName().substring(o2.getStudentName().lastIndexOf(' ') + 1)));
            Map<String, Object> output = new HashMap<>();
            List<ConfigGradeDetail> configGradeDetailLíst = cgdList.stream().distinct().collect(Collectors.toList());
            output.put("ScoreConfig", configGradeDetailLíst);
            output.put("ScoreList", resultList);
            SchoolYear sy = schoolYearRespository.getSchoolYearOptional(gradeBookForm.getSchoolYear(), gradeBookForm.getSemester()).get();
            if (gradeBookForm.getSemester().equals("1")) {
                if (teachingAssignmentRepository.isTeachingforSemester1(gradeBookForm.getSubjectCode(), gradeBookForm.getClassCode(), username) && LocalDateTime.now().isBefore(sy.getToDate()) && LocalDateTime.now().isAfter(sy.getFromDate())) {
                    output.put("canUpdate", 1);
                } else {
                    output.put("canUpdate", 0);
                }
            } else {
                if (teachingAssignmentRepository.isTeachingforSemester2(gradeBookForm.getSubjectCode(), gradeBookForm.getClassCode(), username) && LocalDateTime.now().isBefore(sy.getToDate()) && LocalDateTime.now().isAfter(sy.getFromDate())) {
                    output.put("canUpdate", 1);
                } else {
                    output.put("canUpdate", 0);
                }
            }
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(output);
        } catch (Exception e) {
            e.printStackTrace();
            serviceResult.setMessage("fail");
            serviceResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<Map<String, Object>> getListClassroom(GradeBookForm gradeBookForm) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        try {
            List<Map<String, Object>> ClassRoom = new ArrayList<>();
            if (gradeBookForm.getSemester().equals("1")) {
                ClassRoom = teachingAssignmentRepository.getClassRoomByTeacherCodeAndYearsAndSemester1(gradeBookForm.getTeacherCode(), gradeBookForm.getSchoolYear());
            } else {
                ClassRoom = teachingAssignmentRepository.getClassRoomByTeacherCodeAndYearsAndSemester2(gradeBookForm.getTeacherCode(), gradeBookForm.getSchoolYear());
            }
            Map<String, Object> output = new HashMap<>();
            output.put("ClassRoom", ClassRoom);
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(output);
        } catch (Exception e) {
            e.printStackTrace();
            serviceResult.setMessage("fail");
            serviceResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<Map<String, Object>> getListSubject(GradeBookForm gradeBookForm) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        try {
            List<Map<String, Object>> Subjects = new ArrayList<>();
            Subjects = teachingAssignmentRepository.getSubjectByTeacherCodeAndYearsAndClassCode(gradeBookForm.getTeacherCode(), gradeBookForm.getSchoolYear(), gradeBookForm.getClassCode());
            Map<String, Object> output = new HashMap<>();
            output.put("Subjects", Subjects);
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(output);
        } catch (Exception e) {
            e.printStackTrace();
            serviceResult.setMessage("fail");
            serviceResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<GradebookSubjectsDetails> evaluate(GradebookSubjectsDetailsVO vo, String login) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        try {
            User user = userRepository.findByLogin(login);
            Optional<GradebookSubjectsDetails> gsdOpt = gradebookSubjectsDetailsRepository.findById(vo.getId());
            if (gsdOpt.isPresent()) {
                GradebookSubjectsDetails gsd = gsdOpt.get();
                if (user != null) {
                    gsd.setUpdater(user.getLogin());
                }
                gsd.setEvaluate(vo.getEvaluate());
                Integer evaluateStatus = StringUtils.isNotEmpty(vo.getEvaluate()) ? 1 : 0;
                gsd.setEvaluateStatus(evaluateStatus);
                gsd.setUpdater(username);
                gradebookSubjectsDetailsRepository.save(gsd);
                return new ServiceResult<>(HttpStatus.OK, "Đánh giá thành công", null);
            } else {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "không tìm thấy sổ diểm của học sinh", null);
            }
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.INTERNAL_SERVER_ERROR, "Đánh giá thất bại", null);
        }
    }

    @Override
    public ServiceResult<Boolean> save(GradeBookForm gradeBookForm, String login) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        // save gradebook
        Gradebook gradebook = gradebookRepository.findBySchoolYearAndSemesterAndClassCode(
                gradeBookForm.getSchoolYear(),
                gradeBookForm.getSemester(),
                gradeBookForm.getClassCode()
        );
        if (null == gradebook) {
            gradebook = new Gradebook();
            gradebook.setSchoolYear(gradeBookForm.getSchoolYear());
            gradebook.setSemester(gradeBookForm.getSemester());
            gradebook.setClassCode(gradeBookForm.getClassCode());
        }
        try {
            String gradebookCode = gradebook.getId() == null ? UUID.randomUUID().toString().replace("-", "") : gradebook.getCode();
            gradebook.setCode(gradebookCode);
            gradebook = gradebookRepository.save(gradebook);
            Gradebook checkDuplicate = new Gradebook();
            gradeBookForm
                    .getSubjectsDetails()
                    .forEach(
                            subjectsDetails -> {
                                // save gradebookSubjectDetails
                                GradebookSubjectsDetails gradebookSubjectsDetails = GradebookSubjectsDetailsCustomMapper.toEntity(subjectsDetails);
                                String gradebookSubjectCode;
                                if (gradebookSubjectsDetails.getId() == null) {
                                    Optional<GradebookSubjectsDetails> gggd = gradebookSubjectsDetailsRepository.findFirstByStudentCodeAndSubjectCodeAndParentCode(
                                            subjectsDetails.getStudentCode(),
                                            subjectsDetails.getSubjectCode(),
                                            gradebookCode
                                    );
                                    // Check bug duplicate GradebookSubjectDetails
                                    if (gggd.isPresent()) {
                                        checkDuplicate.setId(1);
                                    }
                                    gradebookSubjectCode = UUID.randomUUID().toString().replace("-", "");
                                    gradebookSubjectsDetails.setEvaluateStatus(0);
                                } else {
                                    gradebookSubjectCode = gradebookSubjectsDetails.getCode();
                                }
                                gradebookSubjectsDetails.setCode(gradebookSubjectCode);
                                gradebookSubjectsDetails.setUpdater(username);
                                Integer status = StringUtils.isBlank(gradebookSubjectsDetails.getEvaluate()) ? 0 : 1;
                                gradebookSubjectsDetails.setEvaluateStatus(status);
                                gradebookSubjectsDetails.setParentCode(gradebookCode);
                                gradebookSubjectsDetails.setUpdater(username);
                                gradebookSubjectsDetailsRepository.save(gradebookSubjectsDetails);

                                // save gradebookScoreDetails
                                List<GradebookScoreDetails> gradebookScoreDetails = subjectsDetails
                                        .getListScore()
                                        .stream()
                                        .filter(scoreDetails -> scoreDetails.getValue() != null)
                                        .map(
                                                scoreDetails -> {
                                                    if (null != scoreDetails.getId()) {
                                                        Optional<GradebookScoreDetails> gsd = gradebookScoreDetailsRepository.findById(scoreDetails.getId());
                                                        GradebookScoreDetails gsdExist = gsd.get();
                                                        gsdExist.setValue(scoreDetails.getValue());
                                                        return gsdExist;
                                                    } else {
                                                        GradebookScoreDetails gsdNew = new GradebookScoreDetails(UUID.randomUUID().toString().replace("-", ""), gradebookSubjectCode,
                                                                scoreDetails.getScoreCode(), scoreDetails.getTimes(), scoreDetails.getValue(), scoreDetails.getType(), scoreDetails.getCoeficient());
                                                        return gsdNew;
                                                    }
                                                }
                                        )
                                        .collect(Collectors.toList());

                                gradebookScoreDetailsRepository.saveAll(gradebookScoreDetails);

                            }
                    );
            SubjectClass subjectClass = subjectClassRepository.findByClassCodeAndSubjectCode(
                    gradeBookForm.getClassCode(),
                    gradeBookForm.getSubjectCode()
            );
            if (subjectClass != null) {
                int countSemesterSubject = 0;
                SubjectClass sc = subjectClass;
                String lastSemester = "0";
                if (sc.getFlgSemester1().equals(1)) {
                    lastSemester = "1";
                    countSemesterSubject++;
                }
                if (sc.getFlgSemester2().equals(1)) {
                    lastSemester = "2";
                    countSemesterSubject++;
                }
                // doi voi mon tinh diem (type=0 ) can cap nhat lai avg_score cho semester=0(diem tb ca nam)
                if (
//                        gradeBookForm.getSemester().equals(lastSemester) &&
                        gradeBookForm.getSubjectsDetails().get(0).getListScore().get(0).getType() == 0
                ) {
                    // save gradebook with semester = 0 when last semester of this subject is saved
                    Gradebook wholeYearGradebookOpt = gradebookRepository.findBySchoolYearAndSemesterAndClassCode(
                            gradeBookForm.getSchoolYear(),
                            "0",
                            gradeBookForm.getClassCode()
                    );

                    int finalCountSemesterSubject = countSemesterSubject;

                    if (wholeYearGradebookOpt == null) {
                        String wholeYearGradebookCode = UUID.randomUUID().toString().replace("-", "");
                        Gradebook wholeYearGradebook = new Gradebook(wholeYearGradebookCode, gradeBookForm.getSchoolYear(), "0", gradeBookForm.getClassCode());
// dang nhap diem 1 mon cho hk 2 va
                        List<GradebookSubjectsDetails> gradebookSubjectsDetailsList = gradeBookForm
                                .getSubjectsDetails()
                                .stream()
                                .filter(subjectsDetails -> subjectsDetails.getAvgScore() != null)
                                .map(
                                        subjectsDetails -> {
                                            List<GradebookSubjectsDetails> previousGradebookSubjectsDetails = gradebookSubjectsDetailsRepository.getByStudentCodeAndSubjectCode(
                                                    subjectsDetails.getStudentCode(),
                                                    subjectsDetails.getSubjectCode(),
                                                    gradeBookForm.getSchoolYear()
                                            );

                                            List<GradebookSubjectsDetails> previousFilter = previousGradebookSubjectsDetails
                                                    .stream()
                                                    .filter(s -> s.getAvgScore() != null)
                                                    .collect(Collectors.toList());
                                            if (previousFilter.size() < finalCountSemesterSubject) {
                                                return null;
                                            }

                                            double avgScore = previousGradebookSubjectsDetails
                                                    .stream()
                                                    .filter(s -> s.getAvgScore() != null)
                                                    .mapToDouble(GradebookSubjectsDetails::getAvgScore)
                                                    .average()
                                                    .orElse(Double.NaN);

                                            return new GradebookSubjectsDetails(
                                                    UUID.randomUUID().toString().replace("-", "")
                                                    , subjectsDetails.getStudentCode(),
                                                    wholeYearGradebookCode,
                                                    Double.isNaN(avgScore) ? null : DataUtil.round(avgScore, 2),
                                                    subjectsDetails.getSubjectCode(),
                                                    username);
                                        }
                                )
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
                        if (!gradebookSubjectsDetailsList.isEmpty()) {
                            gradebookRepository.save(wholeYearGradebook);
                            gradebookSubjectsDetailsRepository.saveAll(gradebookSubjectsDetailsList);
                        }
                    } else {
                        Gradebook wholeYearGradebook = wholeYearGradebookOpt;
                        List<GradebookSubjectsDetails> gradebookSubjectsDetailsList = gradeBookForm
                                .getSubjectsDetails()
                                .stream()
                                .map(
                                        subjectsDetails -> {
                                            double avgScore = Double.NaN;
                                            List<GradebookSubjectsDetails> previousGradebookSubjectsDetails = gradebookSubjectsDetailsRepository.getByStudentCodeAndSubjectCode(
                                                    subjectsDetails.getStudentCode(),
                                                    subjectsDetails.getSubjectCode(),
                                                    gradeBookForm.getSchoolYear()
                                            );
                                            List<GradebookSubjectsDetails> previousFilter = previousGradebookSubjectsDetails
                                                    .stream()
                                                    .filter(s -> !s.getParentCode().equals(wholeYearGradebook.getCode()) && s.getAvgScore() != null)
                                                    .collect(Collectors.toList());
                                            if (
                                                    (previousFilter.size() < finalCountSemesterSubject && subjectsDetails.getAvgScore() != null) ||
                                                            (subjectsDetails.getAvgScore() == null && previousFilter.size() + 1 < finalCountSemesterSubject)
                                            ) {
                                                return null;
                                            }
                                            if (subjectsDetails.getAvgScore() != null) {
                                                avgScore =
                                                        previousGradebookSubjectsDetails
                                                                .stream()
                                                                .filter(s -> !s.getParentCode().equals(wholeYearGradebook.getCode()) && s.getAvgScore() != null)
                                                                .mapToDouble(GradebookSubjectsDetails::getAvgScore)
                                                                .average()
                                                                .orElse(Double.NaN);
                                            }
                                            // check ton tai cua GradebookSubjectsDetails
                                            Optional<GradebookSubjectsDetails> wholeGradebookSubjectsDetailsOpt = previousGradebookSubjectsDetails
                                                    .stream()
                                                    .filter(s -> s.getParentCode().equals(wholeYearGradebook.getCode()))
                                                    .findFirst();
                                            if (wholeGradebookSubjectsDetailsOpt.isPresent()) {
                                                wholeGradebookSubjectsDetailsOpt
                                                        .get()
                                                        .setAvgScore(Double.isNaN(avgScore) ? null : DataUtil.round(avgScore, 2));
                                                wholeGradebookSubjectsDetailsOpt
                                                        .get().setUpdater(username);
                                                return wholeGradebookSubjectsDetailsOpt
                                                        .get();
                                            } else {
                                                if (Double.isNaN(avgScore)) {
                                                    return null;
                                                }
                                                return new GradebookSubjectsDetails(
                                                        UUID.randomUUID().toString().replace("-", ""),
                                                        subjectsDetails.getStudentCode(),
                                                        wholeYearGradebook.getCode(),
                                                        Double.isNaN(avgScore) ? null : DataUtil.round(avgScore, 2),
                                                        subjectsDetails.getSubjectCode()
                                                        , username);
                                            }
                                        }
                                )
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
                        gradebookSubjectsDetailsRepository.saveAll(gradebookSubjectsDetailsList);
                    }
                }

            }
            //    }
            if (checkDuplicate.getId() != null) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thêm thừa dữ liệu", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Cập nhật điểm thất bại", null);
        }
        return new ServiceResult<>(HttpStatus.OK, "Cập nhật điểm thành công", null);
    }

    @Override
    public ByteArrayInputStream exportExcelSampleScore(String year, String subjectCode, String classCode, Integer semester, ConfigScoreDetailIdListForm configScoreDetailIdListForm) {
        ClassRoom classRoom = classRoomRepository.getClassRoomByCode(classCode).get();
        Subjects subjects = subjectsRepository.findByCode(subjectCode);
        List<ConfigScoreDetail> csdList = new ArrayList<>();
        List<GradebookSubjectsDetailsVO> gsdList = new ArrayList<>();//danh sach diem 1 mon cua 1 hs

        List<GradebookScoreDetailsVO> scoreList = gradebookRepository
                .getStudentListWithEachScore(classCode, subjectCode, year, 0, 1, semester.toString())
                .stream()
                .map(
                        o -> {
                            GradebookScoreDetailsVO score = new GradebookScoreDetailsVO();//chi tiet ve 1 dau diem cua 1 hs

                            ConfigScoreDetail scd = new ConfigScoreDetail();// cau hinh diem
                            scd.setId((Integer) o[3]);
                            scd.setName(DataUtil.safeToString(o[4]));
                            scd.setQuantity((Integer) o[7]);

                            csdList.add(scd);
                            GradebookSubjectsDetailsVO gsd = new GradebookSubjectsDetailsVO();
                            gsd.setStudentCode(DataUtil.safeToString(o[2]));
                            gsd.setStudentName(DataUtil.safeToString(o[1]));
                            gsd.setStudentId((Integer) o[0]);
                            gsdList.add(gsd);

                            return score;
                        }
                )
                .collect(Collectors.toList());
        List<ConfigScoreDetail> confScoreDetailsList = csdList.stream().distinct().collect(Collectors.toList());
        List<ConfigScoreDetail> confScoreDetailsListFilter = new ArrayList<>();
        for (Integer id : configScoreDetailIdListForm.getConfScoreDetailsIdList()) {
            confScoreDetailsListFilter.addAll(confScoreDetailsList.stream().filter(csd -> csd.getId() == id).collect(Collectors.toList()));
            ;
        }
        Map<Integer, List<GradebookSubjectsDetailsVO>> mapGSD = gsdList
                .stream()
                .collect(Collectors.groupingBy(GradebookSubjectsDetailsVO::getStudentId));

        Set<Integer> ids = mapGSD.keySet();
        List<GradebookSubjectsDetailsVO> resultList = new ArrayList<>();
        //For each student
        for (Integer id : ids) {
            List<GradebookSubjectsDetailsVO> gradebookSubjectsDetailsDTOS = mapGSD.get(id).stream().distinct().collect(Collectors.toList());
            GradebookSubjectsDetailsVO result = new GradebookSubjectsDetailsVO();
            if (!gradebookSubjectsDetailsDTOS.isEmpty()) {
                Optional<GradebookSubjectsDetailsVO> gsdOpt = Optional.ofNullable(gradebookSubjectsDetailsDTOS
                        .stream()
                        .distinct()
                        .filter(s -> s.getId() != null)
                        .sorted(Comparator.comparingLong(GradebookSubjectsDetailsVO::getId))
                        .findFirst()
                        .orElse(gradebookSubjectsDetailsDTOS.get(0)));
                if (gsdOpt.isPresent()) {
                    result = gsdOpt.get();
                }
                resultList.add(result);
            }
        }
        resultList.sort((o1, o2) -> Collator.getInstance().compare(o1.getStudentName().substring(o1.getStudentName().lastIndexOf(' ') + 1), o2.getStudentName().substring(o2.getStudentName().lastIndexOf(' ') + 1)));
        int numberColums = 0;
        for (ConfigScoreDetail csd : confScoreDetailsListFilter) {
            numberColums += csd.getQuantity();
        }
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet GradeBookSheet = workbook.createSheet("GradeBookScore");
            CellStyle commonStyle = workbook.createCellStyle();
            commonStyle.setAlignment(HorizontalAlignment.CENTER);
            commonStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            for (int i = 0; i < resultList.size() + 7; i++) {
                GradeBookSheet.createRow(i);
                for (int j = 0; j < numberColums + 5; j++) {
                    GradeBookSheet.getRow(i).createCell(j);
                }
            }
            GradeBookSheet.createRow(0).createCell(0).setCellValue("SỔ ĐIỂM " + classRoom.getName() + " - Môn: " + subjects.getName());
            GradeBookSheet.getRow(0).getCell(0).setCellStyle(commonStyle);
            GradeBookSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, numberColums + 2));

            GradeBookSheet.createRow(1).createCell(0).setCellValue("Học kỳ " + semester + " - Năm học : " + year);
            GradeBookSheet.getRow(1).getCell(0).setCellStyle(commonStyle);
            GradeBookSheet.addMergedRegion(new CellRangeAddress(1, 1, 0, numberColums + 2));
            //row 2
            GradeBookSheet.createRow(2);
            //row 3
            GradeBookSheet.createRow(3);
            GradeBookSheet.createRow(4);

            GradeBookSheet.getRow(3).createCell(0).setCellValue("STT");
            GradeBookSheet.getRow(3).createCell(1).setCellValue("Mã học sinh (*)");
            GradeBookSheet.getRow(3).createCell(2).setCellValue("Tên học sinh (*)");
            GradeBookSheet.getRow(3).getCell(0).setCellStyle(commonStyle);
            GradeBookSheet.getRow(3).getCell(1).setCellStyle(commonStyle);
            GradeBookSheet.getRow(3).getCell(2).setCellStyle(commonStyle);
            for (int i = 3; i < numberColums + 3; i++) {
                GradeBookSheet.getRow(3).createCell(i);
            }
            for (int i = 0; i < numberColums + 3; i++) {
                GradeBookSheet.getRow(4).createCell(i);
            }
            GradeBookSheet.addMergedRegion(new CellRangeAddress(3, 4, 0, 0));
            GradeBookSheet.addMergedRegion(new CellRangeAddress(3, 4, 1, 1));
            GradeBookSheet.addMergedRegion(new CellRangeAddress(3, 4, 2, 2));
            int startColum = 3;
            int endColumn = 3;
            for (ConfigScoreDetail csd : confScoreDetailsListFilter) {
                int numberColumsForOneScore = csd.getQuantity();
                endColumn = startColum + numberColumsForOneScore - 1;
                if (numberColumsForOneScore == 1) {
                    GradeBookSheet.getRow(3).createCell(startColum);
                    GradeBookSheet.getRow(3).getCell(startColum).setCellValue(csd.getName());
                    GradeBookSheet.getRow(3).getCell(startColum).setCellStyle(commonStyle);

                    GradeBookSheet.getRow(4).createCell(startColum).setCellValue("Đ1");
                    GradeBookSheet.getRow(4).getCell(startColum).setCellStyle(commonStyle);
                } else {
                    GradeBookSheet.getRow(3).createCell(startColum).setCellValue(csd.getName());
                    GradeBookSheet.getRow(3).getCell(startColum).setCellStyle(commonStyle);
                    GradeBookSheet.addMergedRegion(new CellRangeAddress(3, 3, startColum, endColumn));
                    int a = 1;
                    for (int i = startColum; i <= endColumn; i++) {
                        GradeBookSheet.getRow(4).createCell(i).setCellValue("Đ" + a);
                        GradeBookSheet.getRow(4).getCell(i).setCellStyle(commonStyle);
                        a++;
                    }
                }
                startColum = endColumn + 1;
            }
            for (int i = 1; i < resultList.size() + 1; i++) {
                GradeBookSheet.createRow(i + 4);
                GradeBookSheet.getRow(i + 4).createCell(0).setCellValue(i);
                GradeBookSheet.getRow(i + 4).createCell(1).setCellValue(resultList.get(i - 1).getStudentCode());
                GradeBookSheet.getRow(i + 4).createCell(2).setCellValue(resultList.get(i - 1).getStudentName());
                for (int j = 3; j <= numberColums + 4; j++) {
                    GradeBookSheet.getRow(i + 4).createCell(j);
                }
                GradeBookSheet.getRow(i + 4).getCell(0).setCellStyle(commonStyle);
            }
            for (int j = 0; j < numberColums + 3; j++) {
                GradeBookSheet.autoSizeColumn(j);
            }
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setTopBorderColor(IndexedColors.BLACK.index);
            cellStyle.setRightBorderColor(IndexedColors.BLACK.index);
            cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);
            cellStyle.setLeftBorderColor(IndexedColors.BLACK.index);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            for (int i = 5; i < resultList.size() + 5; i++) {
                for (int j = 0; j < numberColums + 3; j++) {
                    GradeBookSheet.getRow(i).getCell(j).setCellStyle(cellStyle);
                }
            }
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            for (int i = 0; i < numberColums + 3; i++) {
                GradeBookSheet.getRow(3).getCell(i).setCellStyle(cellStyle);
                GradeBookSheet.getRow(4).getCell(i).setCellStyle(cellStyle);
            }
            String idForImport = "";
            for (Integer id : configScoreDetailIdListForm.getConfScoreDetailsIdList()) {
                idForImport += id + " ";
            }
            for (int i = 3; i <= GradeBookSheet.getRow(5).getPhysicalNumberOfCells(); i++) {
                GradeBookSheet.setColumnWidth(i, 15 * 256);
            }
//            CellStyle style = workbook.createCellStyle();
//            Font font = workbook.createFont();
//            font.setColor(XSSFColor.toXSSFColor(Color));
//            style.setFont(font);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setColor(HSSFColor.WHITE.index);
            style.setFont(font);
            GradeBookSheet.createRow(100).createCell(20).setCellValue(idForImport);
            GradeBookSheet.getRow(100).getCell(20).setCellStyle(style);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ValidationException("file", "Không thể xuất file");
        }
    }

    @Override
    public ServiceResult<Boolean> importExcelScore(MultipartFile file, String subjectCode, String classCode, String schoolYear, String semester, String login) throws IOException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        GradeBookForm gradeBookForm = new GradeBookForm();
        gradeBookForm.setSemester(semester);
        gradeBookForm.setSchoolYear(schoolYear);
        gradeBookForm.setClassCode(classCode);
        gradeBookForm.setSubjectCode(subjectCode);
        List<ConfigScoreDetail> csdList = new ArrayList<>();
        List<GradebookSubjectsDetailsVO> gsdList = new ArrayList<>();//danh sach diem 1 mon cua 1 hs
        try {
            List<GradebookScoreDetailsVO> scoreList = gradebookRepository
                    .getStudentListWithEachScore(classCode, subjectCode, schoolYear, 0, 1, semester)
                    .stream()
                    .map(
                            o -> {
                                GradebookScoreDetailsVO score = new GradebookScoreDetailsVO();//chi tiet ve 1 dau diem cua 1 hs

                                ConfigScoreDetail scd = new ConfigScoreDetail();// cau hinh diem
                                scd.setId((Integer) o[3]);
                                scd.setName(DataUtil.safeToString(o[4]));
                                scd.setCode(DataUtil.safeToString(o[5]));
                                scd.setCoefficient((Integer) o[6]);
                                scd.setQuantity((Integer) o[7]);
                                scd.setMinimumScore((Integer) o[8]);
                                scd.setParentCode(DataUtil.safeToString(o[9]));

                                csdList.add(scd);
                                GradebookSubjectsDetailsVO gsd = new GradebookSubjectsDetailsVO();
                                gsd.setAvgScore(DataUtil.safeToString(o[10], "").equals("") ? null : o[10].toString());
                                gsd.setId((Integer) o[11]);
                                gsd.setCode(DataUtil.safeToString(o[12]));
                                gsd.setParentCode(DataUtil.safeToString(o[13]));
                                gsd.setEvaluate(DataUtil.safeToString(o[14]));
                                gsd.setStatus((Integer) o[16]);
                                gsd.setStudentId((Integer) o[0]);
                                gsd.setStudentCode(DataUtil.safeToString(o[2]));
                                gsd.setStudentName(DataUtil.safeToString(o[1]));
                                gsd.setSubjectCode(subjectCode);
                                gsd.setClassCode(classCode);
                                gsdList.add(gsd);

                                score.setIdStudent((Integer) o[0]);
                                score.setId(DataUtil.safeToInt(o[17]));
                                score.setCode(DataUtil.safeToString(o[18]));
                                score.setParentCode(DataUtil.safeToString(o[19]));
                                score.setTimes(DataUtil.safeToInt(o[20]));
                                score.setValue(DataUtil.safeToString(o[21]));
                                score.setCoeficient(DataUtil.safeToInt(o[22]));
                                score.setScoreCode(scd.getCode());
                                score.setScoreName(scd.getName());
                                score.setQuantity(scd.getQuantity());
                                score.setMinimumScore(scd.getMinimumScore());
                                score.setSubjectCode(subjectCode);
                                score.setType(0);
                                return score;
                            }
                    )
                    .collect(Collectors.toList());
            List<ConfigScoreDetail> confScoreDetailsList = csdList.stream().distinct().collect(Collectors.toList());
            Map<Integer, List<GradebookSubjectsDetailsVO>> mapGSD = gsdList
                    .stream()
                    .collect(Collectors.groupingBy(GradebookSubjectsDetailsVO::getStudentId));

            Map<Integer, List<GradebookScoreDetailsVO>> mapScore = scoreList
                    .stream()
                    .collect(Collectors.groupingBy(GradebookScoreDetailsVO::getIdStudent));

            Set<Integer> ids = mapGSD.keySet();
            List<GradebookSubjectsDetailsVO> resultList = new ArrayList<>();
            //For each student
            for (Integer id : ids) {
                List<GradebookSubjectsDetailsVO> gradebookSubjectsDetailsDTOS = mapGSD.get(id).stream().distinct().collect(Collectors.toList());
                GradebookSubjectsDetailsVO result = new GradebookSubjectsDetailsVO();
                if (!gradebookSubjectsDetailsDTOS.isEmpty()) {
                    Optional<GradebookSubjectsDetailsVO> gsdOpt = Optional.ofNullable(gradebookSubjectsDetailsDTOS
                            .stream()
                            .distinct()
                            .filter(s -> s.getId() != null)
                            .sorted(Comparator.comparingLong(GradebookSubjectsDetailsVO::getId))
                            .findFirst()
                            .orElse(gradebookSubjectsDetailsDTOS.get(0)));
                    if (gsdOpt.isPresent()) {
                        result = gsdOpt.get();
                    }
                }
                List<GradebookScoreDetailsVO> scoreListOfStudent = mapScore.get(id).stream().distinct().collect(Collectors.toList());
                List<GradebookScoreDetailsVO> listScore = new ArrayList<>();

                for (ConfigScoreDetail x : confScoreDetailsList) {
                    for (int i = 0; i < x.getQuantity(); i++) {
                        int temp = i;

                        List<GradebookScoreDetailsVO> scores = scoreListOfStudent
                                .stream()
                                .filter(
                                        o ->
                                                o.getId() != null &&
                                                        o.getTimes() != null &&
                                                        o.getTimes().equals(temp + 1) &&
                                                        o.getScoreCode().equals(x.getCode())
                                )
                                .collect(Collectors.toList());
                        GradebookScoreDetailsVO item = new GradebookScoreDetailsVO();
                        if (!scores.isEmpty()) {
                            listScore.add(scores.get(0));
                            ;
                        } else {
                            item.setScoreName(x.getName());
                            item.setScoreCode(x.getCode());
                            item.setTimes(i + 1);
                            item.setType(0);
                            item.setCoeficient(x.getCoefficient());
                            item.setQuantity(x.getQuantity());
                            item.setMinimumScore(x.getMinimumScore());
                            listScore.add(item);
                        }
                    }
                }
                result.setListScore(listScore);
                resultList.add(result);
            }
            resultList.sort((o1, o2) -> Collator.getInstance().compare(o1.getStudentName().substring(o1.getStudentName().lastIndexOf(' ') + 1), o2.getStudentName().substring(o2.getStudentName().lastIndexOf(' ') + 1)));
            // lay data tu file import va update vao data resultList vua lay ra
            // tinh so cot
            // doc file
            if (file == null) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Vui lòng nhập file", null);
            }
            if (isExcelFormat(file)) {
                Workbook workbook = new XSSFWorkbook(file.getInputStream());
                Sheet sheet = workbook.getSheetAt(0);
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

                List<ConfigScoreDetail> confScoreDetailsListInExcel = new ArrayList<>();
                String idForImport = sheet.getRow(100).getCell(20).getStringCellValue().trim();
                List<String> confScoreDetailsIdList = Arrays.asList(idForImport.split("\\s"));
                for (String id : confScoreDetailsIdList) {
                    confScoreDetailsListInExcel.addAll(confScoreDetailsList.stream().filter(csd -> csd.getId() == Integer.parseInt(id.trim())).collect(Collectors.toList()));
                }
                Integer numberColumns = 3;
                for (ConfigScoreDetail csd : confScoreDetailsListInExcel) {
                    numberColumns += csd.getQuantity();
                }

                if (sheet.getRow(5).getPhysicalNumberOfCells() != numberColumns) {
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Vui lòng chọn, nhập đầy đủ cột điểm", null);
                }
                //check đầu điểm
                Integer index = 3;
                for (int i = 0; i < confScoreDetailsListInExcel.size(); i++) {
                    if (!confScoreDetailsListInExcel.get(i).getName().trim().equals(sheet.getRow(3).getCell(index).getStringCellValue().trim())) {
                        return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng 4, tên đầu điểm thứ " + (i + 1) + " đang sai ", null);
                    }
                    index += confScoreDetailsListInExcel.get(i).getQuantity();
                }
                //check cột diểm
                int startColum = 3;
                int endColumn = 3;
                for (ConfigScoreDetail csd : confScoreDetailsListInExcel) {
                    int numberColumsForOneScore = csd.getQuantity();
                    endColumn = startColum + numberColumsForOneScore - 1;
                    if (numberColumsForOneScore == 1) {
                        if (!sheet.getRow(4).getCell(startColum).getStringCellValue().trim().equals("Đ1")) {
                            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng 5, tên cột điểm cột " + startColum + " đang sai ", null);
                        }
                    } else {
                        int a = 1;
                        for (int i = startColum; i <= endColumn; i++) {
                            if (!sheet.getRow(4).getCell(i).getStringCellValue().trim().equals("Đ" + a)) {
                                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng 5, tên cột điểm cột " + i + " đang sai ", null);
                            }
                            a++;
                        }
                    }
                    startColum = endColumn + 1;
                }

                try {
                    for (int i = 5; i < sheet.getPhysicalNumberOfRows(); i++) {
                        int csdIndex = -1;
                        final Row row = sheet.getRow(i);
                        if (row.getCell(1).getCellTypeEnum() == CellType.BLANK) {
                            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + (i + 1) + " ,cột Mã học sinh không được để trống", null);
                        } else if (!resultList.get(i - 5).getStudentCode().equals(row.getCell(1).getStringCellValue().trim())) {
                            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + (i + 1) + " , Mã học sinh đang sai", null);

                        }
                        if (row.getCell(2).getCellTypeEnum() == CellType.BLANK) {
                            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + (i + 1) + " ,cột Tên học sinh không được để trống", null);
                        } else if (!resultList.get(i - 5).getStudentName().equals(row.getCell(2).getStringCellValue().trim())) {
                            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + (i + 1) + " ,Tên học sinh đang sai", null);
                        }
                        for (int j = 3; j < numberColumns; j++) {
                            final Cell cell = row.getCell(j);
                            Integer times = Integer.parseInt(sheet.getRow(4).getCell(j).getStringCellValue().trim().substring(1, 2));
                            if (times == 1) {
                                csdIndex++;
                            }
                            CellValue cellValue = evaluator.evaluate(cell);
                            if (cell.getCellTypeEnum() == CellType.STRING || cell.getCellTypeEnum() == CellType.BLANK) {// khong nhap diem thi nhay sang o tiepư
                                continue;
                            }
                            if (!(cell.getCellTypeEnum() == CellType.NUMERIC)) {// khong nhap diem thi nhay sang o tiepư
                                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + (i + 1) + ", điểm phải nhập số", null);
                            }
                            if (cell.getNumericCellValue() > 10 || cell.getNumericCellValue() < 0) {// khong nhap diem thi nhay sang o tiepư
                                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + (i + 1) + ", điểm phải bé hơn 10 và lớn hơn 0", null);
                            }
                            String studentCode = sheet.getRow(i).getCell(1).getStringCellValue().trim();
                            String csdCode = confScoreDetailsListInExcel.get(csdIndex).getCode();
                            Optional<GradebookScoreDetails> gsd = gradebookScoreDetailsRepository.findByScoreCodeAndTimes(csdCode, times, schoolYear, semester, classCode, studentCode, subjectCode);
                            if (gsd.isPresent()) {
                                for (GradebookScoreDetailsVO gsdVO : resultList.get(i - 5).getListScore()) {
                                    if (gsdVO.getCode().equals(gsd.get().getCode()) && gsdVO.getScoreCode().equals(gsd.get().getScoreCode()) && gsdVO.getTimes().equals(gsd.get().getTimes())) {
                                        gsdVO.setValue(cell.getNumericCellValue() + "");
                                    }
                                }
                            } else {
                                for (GradebookScoreDetailsVO gsdVO : resultList.get(i - 5).getListScore()) {
                                    if (gsdVO.getScoreCode().equals(confScoreDetailsListInExcel.get(csdIndex).getCode()) &&
                                            gsdVO.getTimes().equals(times)) {
                                        gsdVO.setValue(cell.getNumericCellValue() + "");
                                    }
                                }
                            }
                        }
                        resultList.get(i - 5).setUpdater(username);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Lưu điểm không thành công", null);
                }
            } else {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "File không đúng định dạng", null);
            }
            // tinh diem trung binh cho 1 hoc ky

            for (GradebookSubjectsDetailsVO gsd : resultList) {
                Double total = 0D;
                Integer coefficient = 0;
                Boolean check = true;
                for (GradebookScoreDetailsVO gScored : gsd.getListScore()) {
                    if (gScored.getValue() == null) {
                        check = false;
                    }
                }
                if (check) {

                    for (GradebookScoreDetailsVO gScored : gsd.getListScore()) {
                        total += Double.parseDouble(gScored.getValue()) * gScored.getCoeficient();
                        coefficient += gScored.getCoeficient();
                    }
                    Double avgScore = DataUtil.round(total / coefficient, 2);
                    gsd.setAvgScore(avgScore.toString());
                }
            }
            gradeBookForm.setSubjectsDetails(resultList);
            ServiceResult<Boolean> saveResultToDB = save(gradeBookForm, login);
            return saveResultToDB;
        } catch (Exception e) {
            e.printStackTrace();
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Lưu điểm thất bại, có lỗi xảy ra", null);
        }
    }

    @Override
    public ByteArrayInputStream exportExcelSampleGrade(String year, String subjectCode, String classCode, Integer semester, ConfigGradeDetailIdListForm configGradeDetailIdListForm) {
        ClassRoom classRoom = classRoomRepository.getClassRoomByCode(classCode).get();
        Subjects subjects = subjectsRepository.findByCode(subjectCode);
        List<ConfigGradeDetail> cgdList = new ArrayList<>();
        List<GradebookSubjectsDetailsVO> gsdList = new ArrayList<>();
        List<GradebookScoreDetailsVO> scoreList = gradebookRepository
                .getStudentListWithEachClassification(classCode, subjectCode, year, 0, 1, semester + "")
                .stream()
                .map(
                        o -> {
                            GradebookScoreDetailsVO score = new GradebookScoreDetailsVO();
                            score.setIdStudent((Integer) o[0]);

                            ConfigGradeDetail cgd = new ConfigGradeDetail();
                            cgd.setId((Integer) o[3]);
                            cgd.setCode(DataUtil.safeToString(o[4]));
                            cgd.setName(DataUtil.safeToString(o[5]));
                            cgd.setTypeChoose((Integer) o[6]);
                            cgd.setSelectedValue(DataUtil.safeToString(o[7]));
                            cgdList.add(cgd);

                            GradebookSubjectsDetailsVO gsd = new GradebookSubjectsDetailsVO();
                            gsd.setStudentId(score.getIdStudent());
                            gsd.setStudentCode(DataUtil.safeToString(o[2]));
                            gsd.setStudentName(DataUtil.safeToString(o[1]));
                            gsdList.add(gsd);
                            score.setTypeChoose(cgd.getTypeChoose());
                            if (cgd.getTypeChoose().equals(1)) {
                                String selectedValue = cgd.getSelectedValue();
                                List<String> selectList = Arrays
                                        .stream(selectedValue.split(","))
                                        .map(str -> str.trim())
                                        .collect(Collectors.toList());
                                score.setSelectedValue(selectList);
                            }
                            return score;
                        }
                )
                .collect(Collectors.toList());
        Map<Integer, List<GradebookSubjectsDetailsVO>> mapGSD = gsdList
                .stream()
                .collect(Collectors.groupingBy(GradebookSubjectsDetailsVO::getStudentId));

        Map<Integer, List<GradebookScoreDetailsVO>> mapScore = scoreList
                .stream()
                .collect(Collectors.groupingBy(GradebookScoreDetailsVO::getIdStudent));
        Set<Integer> ids = mapGSD.keySet();
        List<ConfigGradeDetail> configGradeDetailList = cgdList.stream().distinct().collect(Collectors.toList());
        List<ConfigGradeDetail> confGradeDetailsListFilter = new ArrayList<>();
        for (Integer id : configGradeDetailIdListForm.getConfGradeDetailsIdList()) {
            confGradeDetailsListFilter.addAll(configGradeDetailList.stream().filter(cgd -> cgd.getId() == id).collect(Collectors.toList()));
        }
        List<GradebookSubjectsDetailsVO> resultList = new ArrayList<>();
        for (Integer id : ids) {
            GradebookSubjectsDetailsVO result = mapGSD.get(id).isEmpty() ? new GradebookSubjectsDetailsVO() : mapGSD.get(id).get(0);
            List<GradebookScoreDetailsVO> scoreListOfStudent = mapScore.get(id).stream().distinct().collect(Collectors.toList());
            result.setListScore(scoreListOfStudent);
            resultList.add(result);
        }
        resultList.sort((o1, o2) -> Collator.getInstance().compare(o1.getStudentName().substring(o1.getStudentName().lastIndexOf(' ') + 1), o2.getStudentName().substring(o2.getStudentName().lastIndexOf(' ') + 1)));
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet GradeBookSheet = workbook.createSheet("GradeBookScore");

            CellStyle commonStyle = workbook.createCellStyle();
            commonStyle.setAlignment(HorizontalAlignment.CENTER);
            commonStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            GradeBookSheet.createRow(0).createCell(0).setCellValue("SỔ ĐIỂM " + classRoom.getName() + " - Môn: " + subjects.getName());
            GradeBookSheet.getRow(0).getCell(0).setCellStyle(commonStyle);
            GradeBookSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

            GradeBookSheet.createRow(1).createCell(0).setCellValue("Học kỳ " + semester + " - Năm học : " + year);
            GradeBookSheet.getRow(1).getCell(0).setCellStyle(commonStyle);
            GradeBookSheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));
            GradeBookSheet.createRow(2);
            GradeBookSheet.createRow(3);

            GradeBookSheet.getRow(3).createCell(0).setCellValue("STT");
            GradeBookSheet.getRow(3).createCell(1).setCellValue("Mã học sinh (*)");
            GradeBookSheet.getRow(3).createCell(2).setCellValue("Tên học sinh (*)");
//tao ten cac dau diem
            for (int i = 0; i < confGradeDetailsListFilter.size(); i++) {
                GradeBookSheet.getRow(3).createCell(i + 3).setCellValue(confGradeDetailsListFilter.get(i).getName());
            }
            //create row
            for (int i = 0; i < resultList.size(); i++) {
                GradeBookSheet.createRow(i + 4);
                for (int j = 0; j <= confGradeDetailsListFilter.size() + 2; j++) {
                    GradeBookSheet.getRow(i + 4).createCell(j);
                }
            }

            //   GradeBookSheet.getRow(3).createCell(confGradeDetailsListFilter.size() + 3).setCellValue("Nhận xét");

            for (int i = 0; i < resultList.size(); i++) {
                GradeBookSheet.createRow(i + 4);
                GradeBookSheet.getRow(i + 4).createCell(0).setCellValue(i + 1);
                GradeBookSheet.getRow(i + 4).getCell(0).setCellStyle(commonStyle);
                GradeBookSheet.getRow(i + 4).createCell(1).setCellValue(resultList.get(i).getStudentCode());
                GradeBookSheet.getRow(i + 4).createCell(2).setCellValue(resultList.get(i).getStudentName());
                //config o nhap diem cho 2 kieu :nhap diem hoac chon tu danh sach

                for (int j = 0; j < confGradeDetailsListFilter.size(); j++) {
                    if (confGradeDetailsListFilter.get(j).getTypeChoose() == 1) {

                        String selectedValue = confGradeDetailsListFilter.get(j).getSelectedValue();
                        List<String> selectList = Arrays
                                .stream(selectedValue.split(","))
                                .map(str -> str.trim())
                                .collect(Collectors.toList());
                        String[] selectedValues = new String[selectList.size()];
                        for (int x = 0; x < selectList.size(); x++) {
                            selectedValues[x] = selectList.get(x);
                        }
                        GradeBookSheet.getRow(i + 4).createCell(j + 3);
                        DataValidation dataValidation = null;
                        DataValidationConstraint constraint = null;
                        DataValidationHelper validationHelper = null;

                        validationHelper = new XSSFDataValidationHelper((XSSFSheet) GradeBookSheet);
                        CellRangeAddressList addressList = new CellRangeAddressList(i + 4, i + 4, j + 3, j + 3);
                        constraint = validationHelper.createExplicitListConstraint(selectedValues);
                        dataValidation = validationHelper.createValidation(constraint, addressList);
                        dataValidation.setSuppressDropDownArrow(true);
                        GradeBookSheet.addValidationData(dataValidation);
                    } else {
                        GradeBookSheet.getRow(i + 4).createCell(j + 3);
                    }
                }
                // GradeBookSheet.getRow(i + 4).createCell(confGradeDetailsListFilter.size() + 3);//cot nhan xet
            }

            for (int j = 0; j < confGradeDetailsListFilter.size() + 3; j++) {
                GradeBookSheet.autoSizeColumn(j);
            }

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setTopBorderColor(IndexedColors.BLACK.index);
            cellStyle.setRightBorderColor(IndexedColors.BLACK.index);
            cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);
            cellStyle.setLeftBorderColor(IndexedColors.BLACK.index);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);

            for (int i = 3; i < resultList.size() + 4; i++) {
                for (int j = 0; j < confGradeDetailsListFilter.size() + 3; j++) {
                    GradeBookSheet.getRow(i).getCell(j).setCellStyle(cellStyle);
                }
            }
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            GradeBookSheet.getRow(3).getCell(0).setCellStyle(cellStyle);
            GradeBookSheet.getRow(3).getCell(1).setCellStyle(cellStyle);
            GradeBookSheet.getRow(3).getCell(2).setCellStyle(cellStyle);
            GradeBookSheet.getRow(3).getCell(3).setCellStyle(cellStyle);
            for (int i = 0; i < resultList.size(); i++) {
                GradeBookSheet.getRow(i + 4).getCell(0).setCellStyle(cellStyle);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            String idForImport = "";
            for (Integer id : configGradeDetailIdListForm.getConfGradeDetailsIdList()) {
                idForImport += id + " ";
            }
            for (int i = 3; i <= GradeBookSheet.getRow(4).getPhysicalNumberOfCells(); i++) {
                GradeBookSheet.setColumnWidth(i, 15 * 256);
            }
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setColor(HSSFColor.WHITE.index);
            style.setFont(font);
            GradeBookSheet.createRow(100).createCell(20).setCellValue(idForImport);
            GradeBookSheet.getRow(100).getCell(20).setCellStyle(style);
            workbook.write(out);
            workbook.close();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ValidationException("file", "Không thể export file");
        }
    }

    @Override
    public ServiceResult<Boolean> importExcelGrade(MultipartFile file, String subjectCode, String classCode, String schoolYear, String semester, String login) throws IOException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        GradeBookForm gradeBookForm = new GradeBookForm();
        gradeBookForm.setSemester(semester);
        gradeBookForm.setSchoolYear(schoolYear);
        gradeBookForm.setClassCode(classCode);
        gradeBookForm.setSubjectCode(subjectCode);
        List<ConfigGradeDetail> cgdList = new ArrayList<>();
        List<GradebookSubjectsDetailsVO> gsdList = new ArrayList<>();
        try {
            List<GradebookScoreDetailsVO> scoreList = gradebookRepository
                    .getStudentListWithEachClassification(gradeBookForm.getClassCode(), gradeBookForm.getSubjectCode(), gradeBookForm.getSchoolYear(), 0, 1, gradeBookForm.getSemester())
                    .stream()
                    .map(
                            o -> {
                                GradebookScoreDetailsVO score = new GradebookScoreDetailsVO();
                                score.setIdStudent((Integer) o[0]);

                                ConfigGradeDetail cgd = new ConfigGradeDetail();
                                cgd.setId((Integer) o[3]);
                                cgd.setCode(DataUtil.safeToString(o[4]));
                                cgd.setName(DataUtil.safeToString(o[5]));
                                cgd.setTypeChoose((Integer) o[6]);
                                cgd.setSelectedValue(DataUtil.safeToString(o[7]));
                                cgdList.add(cgd);

                                GradebookSubjectsDetailsVO gsd = new GradebookSubjectsDetailsVO();
                                gsd.setId((Integer) o[8]);
                                gsd.setCode(DataUtil.safeToString(o[9]));
                                gsd.setParentCode(DataUtil.safeToString(o[10]));
                                gsd.setEvaluate(DataUtil.safeToString(o[11]));
                                gsd.setStatus((Integer) o[13]);
                                gsd.setStudentId(score.getIdStudent());
                                gsd.setStudentCode(DataUtil.safeToString(o[2]));
                                gsd.setStudentName(DataUtil.safeToString(o[1]));
                                gsd.setSubjectCode(gradeBookForm.getSubjectCode());
                                gsd.setClassCode(gradeBookForm.getClassCode());
                                gsdList.add(gsd);

                                score.setId((Integer) o[14]);
                                score.setCode(DataUtil.safeToString(o[15]));
                                score.setParentCode(DataUtil.safeToString(o[16]));
                                score.setTimes((Integer) o[17]);
                                score.setValue(DataUtil.safeToString(o[18]));
                                score.setCoeficient((Integer) o[19]);
                                score.setScoreCode(cgd.getCode());
                                score.setScoreName(cgd.getName());
                                score.setSubjectCode(gradeBookForm.getSubjectCode());
                                score.setType(1);
                                score.setTypeChoose(cgd.getTypeChoose());
                                if (cgd.getTypeChoose().equals(1)) {
                                    String selectedValue = cgd.getSelectedValue();
                                    List<String> selectList = Arrays
                                            .stream(selectedValue.split(","))
                                            .map(str -> str.trim())
                                            .collect(Collectors.toList());
                                    score.setSelectedValue(selectList);
                                }
                                return score;
                            }
                    )
                    .collect(Collectors.toList());
            Map<Integer, List<GradebookSubjectsDetailsVO>> mapGSD = gsdList
                    .stream()
                    .collect(Collectors.groupingBy(GradebookSubjectsDetailsVO::getStudentId));
            Map<Integer, List<GradebookScoreDetailsVO>> mapScore = scoreList
                    .stream()
                    .collect(Collectors.groupingBy(GradebookScoreDetailsVO::getIdStudent));
            Set<Integer> ids = mapGSD.keySet();
            List<ConfigGradeDetail> confGradeDetailsList = cgdList.stream().distinct().collect(Collectors.toList());

            List<GradebookSubjectsDetailsVO> resultList = new ArrayList<>();
            for (Integer id : ids) {
                GradebookSubjectsDetailsVO result = mapGSD.get(id).isEmpty() ? new GradebookSubjectsDetailsVO() : mapGSD.get(id).get(0);
                List<GradebookScoreDetailsVO> scoreListOfStudent = mapScore.get(id).stream().distinct().collect(Collectors.toList());
                result.setListScore(scoreListOfStudent);
                resultList.add(result);
            }
            resultList.sort((o1, o2) -> Collator.getInstance().compare(o1.getStudentName().substring(o1.getStudentName().lastIndexOf(' ') + 1), o2.getStudentName().substring(o2.getStudentName().lastIndexOf(' ') + 1)));
            // doc file
            if (file == null) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Vui lòng nhập file", null);
            }
            if (isExcelFormat(file)) {
                Workbook workbook = new XSSFWorkbook(file.getInputStream());
                Sheet sheet = workbook.getSheetAt(0);

                List<ConfigGradeDetail> confGradeDetailsListInExcel = new ArrayList<>();
                String idForImport = sheet.getRow(100).getCell(20).getStringCellValue().trim();
                List<String> confGradeDetailsIdList = Arrays.asList(idForImport.split("\\s"));
                for (String id : confGradeDetailsIdList) {
                    confGradeDetailsListInExcel.addAll(confGradeDetailsList.stream().filter(csd -> csd.getId() == Integer.parseInt(id.trim())).collect(Collectors.toList()));
                }

                if (sheet.getRow(4).getPhysicalNumberOfCells() != confGradeDetailsListInExcel.size() + 3) {
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Vui lòng chọn, nhập đầy đủ cột điểm", null);
                }
                //check đầu điểm
                Integer index = 3;
                for (int i = 0; i < confGradeDetailsListInExcel.size(); i++) {
                    if (!confGradeDetailsListInExcel.get(i).getName().trim().equals(sheet.getRow(3).getCell(index).getStringCellValue().trim())) {
                        return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng 3, tên đầu điểm thứ " + (i + 1) + " đang sai ", null);
                    }
                    index++;
                }
                try {
                    for (int i = 4; i < sheet.getPhysicalNumberOfRows(); i++) {
                        final Row row = sheet.getRow(i);
                        if (row.getCell(1).getCellTypeEnum() == CellType.BLANK) {
                            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + (i + 1) + " ,cột Mã học sinh không được để trống", null);
                        } else if (!resultList.get(i - 4).getStudentCode().equals(row.getCell(1).getStringCellValue().trim())) {
                            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + (i + 1) + " , Mã học sinh đang sai", null);

                        }
                        if (row.getCell(2).getCellTypeEnum() == CellType.BLANK) {
                            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + (i + 1) + " ,cột Tên học sinh không được để trống", null);
                        } else if (!resultList.get(i - 4).getStudentName().equals(row.getCell(2).getStringCellValue().trim())) {
                            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + (i + 1) + " , Tên học sinh đang sai", null);

                        }
                        for (int j = 3; j < confGradeDetailsListInExcel.size() + 3; j++) {
                            Cell cell = row.getCell(j);
                            if (cell.getCellTypeEnum() == CellType.BLANK) {// khong nhap diem thi nhay sang o tiepư
                                continue;
                            }
                            resultList.get(i - 4).getListScore().get(j - 3).setValue(row.getCell(j).getStringCellValue().trim());
                            resultList.get(i - 4).getListScore().get(j - 3).setTimes(1);
                            resultList.get(i - 4).setUpdater(username);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Cập nhật điểm không thành công", null);
                }
            } else {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "File không đúng định dạng", null);
            }
            gradeBookForm.setSubjectsDetails(resultList);
            ServiceResult<Boolean> saveResultToDB = save(gradeBookForm, login);
            return saveResultToDB;
        } catch (Exception e) {
            e.printStackTrace();
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Cập nhật điểm thất bại, có lỗi xảy ra ", null);
        }

    }

    @Override
    public ServiceResult<List<Map<String, Object>>> getListScoreOfEachSubject(String year, Integer semester, String classCode, String studentCode) {
        try {
            return new ServiceResult<>(HttpStatus.OK, "success", gradebookRepository.getListScoreOfEachSubject(year, semester, classCode, studentCode));
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "fail", null);
        }

    }


    public Boolean isExcelFormat(MultipartFile file) {
        return file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    @Override
    public ServiceResult<Boolean> checkGradeBookToChangeClass(String year, String studentCode) {
        if (gradebookRepository.checkExistScoreToChangeClass(year, studentCode)) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Không thể đổi lớp", null);
        } else {
            return new ServiceResult<>(HttpStatus.OK, "Có thể đổi lớp", null);
        }
    }
}