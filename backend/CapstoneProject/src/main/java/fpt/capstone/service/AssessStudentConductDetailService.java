package fpt.capstone.service;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.AssessStudentConductDetailForm;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface AssessStudentConductDetailService {
    ServiceResult<List<Map<String,Object>>> getInfoConductOfClass(Integer semester, String year, Integer teacherId, String classCode);
    ServiceResult<List<Map<String,Object>>> getEvaluateOfTeacher(Integer semester, String year, String studentCode, String classCode);
    ServiceResult<Boolean> updateAssessStudentConductDetail(List<AssessStudentConductDetailForm> list, String classCode, Integer semester, String schoolYear);
    ServiceResult<List<Map<String, Object>>> getReportTheResultOfCompetition(String year,
                                                                             Integer deptId,
                                                                             Integer gradeLevel,
                                                                             String classCode,
                                                                             Integer semester);
    ServiceResult<List<Map<String, Object>>> getReportTheResultOfEachClass(String year,
                                                                           String classCode,
                                                                           Integer semester);
    ServiceResult<Boolean> checkEnableUpdate(Integer semester);
}
