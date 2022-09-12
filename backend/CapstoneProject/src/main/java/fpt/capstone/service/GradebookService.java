package fpt.capstone.service;

import fpt.capstone.entities.GradebookSubjectsDetails;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.ConfigGradeDetailIdListForm;
import fpt.capstone.form_data.ConfigScoreDetailIdListForm;
import fpt.capstone.form_data.GradeBookForm;
import fpt.capstone.form_data.StudentRateForm;
import fpt.capstone.vo.GradebookSubjectsDetailsVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface GradebookService {
    ServiceResult<Map<String, Object>> findScoreOfClassInSemester(String classCode, String year, Integer semester);
    ServiceResult<Map<String, Object>> findScoreOfClassInSemesterMonXepLoai(String classCode, String year, Integer semester);
    ServiceResult<Map<String, Object>> getStudentRate(String classCode, String year, Integer semester);
    ServiceResult<Boolean> updateStudentRate(StudentRateForm form);
    ServiceResult<Map<String, Object>> onSearch(GradeBookForm gradeBookForm);

    ServiceResult<Map<String, Object>> getListClassroom(GradeBookForm gradeBookForm);

    ServiceResult<Map<String, Object>> getListSubject(GradeBookForm gradeBookForm);

    ServiceResult<Boolean> checkGradeBookToChangeClass(String year, String studentCode);

    ServiceResult<GradebookSubjectsDetails> evaluate(GradebookSubjectsDetailsVO gradebookSubjectsDetailsVO, String login);

    ServiceResult<Boolean> save(GradeBookForm gradeBookForm, String login);
    ByteArrayInputStream exportExcelSampleScore(String year, String subjectCode, String classCode, Integer semester, ConfigScoreDetailIdListForm configScoreDetailIdListForm);
    ServiceResult<Boolean> importExcelScore(MultipartFile file,String subjectCode,String classCode,String schoolYear,String semester,String login) throws IOException;
    ServiceResult<List<Map<String, Object>>> getListScoreOfEachSubject(String year, Integer semester, String classCode, String studentCode);
    ByteArrayInputStream exportExcelSampleGrade(String year, String subjectCode, String classCode, Integer semester, ConfigGradeDetailIdListForm configGradeDetailIdListForm);
    ServiceResult<Boolean> importExcelGrade(MultipartFile file,String subjectCode,String classCode,String schoolYear,String semester,String login)throws IOException;
}
