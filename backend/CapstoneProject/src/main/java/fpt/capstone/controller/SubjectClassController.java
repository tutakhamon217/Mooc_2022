package fpt.capstone.controller;

import fpt.capstone.form_data.SubjectClassUpdate;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.service.SubjectClassService;
import fpt.capstone.service.SubjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class SubjectClassController {
    @Autowired
    private SubjectsService subjectsService;

    @Autowired
    private SubjectClassService subjectClassService;


    @GetMapping(value = "/subjectClass/class", produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<ServiceResult<Map<String, Object>>> getByDeptIdAndGradeLevelAndYears(@RequestParam(required = false) Integer deptId, @RequestParam(required = false) Integer gradeLevel,
                                                                                               @RequestParam String years) throws ParseException {
        ServiceResult<Map<String, Object>> subjects = subjectsService.getAllSubjectByDeptIdAndGradeLevelAndYears(deptId, gradeLevel, years);
        return ResponseEntity.ok(subjects);
    }

    @GetMapping(value = "/subjectClass/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResult<Map<String, Object>>> getSubjectClassPagingAndSearching(@RequestParam(required = false) Integer classId,
                                                                                                @RequestParam(required = false,defaultValue = "") String nameCode,
                                                                                                @RequestParam(required = false) Integer gradeLevel,
                                                                                                @RequestParam(required = false) Integer deptId) throws ParseException {
        ServiceResult<Map<String, Object>> subjects = subjectClassService.getAllSubjectClassPagingAndSearching(classId, nameCode,gradeLevel,deptId);
        return ResponseEntity.ok(subjects);
    }

    @PostMapping(value = "/subjectClass/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResult<Map<String, Object>>> saveSubjectClass(@RequestBody List<SubjectClassUpdate> subjectClasses) throws ParseException {
        ServiceResult<Map<String, Object>> results = subjectClassService.saveSubjectClass(subjectClasses);
        return ResponseEntity.ok(results);
    }

    @GetMapping(value = "/subjectClass/danh-gia-hoc-luc/semester", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResult<Map<String, Object>>> getSubjectClassClassIdSubjectIdSemester(@RequestParam(required = false) Integer classId, @RequestParam(required = false) Integer semester) throws ParseException {
        ServiceResult<Map<String, Object>> results = subjectClassService.getSubjectClassByClassIdSubjectIdSemester(classId, semester);
        return ResponseEntity.ok(results);
    }

}
