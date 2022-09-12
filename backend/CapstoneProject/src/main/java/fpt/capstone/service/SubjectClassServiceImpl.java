package fpt.capstone.service;

import fpt.capstone.form_data.SubjectClassUpdate;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.entities.SubjectClass;
import fpt.capstone.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SubjectClassServiceImpl implements SubjectClassService {
    @Autowired
    private SubjectsRepository subjectsRepository;

    @Autowired
    private SubjectClassRepository subjectClassRepository;

    @Autowired

    private ClassRoomRepository classRoomRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public ServiceResult<Map<String, Object>> getAllSubjectClassPagingAndSearching(Integer classId, String nameCode,Integer gradeLevel,Integer deptId) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        try {
            List<Map<String, Object>> subjectClass = subjectClassRepository.getAllSubjectClassPagingAndSearching(classId, nameCode,gradeLevel,deptId);
            System.out.println(subjectClass.size());
            Map<String, Object> output = new HashMap<>();
            output.put("subjectClass", subjectClass);
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

    @Override
    public ServiceResult<Map<String, Object>> saveSubjectClass(List<SubjectClassUpdate> subjectClasses) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        try {
            for (SubjectClassUpdate s : subjectClasses) {
               if (s.getSubjectClassId()!=null){
                   SubjectClass sc = subjectClassRepository.getById(s.getSubjectClassId());
                   sc.setCoefficient(s.getCoefficient());
                   sc.setFlgSemester1(s.getFlgSemester1());
                   sc.setFlgSemester2(s.getFlgSemester2());
                   sc.setUpdateTime(LocalDateTime.now());
                   sc.setUpdateName(username);
                   subjectClassRepository.save(sc);
               }
               else {
                   SubjectClass sc =new SubjectClass(s.getClassId(),s.getSubjectId(),s.getCoefficient(),s.getFlgSemester1(),s.getFlgSemester2(),LocalDateTime.now(),username,null,null);
                   subjectClassRepository.save(sc);
               }
            }
            Map<String, Object> output = new HashMap<>();
            output.put("saveResult", true);
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(output);
        } catch (Exception e) {
            serviceResult.setMessage("fail");
            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<Map<String, Object>> getSubjectClassByClassIdSubjectIdSemester(Integer classId, Integer semester) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        Map<String, Object> output = new HashMap<>();
        List<Map<String, Object>> subjectClassResult = null;
        if (semester == 0) {
            subjectClassResult = subjectClassRepository.findByClassIdAndSubjectIdSemester(classId, 1, 0);
            subjectClassResult.addAll(subjectClassRepository.findByClassIdAndSubjectIdSemester(classId, 1, 1));
            subjectClassResult.addAll(subjectClassRepository.findByClassIdAndSubjectIdSemester(classId, 0, 1));
        } else if (semester == 1) {
            subjectClassResult= subjectClassRepository.findByClassIdAndSubjectIdSemester(classId, 1, 0);
            subjectClassResult.addAll(subjectClassRepository.findByClassIdAndSubjectIdSemester(classId, 1, 1));
        } else {
            subjectClassResult= subjectClassRepository.findByClassIdAndSubjectIdSemester(classId, 0, 1);
            subjectClassResult.addAll(subjectClassRepository.findByClassIdAndSubjectIdSemester(classId, 1, 1));
        }
        if (subjectClassResult.size() == 0) {
            serviceResult.setMessage("Dữ liệu rỗng");
            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
        } else {
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
        }
        output.put("subjectClass", subjectClassResult);
        serviceResult.setData(output);
        return serviceResult;
    }
}
