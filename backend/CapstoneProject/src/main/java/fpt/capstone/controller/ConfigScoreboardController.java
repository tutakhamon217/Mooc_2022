package fpt.capstone.controller;

import fpt.capstone.entities.ConfigScoreboard;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.ConfigScoreDetailForm;
import fpt.capstone.form_data.CreateGradeDetailFromScoreboardForm;
import fpt.capstone.form_data.CreateScoreDetailFromScoreboardForm;
import fpt.capstone.service.ConfigScoreboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class ConfigScoreboardController {
    @Autowired
    private ConfigScoreboardService configScoreboardService;

    @GetMapping(value = "/config-scoreboard", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<List<Map<String, Object>>> getDropDownListGradeByYear(@RequestParam String year) throws ParseException {
        return new ServiceResult<>(HttpStatus.OK, "success", configScoreboardService.getDropDownListGradeByYear(year));
    }

    @PostMapping(value = "/scoreboard-score", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<ConfigScoreboard> configFromBeginningScoreboardScore(@RequestBody CreateScoreDetailFromScoreboardForm reqBody) throws ParseException {
        return new ServiceResult<>(HttpStatus.OK, "success", configScoreboardService.configFromBeginningScoreboardScore(reqBody.getConfigScoreDetails(), reqBody.getGrade_id(), reqBody.getYear(), reqBody.getSubjectCode(), reqBody.getSemester()));
    }

    @PostMapping(value = "/scoreboard-grading", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<ConfigScoreboard> configFromBeginningScoreboardGrading(@RequestBody CreateGradeDetailFromScoreboardForm reqBody) throws ParseException {
        return new ServiceResult<>(HttpStatus.OK, "success", configScoreboardService.configFromBeginningScoreboardGrading(reqBody.getConfigGradeDetailForms(), reqBody.getGrade_id(), reqBody.getYear(), reqBody.getSubjectCode(), reqBody.getSemester()));
    }
}
