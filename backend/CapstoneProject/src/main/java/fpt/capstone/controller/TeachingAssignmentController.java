package fpt.capstone.controller;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.TeachingAssignmentUpdate;
import fpt.capstone.payroll.TeachingAssignmentImportException;
import fpt.capstone.service.TeachingAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class TeachingAssignmentController {
    @Autowired
    private TeachingAssignmentService teachingAssignmentService;

    @GetMapping(value = "/teachingAssignment/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResult<Map<String, Object>>> getSubjectClassPagingAndSearching(@RequestParam(required = true) String nameCodeTeacher,
                                                                                                @RequestParam(required = true) String nameCodeSubject,
                                                                                                @RequestParam(required = true) Integer classId) {
        ServiceResult<Map<String, Object>> teachingAssignment = teachingAssignmentService.getAllTeachingAssignmentSearching(nameCodeTeacher, nameCodeSubject, classId);
        return ResponseEntity.ok(teachingAssignment);
    }

    @DeleteMapping(value = "/teachingAssignment/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceResult<Map<String, Object>>> deleteTeachingAssignment(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(teachingAssignmentService.delete(id));
    }

    @PutMapping(value = "/teachingAssignment/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResult<Map<String, Object>>> updateTeachingAssignment(@RequestBody TeachingAssignmentUpdate teachingAssignmentUpdate) {

        return ResponseEntity.ok(teachingAssignmentService.update(teachingAssignmentUpdate));
    }

    @PostMapping(value = "/teachingAssignment/upload")
    public ServiceResult<Boolean> ImportExcel(@RequestParam("file") MultipartFile file, @RequestParam(required = true) String years) throws Exception {
        try {
            return teachingAssignmentService.importExcel(file, years);
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }

    }

    @GetMapping(value = "/teachingAssignment/download")
    public ResponseEntity<Resource> ExportExcel(@RequestParam() String year) {
        InputStreamResource file = new InputStreamResource(teachingAssignmentService.exportExcel(year));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=teachingAssignment.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @GetMapping(value = "/teachingAssignment/class_subject")
    public ResponseEntity<ServiceResult<Map<String, Object>>> getClassCodeSubjectCodeByYearAndSemesterAndTeacherCode(@RequestParam(required = true) String teacherCode,
                                                                                                                     @RequestParam(required = true) String year,
                                                                                                                     @RequestParam(required = true) Integer semester) throws ParseException {
        ServiceResult<Map<String, Object>> classCodeSubjectCodes = teachingAssignmentService.getClassSubjectByYearAndSemesterAndTeacherCode(teacherCode, year, semester);
        return ResponseEntity.ok(classCodeSubjectCodes);
    }

}
