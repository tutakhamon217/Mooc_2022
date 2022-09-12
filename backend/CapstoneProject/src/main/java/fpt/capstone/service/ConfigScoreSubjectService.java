package fpt.capstone.service;

import fpt.capstone.entities.ServiceResult;

import java.util.List;

public interface ConfigScoreSubjectService {
    ServiceResult<String> getCodeOfCSS(String subject_code);

}
