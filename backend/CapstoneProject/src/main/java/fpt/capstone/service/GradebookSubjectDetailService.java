package fpt.capstone.service;

import fpt.capstone.entities.GradebookSubjectsDetails;
import fpt.capstone.entities.ServiceResult;

import java.util.List;

public interface GradebookSubjectDetailService {
    ServiceResult<Boolean> saveEvaluate(String year, String classCode, Integer semester, List<String> listStudentCode, String evaluate, String subjectCode);
}
