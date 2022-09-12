package fpt.capstone.service;

import fpt.capstone.entities.AssessStudentConduct;
import fpt.capstone.entities.AssessStudentConductDetail;
import fpt.capstone.entities.SchoolYear;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.AssessStudentConductDetailForm;
import fpt.capstone.repository.*;
import fpt.capstone.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class AssessStudentConductDetailServiceImpl implements AssessStudentConductDetailService{
    @Autowired
    AssessStudentConductDetailRepository assessStudentConductDetailRepository;
    @Autowired
    AssessStudentConductRepository assessStudentConductRepository;
    @Autowired
    SchoolYearRespository schoolYearRespository;
    @Autowired
    GradebookRepository gradebookRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AttendanceStudentRepository attendanceStudentRepository;

    @Override
    public ServiceResult<List<Map<String, Object>>> getInfoConductOfClass(Integer semester, String year, Integer teacherId, String classCode) {
        ServiceResult<List<Map<String, Object>>> serviceResult = new ServiceResult();
        try{
            if(gradebookRepository.checkExistGradeBook(year,semester,classCode)){
                if(semester == 0){
                    List<Map<String, Object>> output = assessStudentConductDetailRepository.getConductOfClassByAllStudyYear(year, classCode);
                    if(output.size() == 0){
                        serviceResult.setMessage("Lớp này không có học sinh nào");
                        serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                        return serviceResult;
                    }else {
                        List<Map<String, Object>> listEnough = new ArrayList<>();
                        //check kỳ 1 và 2 đã được đánh giá chưa
                        int amountOfSemester = schoolYearRespository.getSemesterAmount(year);
                        if(amountOfSemester == 2){
                            for (Map<String, Object> m : output){
                                int enough = 0;
                                List<Map<String, Object>> semester1 = assessStudentConductDetailRepository.getEvaluateAndCompetitionBySemester(m.get("student_code").toString(), 1, year);
                                List<Map<String, Object>> semester2 = assessStudentConductDetailRepository.getEvaluateAndCompetitionBySemester(m.get("student_code").toString(), 2, year);
                                if(semester1.size() != 0 && semester2.size() != 0){
                                    if(semester1.get(0).get("conduct") != null
                                    && !semester1.get(0).get("conduct").toString().trim().equals("")
                                    && semester1.get(0).get("competition_title") != null
                                    && !semester1.get(0).get("competition_title").toString().trim().equals("")
                                    && semester2.get(0).get("conduct") != null
                                    && !semester2.get(0).get("conduct").toString().trim().equals("")
                                    && semester2.get(0).get("competition_title") != null
                                    && !semester2.get(0).get("competition_title").toString().trim().equals("")){
                                        enough = 1;
                                    }
                                }
                                Map<String, Object> newMap = new HashMap<>();
                                newMap.put("id", m.get("id"));
                                newMap.put("full_name", m.get("full_name"));
                                newMap.put("student_code", m.get("student_code"));
                                newMap.put("number_off", m.get("number_off"));
                                newMap.put("number_off_allowed", m.get("number_off_allowed"));
                                newMap.put("academic_ability", m.get("academic_ability"));
                                newMap.put("conduct", m.get("conduct"));
                                newMap.put("competition_title", m.get("competition_title"));
                                newMap.put("evaluate", m.get("evaluate"));
                                newMap.put("parent_code", m.get("parent_code"));
                                newMap.put("enough", enough);
                                listEnough.add(newMap);
                            }
                        }else if(amountOfSemester == 1){
                            for (Map<String, Object> m : output){
                                int enough = 0;
                                List<Map<String, Object>> semester1 = assessStudentConductDetailRepository.getEvaluateAndCompetitionBySemester(m.get("student_code").toString(), 1, year);
                                if(semester1.size() != 0){
                                    if(semester1.get(0).get("conduct") != null
                                            && !semester1.get(0).get("conduct").toString().trim().equals("")
                                            && semester1.get(0).get("competition_title") != null
                                            && !semester1.get(0).get("competition_title").toString().trim().equals("")
                                            ){
                                        enough = 1;
                                    }
                                }
                                Map<String, Object> newMap = new HashMap<>();
                                newMap.put("id", m.get("id"));
                                newMap.put("full_name", m.get("full_name"));
                                newMap.put("student_code", m.get("student_code"));
                                newMap.put("number_off", m.get("number_off"));
                                newMap.put("number_off_allowed", m.get("number_off_allowed"));
                                newMap.put("academic_ability", m.get("academic_ability"));
                                newMap.put("conduct", m.get("conduct"));
                                newMap.put("competition_title", m.get("competition_title"));
                                newMap.put("evaluate", m.get("evaluate"));
                                newMap.put("parent_code", m.get("parent_code"));
                                newMap.put("enough", enough);
                                listEnough.add(newMap);
                            }
                        }

                        serviceResult.setMessage("success");
                        serviceResult.setStatus(HttpStatus.OK);
                        serviceResult.setData(listEnough);
                    }
                }else{
                    List<Map<String, Object>> output = assessStudentConductDetailRepository.getInfoConductOfClass(semester, year, classCode);
//                    List<Map<String, Object>> newList = new ArrayList<>();
//                    if(output.size() == 0){
//                        output = assessStudentConductDetailRepository.getInfoConductOfClassNotYet(classCode ,year,semester);
                        if(output.size() == 0){
                            serviceResult.setMessage("Lớp này không có học sinh nào");
                            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                            return serviceResult;
                        }
//                        for (Map<String, Object> m : output){
//                            Map<String, Object> newMap = new HashMap<>();
//                            if()
//                            newMap.put("full_name", m.get("full_name"));
//                            newMap.put("student_code", m.get("student_code"));
//                            newMap.put("number_off", m.get("number_off"));
//                            newMap.put("number_off_allowed", m.get("number_off_allowed"));
//                            newMap.put("academic_ability", m.get("academic_ability"));
//                            newMap.put("conduct", null);
//                            newMap.put("competition_title", null);
//                            newMap.put("evaluate", m.get("evaluate"));
//                            newList.add(newMap);
//                        }
                        serviceResult.setData(output);
//                    }else{
//                        serviceResult.setData(output);
//                    }
                }
                serviceResult.setMessage("success");
                serviceResult.setStatus(HttpStatus.OK);
            }else{
                String semes = semester == 0 ? "cả năm" : semester + "";
                serviceResult.setMessage("Kỳ học " + semes + " của năm học " + year +" chưa được cấu hình điểm");
                serviceResult.setStatus(HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            serviceResult.setMessage("Tìm kiếm thất bại");
            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<List<Map<String, Object>>> getEvaluateOfTeacher(Integer semester, String year, String studentCode, String classCode) {
        ServiceResult<List<Map<String, Object>>> serviceResult = new ServiceResult();
        try{
            List<Map<String, Object>> output = assessStudentConductDetailRepository.getEvaluateOfTeacher(semester, year, studentCode, classCode);
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(output);
        }catch (Exception e){
            serviceResult.setMessage("Có lỗi xảy ra");
            serviceResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<Boolean> updateAssessStudentConductDetail(List<AssessStudentConductDetailForm> list, String classCode, Integer semester, String schoolYear) {
        ServiceResult serviceResult = new ServiceResult();
        try{
            List<AssessStudentConductDetail> listSave = new ArrayList<>();

            List<AssessStudentConductDetailForm> listOld = new ArrayList<>();
            List<AssessStudentConductDetailForm> listNew = new ArrayList<>();
            //phân loại cũ và mới
            for (AssessStudentConductDetailForm a: list){
                if(a.getId() == null){
                    listNew.add(a);
                }else{
                    listOld.add(a);
                }
            }

            //Lấy ra list đã từng được đánh giá hạnh kiểm
            for(AssessStudentConductDetailForm a : listOld){
                AssessStudentConductDetail data = new AssessStudentConductDetail();
                data.setId(a.getId());
                data.setStudent_code(a.getStudent_code());
                data.setNumber_off(a.getNumber_off());
                data.setNumber_off_allowed(a.getNumber_off_allowed());
                data.setAcademic_ability(a.getAcademic_ability());
                data.setConduct(a.getConduct());
                data.setCompetition_title(a.getCompetition_title());
                data.setParent_code(a.getParent_code());
                listSave.add(data);
            }
            if(listOld.size() > 0){
                //update lại listOld vào db
                assessStudentConductDetailRepository.saveAll(listSave);
                listSave = new ArrayList<>();
                if(listNew.size() > 0){
                    for (AssessStudentConductDetailForm a : listNew){
                        AssessStudentConductDetail data = new AssessStudentConductDetail();
                        data.setStudent_code(a.getStudent_code());
                        data.setNumber_off(a.getNumber_off());
                        data.setNumber_off_allowed(a.getNumber_off_allowed());
                        data.setAcademic_ability(a.getAcademic_ability());
                        data.setConduct(a.getConduct());
                        data.setCompetition_title(a.getCompetition_title());
                        data.setParent_code(listOld.get(0).getParent_code());
                        listSave.add(data);
                    }
                    assessStudentConductDetailRepository.saveAll(listSave);
                }
            }else{
                // nghĩa là lớp này chưa từng được đánh giá hạnh kiểm
                AssessStudentConduct asc = new AssessStudentConduct();
                asc.setClass_code(classCode);
                asc.setSemester(semester);
                asc.setSchool_year(schoolYear);
                asc.setCreate_date(LocalDateTime.now());
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
                asc.setCreator(userDetails.getUsername());
                Date now = new Date();
                Timestamp timestamp = new Timestamp(now.getTime());
                String code = "AS_" + timestamp.getTime();
                asc.setCode(code);
                asc = assessStudentConductRepository.save(asc);
                for(AssessStudentConductDetailForm a : listNew){
                    AssessStudentConductDetail data = new AssessStudentConductDetail();
                    data.setStudent_code(a.getStudent_code());
                    data.setNumber_off(a.getNumber_off());
                    data.setNumber_off_allowed(a.getNumber_off_allowed());
                    data.setAcademic_ability(a.getAcademic_ability());
                    data.setConduct(a.getConduct());
                    data.setCompetition_title(a.getCompetition_title());
                    data.setParent_code(asc.getCode());
                    listSave.add(data);
                }
                assessStudentConductDetailRepository.saveAll(listSave);
            }
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setMessage("Cập nhật thành công");
            serviceResult.setData(true);
        }catch (Exception e){
            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
            serviceResult.setMessage("Cập nhật thất bại");
            serviceResult.setData(false);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<List<Map<String, Object>>> getReportTheResultOfCompetition(String year, Integer deptId, Integer gradeLevel, String classCode, Integer semester) {
        ServiceResult<List<Map<String, Object>>> serviceResult = new ServiceResult();
        try{
            List<Map<String, Object>> list = assessStudentConductDetailRepository.getReportTheResultOfCompetition(gradeLevel,deptId,semester,classCode,year);
            if(list.size() != 0){
                int dept = (int) list.get(0).get("deptId");
                Map<String, Object> object = list.get(0);
                List<Map<String, Object>> newList = new ArrayList<>();
                Map<String, Object> addRecord = new HashMap<>();
                addRecord.put("className", object.get("deptName"));
                addRecord.put("excellent", new BigDecimal(0));
                addRecord.put("good", new BigDecimal(0));
                addRecord.put("medium", new BigDecimal(0));
                addRecord.put("weak", new BigDecimal(0));
                addRecord.put("total", new BigDecimal(0));
                List<Map<String, Object>> listTemp = new ArrayList<>();
                int stt = 2;
                for (int i = 0; i < list.size(); i++){
                    if(dept == (int) list.get(i).get("deptId")){
                        addRecord.put("excellent",((BigDecimal) addRecord.get("excellent")).add((BigDecimal) list.get(i).get("excellent")));
                        addRecord.put("good", ((BigDecimal)addRecord.get("good")).add((BigDecimal) list.get(i).get("good")));
                        addRecord.put("medium",((BigDecimal) addRecord.get("medium")).add((BigDecimal) list.get(i).get("medium")));
                        addRecord.put("weak", ((BigDecimal) addRecord.get("weak")).add((BigDecimal) list.get(i).get("weak")));
                        addRecord.put("total",((BigDecimal) addRecord.get("total")).add((BigDecimal) list.get(i).get("excellent")).add((BigDecimal) list.get(i).get("good")).add((BigDecimal) list.get(i).get("medium")).add((BigDecimal) list.get(i).get("weak")));

                        Map<String, Object> oldItem = new HashMap<>();
                        oldItem.put("className", list.get(i).get("className"));
                        oldItem.put("classCode", list.get(i).get("classCode"));
                        oldItem.put("deptId", list.get(i).get("deptId"));
                        oldItem.put("deptName", list.get(i).get("deptName"));
                        oldItem.put("excellent", list.get(i).get("excellent"));
                        oldItem.put("good", list.get(i).get("good"));
                        oldItem.put("gradeLevel", list.get(i).get("gradeLevel"));
                        oldItem.put("medium", list.get(i).get("medium"));
                        oldItem.put("semester", list.get(i).get("semester"));
                        oldItem.put("teacherName", list.get(i).get("teacherName"));
                        oldItem.put("weak", list.get(i).get("weak"));
                        oldItem.put("stt", stt++);

                        listTemp.add(oldItem);
                        if(i + 1 == list.size()){
                            addRecord.put("stt", stt - listTemp.size() - 1);
                            addRecord.put("subResult", listTemp);
                            newList.add(addRecord);
                        }
                    }else{
                        addRecord.put("stt", stt++ - listTemp.size() - 1);
                        addRecord.put("subResult", listTemp);
                        listTemp = new ArrayList<>();
                        newList.add(addRecord);
                        addRecord = new HashMap<>();
                        object = list.get(i);
                        addRecord.put("className", object.get("deptName"));
                        addRecord.put("excellent", new BigDecimal(0));
                        addRecord.put("good", new BigDecimal(0));
                        addRecord.put("medium", new BigDecimal(0));
                        addRecord.put("weak", new BigDecimal(0));
                        addRecord.put("total", new BigDecimal(0));
                        dept = (int) list.get(i).get("deptId");
                        i--;
                    }
                }
                serviceResult.setData(newList);
            }else{
                serviceResult.setData(list);
            }
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
        }catch (Exception e){
            serviceResult.setMessage("fail");
            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<List<Map<String, Object>>> getReportTheResultOfEachClass(String year, String classCode, Integer semester) {
        ServiceResult<List<Map<String, Object>>> serviceResult = new ServiceResult();
        try{
            if(studentRepository.getAmountOfStudentByClassCodeAndYear(classCode, year) == 0){
                serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                serviceResult.setMessage("Lớp này chưa có học sinh nào");
                return serviceResult;
            }
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setMessage("success");
            List<Map<String, Object>> list = assessStudentConductDetailRepository.getReportTheResultOfEachClass(year, classCode, semester);
            if(list.size() != 0){
                String studentCode = list.get(0).get("studentCode").toString().trim();
                List<Map<String, Object>> newList = new ArrayList<>();
                Set<String> set = new HashSet<>();
                Hashtable<String, Integer> htCoefficient = new Hashtable<>();
                //set này chứa danh sách những môn học
                Map<String, Object> object = new HashMap<>();
                for (int i = 0; i < list.size(); i++){
                    if(studentCode.equals(list.get(i).get("studentCode").toString().trim())){
                        if(object.get("studentCode") == null){
                            object.put("studentCode", list.get(i).get("studentCode"));
                            object.put("studentName", list.get(i).get("studentName"));
                            List<Map<String, Object>> getPandK = attendanceStudentRepository.getPandKbyYearAndSemesterAndStudentCode(year, semester, studentCode);
                            if(getPandK.get(0).get("number_off") != null){
                                object.put("number_off", getPandK.get(0).get("number_off"));
                            }else{
                                object.put("number_off", 0);
                            }
                            if(getPandK.get(0).get("number_off_allowed") != null){
                                object.put("number_off_allowed", getPandK.get(0).get("number_off_allowed"));
                            }else{
                                object.put("number_off_allowed", 0);
                            }
                        }
                        if(list.get(i).get("academic_ability") == null || list.get(i).get("academic_ability").toString().trim().equals("")){
                            if(object.get("academic_ability") == null){
                                object.put("academic_ability", null);
                            }
                        }else{
                            object.put("academic_ability", list.get(i).get("academic_ability"));
                        }
                        if(list.get(i).get("conduct") == null || list.get(i).get("conduct").toString().trim().equals("")){
                            if(object.get("conduct") == null){
                                object.put("conduct", null);
                            }
                        }else{
                            object.put("conduct", list.get(i).get("conduct"));
                        }
//                        if(list.get(i).get("number_off") == null || list.get(i).get("number_off").toString().trim().equals("")){
//                            if(object.get("number_off") == null){
//                                object.put("number_off", null);
//                            }
//                        }else{
//                            object.put("number_off", list.get(i).get("number_off"));
//                        }
//
//                        if(list.get(i).get("number_off_allowed") == null || list.get(i).get("number_off_allowed").toString().trim().equals("")){
//                            if(object.get("number_off_allowed") == null){
//                                object.put("number_off_allowed", null);
//                            }
//                        }else{
//                            object.put("number_off_allowed", list.get(i).get("number_off_allowed"));
//                        }

                        if(list.get(i).get("competition_title") == null || list.get(i).get("competition_title").toString().trim().equals("")){
                            if(object.get("competition_title") == null){
                                object.put("competition_title", null);
                            }
                        }else{
                            object.put("competition_title", list.get(i).get("competition_title"));
                        }
                        object.put(list.get(i).get("subjectName").toString(), list.get(i).get("avg_score"));
                        set.add(list.get(i).get("subjectName").toString());
                        htCoefficient.put(list.get(i).get("subjectName").toString(), Integer.parseInt(list.get(i).get("coefficient").toString()));
                        //Tính điểm trung bình cho môn cuối danh sách
                        if(i + 1 == list.size()){
                            Iterator<String> iterator = set.iterator();
                            double avg  = 0;
                            int counter = 0;
                            while (iterator.hasNext()){
                                String ite = iterator.next();
                                if(object.containsKey(ite)){
                                    try{
                                        double doub = Double.parseDouble(object.get(ite).toString());
                                        if(!object.get(ite).toString().trim().equals("")){
                                            avg += (doub * htCoefficient.get(ite));
                                            counter += htCoefficient.get(ite);
                                        }
                                    }catch (Exception e){
                                    }
                                }
                            }
                            if(avg == 0 && counter == 0){
                                object.put("avg", null);
                            }else{
                                object.put("avg", String.format("%.3g%n", avg / counter));
                            }
                            newList.add(object);
                        }
                    }else{
                        //Tính điểm trung bình cho môn học
                        Iterator<String> iterator = set.iterator();
                        double avg  = 0;
                        int counter = 0;
                        while (iterator.hasNext()){
                            String ite = iterator.next();
                            if(object.containsKey(ite)){
                                try{
                                    double doub = Double.parseDouble(object.get(ite).toString());
                                    if(!object.get(ite).toString().trim().equals("")){
                                        avg += (doub * htCoefficient.get(ite));
                                        counter += htCoefficient.get(ite);
                                    }
                                }catch (Exception e){
                                }
                            }
                        }
                        if(avg == 0 && counter == 0){
                            object.put("avg", null);
                        }else{
                            object.put("avg", String.format("%.3g%n", avg / counter));
                        }
                        //
                        newList.add(object);
                        studentCode = list.get(i).get("studentCode").toString().trim();
                        object = new HashMap<>();
                        i--;
                    }
                }

                List<Map<String, Object>> output = new ArrayList<>();
                Map<String, Object> outputDesserts = new HashMap<>();
                List<Map<String, Object>> sort = new ArrayList<>();
                //lấy ra mảng những học sinh có điểm trung bình, gắn vào mảng sort
                for (int i = 0; i < newList.size(); i++){
                    if(newList.get(i).get("avg") != null){
                        sort.add(newList.get(i));
                    }else{
                        newList.get(i).put("rank", null);
                    }
                }
                //sort mảng sort theo avg để ra rank từng học sinh
                for (int i = 0; i < sort.size() - 1; i++){
                    for (int j = 0 ; j < sort.size() - i - 1; j++){
                        if(Double.parseDouble(sort.get(j).get("avg").toString()) < Double.parseDouble(sort.get(j + 1).get("avg").toString())){
                            Map<String, Object> temp = sort.get(j);
                            sort.set(j, sort.get(j + 1));
                            sort.set(j + 1, temp);
                        }
                    }
                }

                //mảng chứa những phần tử index ở rank( cả trường hợp = điểm nhau )
                int[] arrRankOfSortArray = new int[sort.size()];
                int rank = 1;
                for (int i = 0; i < sort.size() - 1; i++){
                    if(Double.parseDouble(sort.get(i).get("avg").toString()) ==  Double.parseDouble(sort.get(i + 1).get("avg").toString())){
                        if(i > 0){
                            if(Double.parseDouble(sort.get(i).get("avg").toString()) !=  Double.parseDouble(sort.get(i - 1).get("avg").toString())){
                                rank++;
                            }
                        }
                        arrRankOfSortArray[i] = rank;
                    }else{
                        if(i > 0){
                            if(Double.parseDouble(sort.get(i).get("avg").toString()) !=  Double.parseDouble(sort.get(i - 1).get("avg").toString())){
                                rank++;
                            }
                        }
                        arrRankOfSortArray[i] = rank;
                    }
                }
                //xử lý rank của phần tử cuối mảng
                if(sort.size() >= 2){
                    if(Double.parseDouble(sort.get(sort.size() - 2).get("avg").toString()) ==  Double.parseDouble(sort.get(sort.size() - 1).get("avg").toString())){
                        arrRankOfSortArray[sort.size() - 1] = arrRankOfSortArray[sort.size() - 2];
                    }else{
                        arrRankOfSortArray[sort.size() - 1] = arrRankOfSortArray[sort.size() - 2] + 1;
                    }
                }else if(sort.size() == 1){
                    arrRankOfSortArray[0] = 1;
                }

                //gán rank cho từng học sinh
                for (int i = 0; i < sort.size(); i++){
                    for (int j = 0; j < newList.size(); j++){
                        if(sort.get(i).get("studentCode").toString() == newList.get(j).get("studentCode").toString()){
                            newList.get(j).put("rank", arrRankOfSortArray[i]);
                        }
                    }
                }

                outputDesserts.put("desserts", newList);

                for (int i = 0; i < newList.size(); i++){
                    Iterator<String> ite = set.iterator();
                    while(ite.hasNext()){
                        String key = ite.next();
                        if(!newList.get(i).containsKey(key)){
                            newList.get(i).put(key, null);
                        }
                    }
                }

                output.add(outputDesserts);
                Map<String, Object> outputSubjects = new HashMap<>();
                outputSubjects.put("subjects", set);
                output.add(outputSubjects);

                serviceResult.setData(output);
            }else{
                serviceResult.setData(list);
            }
            if(serviceResult.getData().size() == 0) {
                serviceResult.setMessage("Lớp này chưa khai báo môn học cho lớp");
                serviceResult.setStatus(HttpStatus.BAD_REQUEST);
            }else{
                serviceResult.setMessage("success");
            }
        }catch (Exception e){
            serviceResult.setMessage("Lấy thông tin thất bại");
            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<Boolean> checkEnableUpdate(Integer semester) {
        Boolean check = false;
        LocalDateTime now = LocalDateTime.now();
        List<SchoolYear> list = schoolYearRespository.getAllSchoolYearNoDistinct().get();
        for(int i = 0; i < list.size(); i++){
            Optional<SchoolYear> schoolYear = schoolYearRespository.getSemesterOf(now);
            if(!schoolYear.equals(Optional.empty())){
                if((semester == 1 || semester == 2) && semester == Integer.parseInt(schoolYear.get().getSemester())){
                    check = true;
                    break;
                }
                if(semester == 0 && Integer.parseInt(schoolYear.get().getSemester()) == 2){
                    check = true;
                    break;
                }
            }
        }
        return new ServiceResult<>(HttpStatus.OK, "success", check);
    }
}
