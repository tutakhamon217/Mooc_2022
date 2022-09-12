package fpt.capstone.controller;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.EvaluateSubjectForm;
import fpt.capstone.service.GradebookSubjectDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class GradebookSubjectDetailController {
    @Autowired
    private GradebookSubjectDetailService gradebookSubjectDetailService;

    @PostMapping(value = "/gradebook-subject-detail/evaluate")
    public ServiceResult<Boolean> evaluateSubject(EvaluateSubjectForm evaluateSubjectForm){
        return gradebookSubjectDetailService.saveEvaluate(evaluateSubjectForm.getYear(), evaluateSubjectForm.getClassCode(), evaluateSubjectForm.getSemester(), evaluateSubjectForm.getListStudentCode(),evaluateSubjectForm.getEvaluate(),evaluateSubjectForm.getSubjectCode());
    }
}
