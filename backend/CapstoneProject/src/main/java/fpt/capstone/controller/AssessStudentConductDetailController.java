package fpt.capstone.controller;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.AssessStudentConductDetailForm;
import fpt.capstone.form_data.UpdateAssessStudentConductDetailForm;
import fpt.capstone.service.AssessStudentConductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class AssessStudentConductDetailController {
    @Autowired
    AssessStudentConductDetailService assessStudentConductDetailService;

    @GetMapping(value = "/assess-student-conduct-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<List<Map<String, Object>>> getInfoConductOfClass(@RequestParam() Integer semester,
                                                                          @RequestParam() String year,
                                                                          @RequestParam() Integer userId,
                                                                          @RequestParam() String classCode)
    {
        return assessStudentConductDetailService.getInfoConductOfClass(semester, year, userId, classCode);
    }

    @GetMapping(value = "/assess-student-conduct-detail/conduct-of-teacher", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<List<Map<String, Object>>> getEvaluateOfTeacher(@RequestParam() Integer semester,
                                                                          @RequestParam() String year,
                                                                          @RequestParam() String studentCode,
                                                                          @RequestParam() String classCode)
    {
        return assessStudentConductDetailService.getEvaluateOfTeacher(semester, year, studentCode, classCode);
    }

    @PutMapping(value = "/assess-student-conduct-detail/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<Boolean> updateAssessStudentConductDetail(@RequestBody UpdateAssessStudentConductDetailForm update){
        return assessStudentConductDetailService.updateAssessStudentConductDetail(update.getList(), update.getClassCode(), update.getSemester(), update.getSchoolYear());
    }

    @GetMapping(value = "/assess-student-conduct-detail/get-report-the-result-of-competition", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<List<Map<String, Object>>> getReportTheResultOfCompetition(@RequestParam() String year,
                                                                                    @RequestParam(required = false) Integer deptId,
                                                                                    @RequestParam(required = false) Integer gradeLevel,
                                                                                    @RequestParam(required = false) String classCode,
                                                                                    @RequestParam() Integer semester){
        return assessStudentConductDetailService.getReportTheResultOfCompetition(year,deptId,gradeLevel,classCode,semester);
    }

    @GetMapping(value = "/assess-student-conduct-detail/get-report-the-result-of-each-class", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<List<Map<String, Object>>> getReportTheResultOfEachClass(@RequestParam() String year,
                                                                                  @RequestParam() String classCode,
                                                                                  @RequestParam() Integer semester){
        return assessStudentConductDetailService.getReportTheResultOfEachClass(year,classCode,semester);
    }

        @GetMapping(value = "/assess-student-conduct-detail/check-update")
    public ServiceResult<Boolean> checkEnableUpdate(@RequestParam Integer semester){
        return assessStudentConductDetailService.checkEnableUpdate(semester);
    }
}
