package fpt.capstone.service;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.TeachingAssignmentUpdate;
import fpt.capstone.payroll.TeachingAssignmentImportException;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

public interface TeachingAssignmentService {
    ServiceResult<Map<String, Object>> getAllTeachingAssignmentSearching(String nameCodeTeacher, String nameCodeSubject, Integer classId);

    ServiceResult<Map<String, Object>> delete(Integer teachingAssignmentId);

    ServiceResult<Map<String, Object>> update(TeachingAssignmentUpdate teachingAssignmentUpdate);

    ServiceResult<Boolean> importExcel(MultipartFile file, String years) throws IOException, TeachingAssignmentImportException;

    ByteArrayInputStream exportExcel(String year);

    ServiceResult<Map<String, Object>> getClassSubjectByYearAndSemesterAndTeacherCode(String teacherCode, String year, Integer semester);

}
