package fpt.capstone.controller;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.ConfigGradeDetailIdListForm;
import fpt.capstone.form_data.ConfigScoreDetailIdListForm;
import fpt.capstone.form_data.GradeBookForm;
import fpt.capstone.form_data.StudentRateForm;
import fpt.capstone.service.GradebookService;
import fpt.capstone.vo.GradebookSubjectsDetailsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class GradeBookController {
    @Autowired
    private GradebookService gradebookService;

    @PostMapping("/gradebooks/search")
    public ResponseEntity<?> onSearch(@RequestBody GradeBookForm gradeBookForm) {
        ServiceResult<Map<String, Object>> serviceResult = gradebookService.onSearch(gradeBookForm);
        return ResponseEntity.ok()
                .body(serviceResult);
    }

    
    @PostMapping("/gradebooks/classroom-list")
    public ResponseEntity<?> getListClassroom(@RequestBody GradeBookForm gradeBookForm) {
        ServiceResult<Map<String, Object>> result = gradebookService.getListClassroom(gradeBookForm);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/gradebooks/subject-list")
    public ResponseEntity<?> getListSubject(@RequestBody GradeBookForm gradeBookForm) {
        ServiceResult<Map<String, Object>> result = gradebookService.getListSubject(gradeBookForm);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/gradebooks/evaluate")
    public ResponseEntity<?> evaluate(@RequestBody GradebookSubjectsDetailsVO gradebookSubjectsDetailsVO, @RequestParam(required = true) String login) {
        ServiceResult<?> result = gradebookService.evaluate(gradebookSubjectsDetailsVO, login);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/gradebooks/save")
    public ResponseEntity<ServiceResult<Boolean>> saveGradebook(@RequestBody GradeBookForm gradeBookForm, @RequestParam(required = true) String login) {
        try {
            ServiceResult<Boolean> result = gradebookService.save(gradeBookForm, login);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(value = "/gradebooks/score/download")
    public ResponseEntity<Resource> ExportExcelScore(@RequestParam(required = true) String year, @RequestParam(required = true) String subjectCode,
                                                     @RequestParam(required = true) String classCode, @RequestParam(required = true) Integer semester,
                                                     @RequestBody ConfigScoreDetailIdListForm configScoreDetailIdListForm) {
        InputStreamResource file = new InputStreamResource(gradebookService.exportExcelSampleScore(year, subjectCode, classCode, semester, configScoreDetailIdListForm));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=GradeBookScore.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @PostMapping(value = "/gradebooks/score/upload")
    public ServiceResult<Boolean> ImportExcelScore(@RequestParam("file") MultipartFile file, @RequestParam(required = true) String subjectCode, @RequestParam(required = true) String classCode,
                                                   @RequestParam(required = true) String schoolYear, @RequestParam(required = true) String semester, @RequestParam(required = true) String login) throws IOException {
        return gradebookService.importExcelScore(file, subjectCode, classCode, schoolYear, semester, login);
    }

    @PostMapping(value = "/gradebooks/grade/download")
    public ResponseEntity<Resource> ExportExcelGrade(@RequestParam(required = true) String year, @RequestParam(required = true) String subjectCode,
                                                     @RequestParam(required = true) String classCode, @RequestParam(required = true) Integer semester,
                                                     @RequestBody ConfigGradeDetailIdListForm configGradeDetailIdListForm) {
        InputStreamResource file = new InputStreamResource(gradebookService.exportExcelSampleGrade(year, subjectCode, classCode, semester,configGradeDetailIdListForm));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=GradeBookGrade.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    @PostMapping(value = "/gradebooks/grade/upload")
    public ServiceResult<Boolean> ImportExcelGrade(@RequestParam("file") MultipartFile file, @RequestParam(required = true) String subjectCode, @RequestParam(required = true) String classCode,
                                                   @RequestParam(required = true) String schoolYear, @RequestParam(required = true) String semester, @RequestParam(required = true) String login) throws IOException {
        if (file==null){
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Vui lòng nhập file", null);
        }
        return gradebookService.importExcelGrade(file, subjectCode, classCode, schoolYear, semester, login);
    }


//  man danh gia hoc luc
    @GetMapping(value = "/gradebook/avg-score", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResult<Map<String, Object>>> findScoreOfClassInSemester(@RequestParam(required = false) String classCode, @RequestParam(required = false)  String year, @RequestParam(required = false)  Integer semester) throws IOException {
        ServiceResult<Map<String, Object>> results = gradebookService.findScoreOfClassInSemester(classCode, year, semester);
        return ResponseEntity.ok(results);
    }

    @GetMapping(value = "/gradebook/xep-loai", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResult<Map<String, Object>>> findScoreOfClassInSemesterMonXepLoai(@RequestParam(required = false) String classCode, @RequestParam(required = false)  String year, @RequestParam(required = false)  Integer semester) throws IOException {
        ServiceResult<Map<String, Object>> results = gradebookService.findScoreOfClassInSemesterMonXepLoai(classCode, year, semester);
        return ResponseEntity.ok(results);
    }

    @GetMapping(value = "/gradebook/student-rate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResult<Map<String, Object>>> getStudentRate(@RequestParam(required = false) String classCode, @RequestParam(required = false)  String year, @RequestParam(required = false)  Integer semester) throws IOException {
        ServiceResult<Map<String, Object>> results = gradebookService.getStudentRate(classCode, year, semester);
        return ResponseEntity.ok(results);
    }

    @PostMapping(value = "/gradebook/update-student-rate")
    public ServiceResult<Boolean> updateStudentRate(@RequestBody StudentRateForm form) throws IOException {
        ServiceResult<Boolean> results = gradebookService.updateStudentRate(form);
        return results;
    }
    
    @GetMapping(value = "/gradebooks/subject-score-of-student")
    public ServiceResult<List<Map<String, Object>>> getListScoreOfEachSubject(@RequestParam String year,
                                                                              @RequestParam Integer semester,
                                                                              @RequestParam String classCode,
                                                                              @RequestParam String studentCode){
        return gradebookService.getListScoreOfEachSubject(year, semester, classCode, studentCode);
    }

    @GetMapping(value = "/gradebooks/check-change-class")
    public ServiceResult<Boolean> checkGradeToChangeClass(@RequestParam String year,
                                                                              @RequestParam String studentCode){
        return gradebookService.checkGradeBookToChangeClass(year, studentCode);
    }
}
