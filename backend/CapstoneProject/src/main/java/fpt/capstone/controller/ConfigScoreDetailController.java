package fpt.capstone.controller;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.ConfigScoreDetailForm;
import fpt.capstone.service.ConfigScoreDetailService;
import fpt.capstone.vo.ScoreStudentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

import java.util.List;


@RestController
@CrossOrigin(origins = "*")
public class ConfigScoreDetailController {
    @Autowired
    private ConfigScoreDetailService configScoreDetailService;

    @PostMapping(value = "/config-score-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<List<ConfigScoreDetailForm>> add(@RequestBody List<ConfigScoreDetailForm> configScoreDetails) throws ParseException {
        ServiceResult<List<ConfigScoreDetailForm>> result = configScoreDetailService.saveAll(configScoreDetails);
        return result;
    }

    @DeleteMapping(value = "/config-score-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<Boolean> deleteAll(@RequestBody Integer[] list) throws ParseException {
        return configScoreDetailService.deleteAll(list);
    }

    @PutMapping(value = "/config-score-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<Boolean> updateChange(@RequestBody List<ConfigScoreDetailForm> configScoreDetails) throws ParseException {
        return configScoreDetailService.updateChange(configScoreDetails);
    }


}
