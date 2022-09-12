package fpt.capstone.service;

import fpt.capstone.form_data.SubjectForm;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.vo.DropDownVo;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface SubjectsService {

    ServiceResult<Map<String, Object>> getAllSubjectsPagingAndSearching(Integer deptId, String code, String name, Integer gradeLevel, Integer pageIndex, Integer pageSize);

    ServiceResult<Map<String, Object>> loadScreen(Pageable pageable);

    Integer getTotalSubject();
    ServiceResult<Map<String, Object>>addSubject(SubjectForm subjectForm);
    ServiceResult<Map<String, Object>>updateSubject(SubjectForm subjectForm,int id);
    ServiceResult<Map<String, Object>>deleteSubject(int id);

    ServiceResult<Map<String, Object>> getAllSubjectsPaging(Pageable pageable);

    ServiceResult<Map<String, Object>> getAllSubjectByDeptIdAndGradeLevelAndYears(Integer deptId, Integer gradeLevel, String years);

    ServiceResult<List<DropDownVo>> getDropDownByDeptIDAndGradeId(Integer deptID, Integer gradeId);

    ServiceResult<Map<String, Object>> getSubjectByDeptAndType(Integer grade_level, Integer type, String year, Integer grade_id, Integer semester);

    ServiceResult<Map<String, Object>> getConfigScoreSubject(String parent_code);

    ServiceResult<Map<String, Object>> getConfigGradingDetails(String parent_code);

    ServiceResult<Map<String, Object>> getSubjectsNotYetConfigScoreboard(Integer grade_level, Integer sup_type, String year, Integer grade_id, Integer semester);

    ServiceResult<Map<String, Object>> getSubjectInClass(String class_code, Integer semester);
}
