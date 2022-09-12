package fpt.capstone.service;

import fpt.capstone.form_data.SubjectForm;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.entities.SubjectDept;
import fpt.capstone.entities.Subjects;
import fpt.capstone.payroll.ValidationException;
import fpt.capstone.repository.*;
import fpt.capstone.vo.DropDownVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubjectsServiceImpl implements SubjectsService {
    @Autowired
    private SubjectsRepository subjectsRepository;
    @Autowired
    private SubjectDeptRepository subjectDeptRepository;
    @Autowired
    private ClassRoomRepository classRoomRepository;
    @Autowired
    private GradeLevelRepositpry gradeLevelRepositpry;
    @Autowired
    private DepartmentRepository departmentRepository;


    @Override
    public ServiceResult<Map<String, Object>> getAllSubjectsPagingAndSearching(Integer deptId, String code, String name, Integer gradeLevel, Integer pageIndex, Integer pageSize) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        List<Map<String, Object>> subjects;
        Map<String, Object> output = new HashMap<>();
        try {
            if (pageIndex == 0 & pageSize == 0) {
                subjects = subjectsRepository.getAllSubjectsSearching(deptId, code, name, gradeLevel);
                List<Map<String, Object>> collect = subjects.stream().map(x -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", x.get("id"));
                    data.put("code", x.get("code"));
                    data.put("name", x.get("name"));
                    data.put("gradeName", x.get("gradeName"));
                    data.put("type", x.get("type"));
                    data.put("sub_type", x.get("sub_type"));
                    data.put("description", x.get("description"));
                    data.put("isConfigGrade", x.get("isConfigGrade"));
                    data.put("isConfigClass", x.get("isConfigClass"));
                    data.put("Departments", subjectDeptRepository.getSubjectDeptBySubjectQueryNative(Integer.parseInt(x.get("id").toString())));
                    return data;

                }).collect(Collectors.toList());
                output.put("subjects", collect);
                serviceResult.setMessage("success");
                serviceResult.setStatus(HttpStatus.OK);
                serviceResult.setData(output);

            }
            else {
                System.out.println("paging");
                Pageable pageable = PageRequest.of(pageIndex,pageSize);
                subjects = subjectsRepository.getAllSubjectsPagingAndSearching(deptId, code, name, gradeLevel,pageable);
                List<Map<String, Object>> collect = subjects.stream().map(x -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", x.get("id"));
                    data.put("code", x.get("code"));
                    data.put("name", x.get("name"));
                    data.put("gradeName", x.get("gradeName"));
                    data.put("type", x.get("type"));
                    data.put("sub_type", x.get("sub_type"));
                    data.put("description", x.get("description"));
                    data.put("isConfigClass", x.get("isConfigClass"));
                    data.put("isConfigGrade", x.get("isConfigGrade"));
                    data.put("Departments", subjectDeptRepository.getSubjectDeptBySubjectQueryNative(Integer.parseInt(x.get("id").toString())));
                    return data;

                }).collect(Collectors.toList());
                output.put("pageSize", pageSize);
                output.put("pageIndex", pageIndex);
                output.put("subjects", collect);
                serviceResult.setMessage("success");
                serviceResult.setStatus(HttpStatus.OK);
                serviceResult.setData(output);
            }
        } catch (Exception e) {
            serviceResult.setMessage("fail");
            serviceResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<Map<String, Object>> loadScreen(Pageable pageable) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        try {
            List<Map<String, Object>> subjects = subjectsRepository.getAllSubjectsPaging(pageable);
            List<Map<String, Object>> collect = subjects.stream().map(x -> {
                Map<String, Object> data = new HashMap<>();
                data.put("id", x.get("id"));
                data.put("code", x.get("code"));
                data.put("name", x.get("name"));
                data.put("gradeName", x.get("gradeName"));
                data.put("type", x.get("type"));
                data.put("sub_type", x.get("sub_type"));
                data.put("description", x.get("description"));
                data.put("canUpdate", x.get("canUpdate"));
                data.put("Departments", subjectDeptRepository.getSubjectDeptsBySubjectId(Integer.parseInt(x.get("id").toString())));
                return data;

            }).collect(Collectors.toList());;
            Map<String, Object> output = new HashMap<>();
            output.put("pageSize", pageable.getPageSize());
            output.put("pageIndex", pageable.getPageNumber());
            output.put("subjects", collect);
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
    public Integer getTotalSubject() {
        return subjectsRepository.getTotalSubject();
    }

    @Override
    public ServiceResult<Map<String, Object>> getAllSubjectsPaging(Pageable pageable) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        try {
            List<Map<String, Object>> subjects = subjectsRepository.getAllSubjectsPaging(pageable);
            List<Map<String, Object>> collect = subjects.stream().map(x -> {
                Map<String, Object> data = new HashMap<>();
                data.put("id", x.get("id"));
                data.put("code", x.get("code"));
                data.put("name", x.get("name"));
                data.put("gradeName", x.get("gradeName"));
                data.put("type", x.get("type"));
                data.put("sub_type", x.get("sub_type"));
                data.put("description", x.get("description"));
                data.put("canUpdate", x.get("canUpdate"));
                data.put("Departments", subjectDeptRepository.getSubjectDeptsBySubjectId(Integer.parseInt(x.get("id").toString())));
                return data;

            }).collect(Collectors.toList());;
            Map<String, Object> output = new HashMap<>();
            output.put("subjects", collect);
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
    public ServiceResult<List<DropDownVo>> getDropDownByDeptIDAndGradeId(Integer deptID, Integer gradeId) {
        return new ServiceResult<>(HttpStatus.OK, "success", subjectsRepository.getSubjectsByDeptIDAndGradeLevel(deptID, gradeId));
    }

    @Override
    public ServiceResult<Map<String, Object>> getSubjectByDeptAndType(Integer grade_level, Integer type, String year, Integer grade_id, Integer semester) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        Map<String, Object> output = new HashMap<>();
        try {
            output.put("data", subjectsRepository.getSubjectByDeptAndType(grade_level, type, year, grade_id, semester));
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(output);
        } catch (Exception ex) {
            serviceResult.setMessage(ex.getMessage());
            serviceResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<Map<String, Object>> getConfigScoreSubject(String parent_code) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        Map<String, Object> output = new HashMap<>();
        try {
            output.put("data", subjectsRepository.getConfigScoreSubject(parent_code));
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(output);
        } catch (Exception ex) {
            serviceResult.setMessage("error");
            serviceResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<Map<String, Object>> getConfigGradingDetails(String parent_code) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        Map<String, Object> output = new HashMap<>();
        try {
            output.put("data", subjectsRepository.getConfigGradingDetails(parent_code));
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(output);
        } catch (Exception ex) {
            serviceResult.setMessage(ex.getMessage());
            serviceResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<Map<String, Object>> getSubjectsNotYetConfigScoreboard(Integer grade_level, Integer sup_type, String year, Integer grade_id, Integer semester) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        Map<String, Object> output = new HashMap<>();
        try {
            output.put("data", subjectsRepository.getSubjectsNotYetConfigScoreboard(grade_level, sup_type, year, grade_id, semester));
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(output);
        } catch (Exception ex) {
            serviceResult.setMessage(ex.getMessage());
            serviceResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<Map<String, Object>> updateSubject(SubjectForm s, int id) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        try {
            Subjects subject = subjectsRepository.getById(id);
            if (subject == null) {
                serviceResult.setMessage("Môn học không tồn tại");
                serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                serviceResult.setData(null);
                return serviceResult;
            }
            if (!gradeLevelRepositpry.isExist(s.getGradeLevel())) {
                serviceResult.setMessage("Khối học không tồn tại");
                serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                serviceResult.setData(null);
                return serviceResult;
            }
            for (Integer deptId: s.getDeptId()) {
                if (!departmentRepository.isExistForClassRoom(deptId)) {
                    serviceResult.setMessage("Khoa ban với id : "+deptId+ " tồn tại");
                    serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                    serviceResult.setData(null);
                    return serviceResult;
                }
            }
            Subjects updateSubject = subjectsRepository.save(new Subjects(subject.getId(), subject.getCreatedTime(), "", LocalDateTime.now(),
                    "", s.getName(), s.getCode(), s.getGradeLevel(), s.getType(), s.getSubType(), s.getDescription()));
            subjectDeptRepository.deleteAll(subjectDeptRepository.getSubjectDeptsBySubjectId(id));
            for (Integer deptId: s.getDeptId()) {
                subjectDeptRepository.save(new SubjectDept(id,deptId));
            }
            Map<String, Object> output = new HashMap<>();
            output.put("saveResult", true);
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
    public ServiceResult<Map<String, Object>> addSubject(SubjectForm s) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        if (subjectsRepository.findByCode(s.getCode()) != null) {
            serviceResult.setMessage("Mã môn học đã tồn tại");
            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
            serviceResult.setData(null);
            return serviceResult;
        }
        if (!gradeLevelRepositpry.isExist(s.getGradeLevel())) {
            serviceResult.setMessage("Khối học không tồn tại");
            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
            serviceResult.setData(null);
            return serviceResult;
        }
        for (Integer deptId: s.getDeptId()) {
            if (!departmentRepository.isExistForClassRoom(deptId)) {
                serviceResult.setMessage("Khoa ban với id : "+deptId+ " tồn tại");
                serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                serviceResult.setData(null);
                return serviceResult;
            }
        }
        try {
            Subjects addSubject = subjectsRepository.save(new Subjects(LocalDateTime.now(), username, null,
                    "", s.getName(), s.getCode(), s.getGradeLevel(), s.getType(), s.getSubType(), s.getDescription()));
            for (Integer deptId: s.getDeptId()) {
                SubjectDept sd = new SubjectDept(addSubject.getId(), deptId);
                subjectDeptRepository.save(sd);
            }
            Map<String, Object> output = new HashMap<>();
            output.put("saveResult", true);
            output.put("addSubject", addSubject);
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
    public ServiceResult<Map<String, Object>> deleteSubject(int id) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        try {
            Subjects subject = subjectsRepository.getById(id);
            if (subject == null) {
                serviceResult.setMessage("Môn học không tồn tại");
                serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                serviceResult.setData(null);
                return serviceResult;
            }
            subjectsRepository.deleteById(id);
            List<SubjectDept> sd = subjectDeptRepository.getSubjectDeptsBySubjectId(id);
            subjectDeptRepository.deleteAll(sd);
            Map<String, Object> output = new HashMap<>();
            output.put("deleteResult", true);
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
    public ServiceResult<Map<String, Object>> getAllSubjectByDeptIdAndGradeLevelAndYears(Integer deptId, Integer gradeLevel, String years) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();

        try {
            List<Map<String, Object>> subjects = classRoomRepository.getAllClassByDeptIdAndGradeLevelAndYears(deptId, gradeLevel, years);
            Map<String, Object> output = new HashMap<>();
            output.put("subjects", subjects);
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
    public ServiceResult<Map<String, Object>> getSubjectInClass(String class_code, Integer semester) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        try {
            List<Map<String, Object>> subjects;
            if (semester == 1) {
                subjects= subjectsRepository.listAllSubjectOfClassSemester1(class_code);;
            } else {
                subjects= subjectsRepository.listAllSubjectOfClassSemester2(class_code);;
            }
            Map<String, Object> output = new HashMap<>();
            output.put("subjects", subjects);
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(output);
        } catch (Exception e) {
            serviceResult.setMessage("fail");
            serviceResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return serviceResult;
        // TODO Auto-generated method stub
    }

}
