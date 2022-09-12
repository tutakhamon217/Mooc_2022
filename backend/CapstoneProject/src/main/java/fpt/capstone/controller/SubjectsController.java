package fpt.capstone.controller;

import fpt.capstone.form_data.SubjectForm;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.entities.Student;
import fpt.capstone.service.SubjectsService;
import fpt.capstone.vo.DropDownVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class SubjectsController {
    @Autowired
    private SubjectsService subjectsService;

    @GetMapping(value = "/subjects/all/paging", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResult<Map<String, Object>>> getAllSubjectPaging(@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageIndex,
                                                                                  @RequestParam(required = false) Integer deptId, @RequestParam(required = false) String name,
                                                                                  @RequestParam(required = false) String code, @RequestParam(required = false) Integer gradeLevel) throws ParseException {
        ServiceResult<Map<String, Object>> subjects = subjectsService.getAllSubjectsPagingAndSearching(deptId, code, name, gradeLevel, pageIndex, pageSize);
        return ResponseEntity.ok(subjects);

    }

    @GetMapping(value = "/subjects/all/load", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResult<Map<String, Object>>> LoadScreen() throws ParseException {
        Pageable pageable = PageRequest.of(0, subjectsService.getTotalSubject());
        ServiceResult<Map<String, Object>> subjects = subjectsService.loadScreen(pageable);
        return ResponseEntity.ok(subjects);

    }
    @GetMapping(value = "/subjects/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResult<Map<String, Object>>> getAll() throws ParseException {
        Pageable pageable = Pageable.unpaged();
        ServiceResult<Map<String, Object>> subjects = subjectsService.getAllSubjectsPaging(pageable);
        return ResponseEntity.ok(subjects);

    }

    @PostMapping(value = "/subjects/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResult<Map<String, Object>>> addSubject(@Valid SubjectForm subjectForm, BindingResult bindingResult) throws ParseException {
        ServiceResult<Map<String, Object>> result = new ServiceResult<>();
        result = subjectsService.addSubject(subjectForm);
        return ResponseEntity.ok(result);

    }

    @PutMapping(value = "/subjects/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResult<Map<String, Object>>> updateSubject(SubjectForm subjectForm, @RequestParam(required = false) int id) throws ParseException {

        ServiceResult<Map<String, Object>> result = subjectsService.updateSubject(subjectForm, id);
        return ResponseEntity.ok(result);

    }

    @DeleteMapping(value = "/subjects/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResult<Map<String, Object>>> deleteSubject(@RequestParam(required = false) int id) throws ParseException {

        ServiceResult<Map<String, Object>> result = subjectsService.deleteSubject(id);
        return ResponseEntity.ok(result);

    }

    @GetMapping(value = "/subjects/dropdown/department", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<List<DropDownVo>> getDropdownByDepartment(@RequestParam(required = false) Integer deptID, @RequestParam(required = false) Integer gradeId) {
        return subjectsService.getDropDownByDeptIDAndGradeId(deptID, gradeId);
    }

    @GetMapping(value = "/subject/dept-type", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<Map<String, Object>> getSubjectByDeptAndType(@RequestParam Integer grade_level,
                                                                      @RequestParam Integer type,
                                                                      @RequestParam String year,
                                                                      @RequestParam Integer grade_id,
                                                                      @RequestParam Integer semester
    ) {
        return subjectsService.getSubjectByDeptAndType(grade_level, type, year, grade_id, semester);
    }

    @GetMapping(value = "/subject/config-score-subject", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<Map<String, Object>> getConfigScoreSubject(@RequestParam String parent_code) {
        return subjectsService.getConfigScoreSubject(parent_code);
    }

    @GetMapping(value = "/subject/config-grading-details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<Map<String, Object>> getConfigGradingDetails(@RequestParam String parent_code) {
        return subjectsService.getConfigGradingDetails(parent_code);
    }

    @GetMapping(value = "/subject/subject-not-yet-in-scoreboard", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<Map<String, Object>> getSubjectsNotYetConfigScoreboard(@RequestParam Integer grade_level,
                                                                                @RequestParam Integer sup_type,
                                                                                @RequestParam String year,
                                                                                @RequestParam Integer grade_id,
                                                                                @RequestParam Integer semester) {
        return subjectsService.getSubjectsNotYetConfigScoreboard(grade_level, sup_type, year, grade_id, semester);
    }

    @GetMapping(value = "/subject/subject-of-class", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<Map<String, Object>> getSubjectsOfClassInSemester(@RequestParam String class_code,
                                                                                @RequestParam Integer semester) {
        return subjectsService.getSubjectInClass(class_code, semester);
    }

}
