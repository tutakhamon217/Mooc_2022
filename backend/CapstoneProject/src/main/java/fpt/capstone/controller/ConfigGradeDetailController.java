package fpt.capstone.controller;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.ConfigGradeDetailForm;
import fpt.capstone.service.ConfigGradeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ConfigGradeDetailController {
    @Autowired
    private ConfigGradeDetailService configGradeDetailService;


    @PostMapping(value = "/config-grade-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<List<ConfigGradeDetailForm>> add(@RequestBody List<ConfigGradeDetailForm> configGradeDetails) throws ParseException {
        ServiceResult<List<ConfigGradeDetailForm>> result = configGradeDetailService.saveAll(configGradeDetails);
        return result;
    }

    @DeleteMapping(value = "/config-grade-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<Boolean> deleteAll(@RequestBody Integer[] list) throws ParseException {
        return configGradeDetailService.deleteAll(list);
    }

    @PutMapping(value = "/config-grade-detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<Boolean> updateChange(@RequestBody List<ConfigGradeDetailForm> configGradeDetails) throws ParseException {
        return configGradeDetailService.updateChange(configGradeDetails);
    }
}
