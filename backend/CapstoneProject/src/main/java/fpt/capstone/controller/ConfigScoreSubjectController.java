package fpt.capstone.controller;

import fpt.capstone.entities.ConfigScoreSubject;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.service.ConfigScoreSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class ConfigScoreSubjectController {

    @Autowired
    private ConfigScoreSubjectService configScoreSubjectService;

    @GetMapping(value = "/config-score-subject/{subject_code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<String> getCodeOfCSS(@PathVariable("subject_code") String subject_code) {
        return configScoreSubjectService.getCodeOfCSS(subject_code);
    }
}
