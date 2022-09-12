package fpt.capstone.controller;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.entities.Student;
import fpt.capstone.form_data.StudentForm;
import fpt.capstone.form_data.*;
import fpt.capstone.service.StudentService;
import fpt.capstone.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayInputStream;

import java.util.List;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;

@RestController
@CrossOrigin(origins = "*")
public class StudentController {
    @Autowired
    private StudentService service;

    @GetMapping(value = "/student/search", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<Page<StudentVo>> getAllStudentByNameOrCodeInClass(@RequestParam("status") String status, @RequestParam("class_code") String class_code, @RequestParam("search_word") String search_word, @RequestParam("grade_level") String grade_level, @RequestParam("years") String years, @RequestParam() int pageIndex, @RequestParam() int pageSize)
    {
        return service.getAllStudentByNameOrCodeInClass(status, class_code, search_word, grade_level, years, PageRequest.of(pageIndex, pageSize));
    }

    @PostMapping(value = "/student/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<Boolean> insert(@RequestBody StudentForm form) {
        return service.insert(form);
    }

    @PostMapping(value = "/student/update", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<Boolean> update(@RequestBody StudentForm form)
    {
        return service.update(form);
    }

    @GetMapping(value = "/student/student-of-class", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<Map<String, Object>>> getStudentOfClass(@RequestParam() String year,
                                                   @RequestParam() String class_code,
                                                   @RequestParam() Integer semester,
                                                   @RequestParam() String subjectCode)
    {
        return service.getStudentOfClass(year, class_code, semester, subjectCode);
    }

    @GetMapping(value = "/student/student-danh-gia-hoc-luc", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<Student>> getStudentOfClassScreenDanhGiaHocLuc(@RequestParam() String year,
                                                   @RequestParam() String class_code)
    {
        return service.getStudentOfClassScreenDanhGiaHocLuc(year, class_code);
    }


    @PostMapping(value = "/student/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<StudentDetailInformationVo>> insert(@RequestBody idForm form)
    {
        return service.getStudentDetailInformation(form.getId(), form.getYear());
    }

    
    @GetMapping(value = "/student/class", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<List<Student>> getStudentInClass(@RequestParam String classCode, @RequestParam String year)
    {
        return service.getStudentManagedByClass(classCode, year);
    }

    @GetMapping(value = "/student/history-year")
    public ServiceResult<List<Map<String, Object>>> getAllYearHistoryOfStudent(@RequestParam String studentCode){
        return service.getAllYearHistoryOfStudent(studentCode);
    }

    @GetMapping(value = "/student/get-result-study-ever")
    public ServiceResult<List<Map<String, Object>>> getResultStudyEver(@RequestParam String classCode,
                                                                       @RequestParam String year,
                                                                       @RequestParam String studentCode,
                                                                       @RequestParam Integer classId){
        return service.getResultStudyEver(classCode,year,studentCode,classId);
    }

    @GetMapping(value = "/student/detail-side-parent")
    ServiceResult<List<StudentDetailInformationVo>> getInfor(@RequestParam String studentCode,
                                                             @RequestParam String year)
    {
        return service.getStudentDetailInformationSideParent(studentCode, year);
    }

    @GetMapping(value = "/student/download")
    public ResponseEntity<Resource> ExportExcel() {
        ByteArrayInputStream tmp = service.exportTemplateExcel();
        if (tmp != null) {
            InputStreamResource file = new InputStreamResource(tmp);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=DS_HocSinh.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(file);
        } else {
            return ResponseEntity.badRequest().body(new InputStreamResource(new ByteArrayInputStream(null)));
        }
    }

    @PostMapping(value = "/student/upload")
    public ServiceResult<List<String>> ImportExcel(@RequestParam("file") MultipartFile file, @RequestParam(required = true) String year) throws IOException {
        return service.importExcel(file, year);
    }
}
