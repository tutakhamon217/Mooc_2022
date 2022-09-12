package fpt.capstone.service;

import fpt.capstone.form_data.SubjectClassUpdate;
import fpt.capstone.entities.ServiceResult;

import java.util.List;
import java.util.Map;

public interface SubjectClassService {
    ServiceResult<Map<String, Object>> getAllSubjectClassPagingAndSearching(Integer classId, String nameCode,Integer gradeLevel,Integer deptId);

    ServiceResult<Map<String, Object>> saveSubjectClass(List<SubjectClassUpdate> subjectClasses);

    ServiceResult<Map<String, Object>> getSubjectClassByClassIdSubjectIdSemester(Integer classId, Integer semester);
}
