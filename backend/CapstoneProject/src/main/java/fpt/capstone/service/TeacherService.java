package fpt.capstone.service;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.entities.Teacher;
import fpt.capstone.form_data.TeacherForm;
import fpt.capstone.vo.DropDownVo;

import java.util.List;
import java.util.Map;

public interface TeacherService {
    ServiceResult<List<DropDownVo>> getDropDownValues(Integer depId);
    ServiceResult<List<DropDownVo>> getAllTeacherOfRoot(Integer depId);
    ServiceResult<Map<String, Object>>getAllteacher();
    ServiceResult<Map<String, Object>>getAllteacherGVCN();
    ServiceResult<Map<String, Object>>getAllTeacherByCodeOrFullNameAndAuthority(String codeName,String authorityCode,Integer deptId);
    ServiceResult<Map<String, Object>>getTeacherDetailById(Integer teacherId);
    List<Teacher> getTeacherByCode(String teacherCode);
    ServiceResult<Boolean> insert (TeacherForm form);
    ServiceResult<Boolean> update (TeacherForm form,Integer id);
    ServiceResult<Map<String, Object>>getAllTeacherForTeachingAssignment(Integer subjectId);
}
