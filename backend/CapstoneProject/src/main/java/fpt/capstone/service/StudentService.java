package fpt.capstone.service;


import fpt.capstone.entities.ServiceResult;
import fpt.capstone.entities.Student;
import fpt.capstone.vo.StudentVo;
import fpt.capstone.vo.*;
import fpt.capstone.form_data.StudentForm;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
public interface StudentService {

    ServiceResult<Page<StudentVo>> getAllStudentByNameOrCodeInClass(String status, String class_id, String search_word, String grade_level, String years, Pageable pageable);

    ServiceResult<Boolean> insert(StudentForm form);
    ServiceResult<Boolean> update(StudentForm form);
    ServiceResult<List<Map<String, Object>>> getStudentOfClass(String year, String classCode, Integer semester, String subjectCode);
    ServiceResult<List<Student>> getStudentOfClassScreenDanhGiaHocLuc(String year, String classCode);
    ServiceResult<List<StudentDetailInformationVo>> getStudentDetailInformation(String student_code, String year);
    ServiceResult<List<Student>> getStudentManagedByClass(String classCode, String year);
    ServiceResult<List<Map<String, Object>>> getAllYearHistoryOfStudent(String studentCode);
    ServiceResult<List<Map<String, Object>>> getResultStudyEver(String classCode, String year, String studentCode, Integer classId);
    ServiceResult<List<StudentDetailInformationVo>> getStudentDetailInformationSideParent(String studentCode, String year);
    ByteArrayInputStream exportTemplateExcel();
    ServiceResult<List<String>> importExcel(MultipartFile file, String year) throws IOException;
}
