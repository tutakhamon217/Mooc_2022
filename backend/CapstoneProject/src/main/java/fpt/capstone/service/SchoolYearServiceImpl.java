package fpt.capstone.service;

import fpt.capstone.form_data.SchoolYearForm;
import fpt.capstone.payroll.ValidationException;
import fpt.capstone.entities.SchoolYear;
import fpt.capstone.entities.ServiceResult;

import fpt.capstone.repository.ScheduleTimetableRepository;
import fpt.capstone.repository.SchoolYearRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class SchoolYearServiceImpl implements SchoolYearService {

    @Autowired
    private SchoolYearRespository schoolYearRepository;

    @Autowired
    private ScheduleTimetableRepository scheduleTimetableRepository;


    @Override
    public <S extends SchoolYear> List<S> saveAll(Iterable<S> entities) {
        return schoolYearRepository.saveAll(entities);
    }

    @Override
    public List<SchoolYear> getSchoolYearByYears(String years) {
        return schoolYearRepository.getSchoolYearByYearsOrderBySemesterAsc(years);
    }


    @Override
    public void deleteSchoolYear(SchoolYear schoolYear) {
        schoolYearRepository.delete(schoolYear);
    }

    @Override
    public ServiceResult<Map<String, Object>> getAllSchoolYearPaging() {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        try {
            List<Map<String, Object>> all = schoolYearRepository.findAllGroupYear();

            List<Map<String, Object>> collect = all.stream().map(x -> {
                Map<String, Object> data = new HashMap<>();
                data.put("year", x.get("years"));
                data.put("semesterAmount", x.get("semester_amount"));
                data.put("canUpdate", x.get("canUpdate"));
                data.put("semesters", schoolYearRepository.getSemesterInfor(x.get("years").toString()));
                return data;

            }).collect(Collectors.toList());
            Map<String, Object> output = new HashMap<>();
            output.put("schoolYears", collect);

            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(output);
        } catch (Exception e) {
            serviceResult.setMessage("fail");
            serviceResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return serviceResult;
    }

    @Override
    public ServiceResult<Map<String, Object>> saveSchoolYear(SchoolYearForm schoolYearForm) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        int semesterAmount = Integer.parseInt(schoolYearForm.getSemesterAmount());
        List<SchoolYear> schoolYears = new ArrayList<>();
        String years = schoolYearForm.getYears();
        Integer yearCurrent = Integer.parseInt(Arrays.stream(years.split("-")).toArray()[1].toString());
        Integer yearNext = yearCurrent + 1;
        Integer yearPast1 = yearCurrent - 1;
        Integer yearPast2 = yearCurrent - 2;
        DateTimeFormatter ft = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        Map<String, Object> output = new HashMap<>();
        if (LocalDateTime.parse(schoolYearForm.getToDate1(), ft).isBefore(LocalDateTime.parse(schoolYearForm.getFromDate1(), ft).plusDays(1))) {
            serviceResult.setMessage("Ng??y b???t ?????u h???c k??? 1 kh??ng ???????c v?????t qu?? ng??y k???t th??c h???c k??? 1");
            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
            return serviceResult;
        }
        if (schoolYearRepository.getSchoolYearByYearsOrderBySemesterAsc(yearPast2 + "-" + yearPast1).size() > 0) {
            if (LocalDateTime.parse(schoolYearForm.getFromDate1(), ft).isBefore(schoolYearRepository.getSchoolYearByYearsOrderBySemesterAsc(yearPast2 + "-" + yearPast1).get(schoolYearRepository.getSchoolYearByYearsOrderBySemesterAsc(yearPast2 + "-" + yearPast1).size() - 1).getToDate().plusDays(1))) {
                serviceResult.setMessage("Ng??y b???t ?????u c???a n??m h???c th??m m???i kh??ng ???????c tr?????c ng??y cu???i c??ng c???a n??m h???c tr?????c ????");
                serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                return serviceResult;
            }
        }
        if (schoolYearForm.getSemesterAmount().equals("2")) {
            if (LocalDateTime.parse(schoolYearForm.getToDate1(), ft).isAfter(LocalDateTime.parse(schoolYearForm.getFromDate2(), ft))) {
                serviceResult.setMessage("Ng??y b???t ?????u h???c k??? 2 kh??ng ???????c tr?????c ng??y k???t th??c h???c k??? 1");
                serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                return serviceResult;
            }
            if (LocalDateTime.parse(schoolYearForm.getToDate2(), ft).isBefore(LocalDateTime.parse(schoolYearForm.getFromDate2(), ft).plusDays(1))) {
                serviceResult.setMessage("Ng??y b???t ?????u h???c k??? 2 kh??ng ???????c v?????t qu?? ng??y k???t th??c h???c k??? 2");
                serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                return serviceResult;
            }
            if (schoolYearRepository.getSchoolYearByYearsOrderBySemesterAsc(yearCurrent + "-" + yearNext).size() > 0) {
                if (LocalDateTime.parse(schoolYearForm.getToDate2(), ft).isAfter(schoolYearRepository.getSchoolYearByYearsOrderBySemesterAsc(yearCurrent + "-" + yearNext).get(0).getFromDate())) {
                    serviceResult.setMessage("Ng??y k???t th??c h???c k??? cu???i c???a n??m h???c th??m m???i kh??ng ???????c v?????t qu?? ng??y ?????u ti??n c???a n??m ti???p theo ");
                    serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                    return serviceResult;
                }
            }
        } else {
            System.out.println(yearCurrent + "-" + yearNext);
            System.out.println(schoolYearRepository.getSchoolYearByYearsOrderBySemesterAsc(yearCurrent + "-" + yearNext).size());
            if (schoolYearRepository.getSchoolYearByYearsOrderBySemesterAsc(yearCurrent + "-" + yearNext).size() > 0) {
                if (LocalDateTime.parse(schoolYearForm.getToDate1(), ft).isAfter(schoolYearRepository.getSchoolYearByYearsOrderBySemesterAsc(yearCurrent + "-" + yearNext).get(0).getFromDate())) {
                    serviceResult.setMessage("Ng??y k???t th??c h???c k??? cu???i c???a n??m h???c th??m m???i kh??ng ???????c v?????t qu?? ng??y ?????u ti??n c???a h???c k??? 1 n??m ti???p theo ");
                    serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                    return serviceResult;
                }
            }
        }

        try {
            schoolYears = getSchoolYearByYears(years);
            if (schoolYears.size() == 0) {
                if (schoolYearForm.getFromDate2() == null) {
                    schoolYears.add(new SchoolYear(schoolYearForm.getYears(), semesterAmount, 1 + "", LocalDateTime.parse(schoolYearForm.getFromDate1(), ft)
                            , LocalDateTime.parse(schoolYearForm.getToDate1(), ft), LocalDateTime.now(), username, null, null));
                } else {
                    schoolYears.add(new SchoolYear(schoolYearForm.getYears(), semesterAmount, 1 + "", LocalDateTime.parse(schoolYearForm.getFromDate1(), ft)
                            , LocalDateTime.parse(schoolYearForm.getToDate1(), ft), LocalDateTime.now(), username, null, null));
                    schoolYears.add(new SchoolYear(schoolYearForm.getYears(), semesterAmount, 2 + "", LocalDateTime.parse(schoolYearForm.getFromDate2(), ft)
                            , LocalDateTime.parse(schoolYearForm.getToDate2(), ft), LocalDateTime.now(), username, null, null));
                }
            } else {
                if (scheduleTimetableRepository.isExistedByYear(schoolYearForm.getYears())) {
                    serviceResult.setMessage("N??m h???c ???? ???????c x???p th???i kh??a bi???u n??n kh??ng th??? c???p nh???t");
                    serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                    serviceResult.setData(output);
                    return serviceResult;
                }
                // LocalDateTime yearNow = LocalDateTime.now();
                // if (schoolYears.size() == 1) {
                //     if (!schoolYears.get(0).getFromDate().isBefore(yearNow)||!schoolYears.get(0).getToDate().isAfter(yearNow)) {
                //         serviceResult.setMessage("Kh??ng th??? c???p nh???t n??m h???c ???? di???n ra");
                //         serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                //         serviceResult.setData(output);
                //         return serviceResult;
                //     }
                // }
                // else{
                //     if (!schoolYears.get(0).getFromDate().isBefore(yearNow)||!schoolYears.get(1).getToDate().isAfter(yearNow)) {
                //         serviceResult.setMessage("Kh??ng th??? c???p nh???t n??m h???c ???? di???n ra");
                //         serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                //         serviceResult.setData(output);
                //         return serviceResult;
                //     }
                // }
                if (schoolYearForm.getSemesterAmount().equals("1")) {
                    schoolYears.get(0).setFromDate(LocalDateTime.parse(schoolYearForm.getFromDate1(), ft));
                    schoolYears.get(0).setSemesterAmount(1);
                    schoolYears.get(0).setToDate(LocalDateTime.parse(schoolYearForm.getToDate1(), ft));
                    schoolYears.get(0).setUpdateName(username);
                    schoolYears.get(0).setUpdateTime(LocalDateTime.now());
                    schoolYears.get(0).setSemesterAmount(Integer.parseInt(schoolYearForm.getSemesterAmount()));
                } else {
                    if (schoolYears.size() == 1) {
                        schoolYears.get(0).setFromDate(LocalDateTime.parse(schoolYearForm.getFromDate1(), ft));
                        schoolYears.get(0).setToDate(LocalDateTime.parse(schoolYearForm.getToDate1(), ft));
                        schoolYears.get(0).setUpdateName(username);
                        schoolYears.get(0).setUpdateTime(LocalDateTime.now());
                        schoolYears.get(0).setSemesterAmount(Integer.parseInt(schoolYearForm.getSemesterAmount()));
                        schoolYears.add(new SchoolYear(schoolYearForm.getYears(), semesterAmount, 2 + "", LocalDateTime.parse(schoolYearForm.getFromDate2(), ft)
                                , LocalDateTime.parse(schoolYearForm.getToDate2(), ft), LocalDateTime.now(), username, null, null));
                    } else {
                        if (schoolYears.get(0).getSemester().equals("1")) {
                            schoolYears.get(0).setFromDate(LocalDateTime.parse(schoolYearForm.getFromDate1(), ft));
                            schoolYears.get(0).setToDate(LocalDateTime.parse(schoolYearForm.getToDate1(), ft));
                            schoolYears.get(0).setUpdateName(username);
                            schoolYears.get(0).setUpdateTime(LocalDateTime.now());
                            schoolYears.get(0).setSemesterAmount(Integer.parseInt(schoolYearForm.getSemesterAmount()));

                            schoolYears.get(1).setFromDate(LocalDateTime.parse(schoolYearForm.getFromDate2(), ft));
                            schoolYears.get(1).setToDate(LocalDateTime.parse(schoolYearForm.getToDate2(), ft));
                            schoolYears.get(1).setUpdateName(username);
                            schoolYears.get(1).setUpdateTime(LocalDateTime.now());
                            schoolYears.get(1).setSemesterAmount(Integer.parseInt(schoolYearForm.getSemesterAmount()));

                        } else {
                            schoolYears.get(1).setFromDate(LocalDateTime.parse(schoolYearForm.getFromDate1(), ft));
                            schoolYears.get(1).setToDate(LocalDateTime.parse(schoolYearForm.getToDate1()));
                            schoolYears.get(1).setUpdateName(username);
                            schoolYears.get(1).setUpdateTime(LocalDateTime.now());
                            schoolYears.get(1).setSemesterAmount(Integer.parseInt(schoolYearForm.getSemesterAmount()));
                            schoolYears.get(0).setSemesterAmount(Integer.parseInt(schoolYearForm.getSemesterAmount()));
                            schoolYears.get(0).setFromDate(LocalDateTime.parse(schoolYearForm.getFromDate2(), ft));
                            schoolYears.get(0).setToDate(LocalDateTime.parse(schoolYearForm.getToDate2(), ft));
                            schoolYears.get(0).setUpdateName(username);
                            schoolYears.get(0).setUpdateTime(LocalDateTime.now());
                        }
                    }
                }
            }
            if (saveAll(schoolYears) != null) {
                output.put("saveResult", "Th??m n??m ho??c tha??nh c??ng");
                if (schoolYearForm.getFromDate2() == null && schoolYears.size() == 2) {
                    schoolYearRepository.deleteById(schoolYears.get(1).getId());
                }
            } else {
                output.put("saveResult", "Th??m n??m ho??c th????t ba??i");
            }
            serviceResult.setMessage("Th??m n??m ho??c tha??nh c??ng");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(output);

        } catch (Exception e) {
            e.printStackTrace();
            serviceResult.setMessage("Th??m n??m ho??c th????t ba??i");
            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
        }
        return serviceResult;

    }

    @Override
    public ServiceResult<List<String>> getAllSchoolYears() {
        return new ServiceResult<>(HttpStatus.OK, "All school years", schoolYearRepository.getAllSchoolYear());
    }

    @Override
    public Map<String, Object> getSemesterAmount(String years) {
        Map<String, Object> output = new HashMap<>();
        output.put("semesterAmount", schoolYearRepository.getSemesterAmount(years));
        return output;
    }

    @Override
    public ServiceResult<Boolean> checkStartEndSemester(String years, Integer semester) {

        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        Timestamp tmp = null;
        if (semester == 0) {
            List<Map<String, Object>> rangeDate = schoolYearRepository.getStartEndSemester(years, 1);
            if (rangeDate.size() == 0) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Semester range", null);
            }
            tmp = (Timestamp) rangeDate.get(0).get("from_date");
            startDate = tmp.toLocalDateTime();
            tmp = (Timestamp) rangeDate.get(0).get("to_date");
            endDate = tmp.toLocalDateTime();
            rangeDate = schoolYearRepository.getStartEndSemester(years, 2);
            if (rangeDate.size() > 0) {
                tmp = (Timestamp) rangeDate.get(0).get("from_date");
                startDate = tmp.toLocalDateTime();
                tmp = (Timestamp) rangeDate.get(0).get("to_date");
                endDate = tmp.toLocalDateTime();
            }
        } else {
            List<Map<String, Object>> rangeDate = schoolYearRepository.getStartEndSemester(years, semester);
            if (rangeDate.size() == 0) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Semester range", null);
            }


            tmp = (Timestamp) rangeDate.get(0).get("from_date");
            startDate = tmp.toLocalDateTime();
            tmp = (Timestamp) rangeDate.get(0).get("to_date");
            endDate = tmp.toLocalDateTime();
            ;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        LocalDateTime ltn = LocalDateTime.now();

        if ((startDate.isBefore(ltn) || startDate.isEqual(ltn)) && (endDate.isAfter(ltn) || endDate.isEqual(ltn))) {
            return new ServiceResult<>(HttpStatus.OK, "Semester range", null);
        } else {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Th????i gian ??a??nh gia?? b????t ??????u t???? " + dtf.format(startDate) + " va?? k????t thu??c va??o " + dtf.format(endDate), null);
        }
    }

    @Override
    public ServiceResult<Map<String, Object>> getCurrentSchoolyear() {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        Map<String, Object> output = new HashMap<>();
        try {
            int year = Year.now().getValue();
            int nextyear = year + 1;
            String schoolyear = year + "-" + nextyear;
            System.out.println(schoolyear);
            output.put("currentSchoolYear", schoolYearRepository.getSchoolYearByYearsOrderBySemesterAsc(schoolyear));
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(output);
        } catch (Exception e) {
            e.printStackTrace();
            serviceResult.setMessage("L????y n??m ho??c hi????n ta??i th????t ba??i");
            serviceResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<SchoolYear> getSchoolYear(String year, String semester) {
        SchoolYear sy = schoolYearRepository.getSchoolYearOptional(year, semester).orElse(null);
        return new ServiceResult<SchoolYear>(HttpStatus.OK, "school year", sy);
    }

    @Override
    public ServiceResult<SchoolYear> getSchoolYearOf(String date) {
        Date attendanceDate = null;
        try {
            attendanceDate = (new SimpleDateFormat("yyyy-MM-dd")).parse(date);
        } catch (Exception e) {
            throw new ValidationException("date", "ng??y ??i???m danh kh??ng h???p l???");
        }
        return new ServiceResult<>(HttpStatus.OK, "school year", schoolYearRepository.getSemesterOf(attendanceDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()).orElse(null));
    }

    @Override
    public ServiceResult<List<String>> getSchoolYearOfHistoryStudent(String studentCode) {
        try {
            return new ServiceResult<>(HttpStatus.OK, "success", schoolYearRepository.getSchoolYearOfHistoryStudent(studentCode));
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "L???y n??m h???c th???t b???i", null);
        }
    }

    @Override

    public ServiceResult<List<String>> getCurrentAndNextSchoolYear() {
        try {
            String currentYear = schoolYearRepository.getCurrentSchoolYear();
            if (null == currentYear) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Ch??a c?? n??m h???c hi???n t???i", null);
            }
            Integer yearNow = Integer.parseInt(currentYear.substring(5, 9));
            Integer yearNext = yearNow + 1;
            List<String> result = new ArrayList<>();
            result.add(currentYear);
            if (schoolYearRepository.getSchoolYearByYears(yearNow + "-" + yearNext).size() == 0) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "success", result);
            } else {
                result.add(yearNow + "-" + yearNext);
                return new ServiceResult<>(HttpStatus.OK, "success", result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "fail", null);
        }
    }

    public ServiceResult<SchoolYear> getObjCurrentYear() {
        try {
            LocalDateTime now = LocalDateTime.now();
            List<SchoolYear> list = schoolYearRepository.getAllSchoolYearNoDistinct().get();
            for (int i = 0; i < list.size(); i++) {
                Optional<SchoolYear> schoolYear = schoolYearRepository.getSemesterOf(now);
                System.out.println(now);
                if (!schoolYear.equals(Optional.empty())) {
                    return new ServiceResult<>(HttpStatus.OK, "success", schoolYear.get());
                }
            }
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Kh??ng t???n t???i n??m h???c n??o", null);
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Kh??ng t???n t???i n??m h???c n??o", null);

        }
    }

    @Override
    public ServiceResult<List<Map<String, Object>>> getRangeOfSemester(String year, Integer semester) {
        try{
            return new ServiceResult<>(HttpStatus.OK, "success", schoolYearRepository.getRangeOfSemester(year, semester));
        }catch (Exception e){
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Kh??ng t???n t???i h???c k??? n??y", null);
        }
    }
}
