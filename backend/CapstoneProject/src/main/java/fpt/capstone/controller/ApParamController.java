package fpt.capstone.controller;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.service.ApParamService;
import fpt.capstone.vo.ApParamDropDownVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ApParamController {
    @Autowired
    private ApParamService service;

    @GetMapping(value = "/ap_param/dropdown/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<ApParamDropDownVo>> getDropDownValues(@PathVariable("type") String type) {
        return service.getDropDownValues(type);
    }

    @GetMapping(value = "/ap_param/holiday", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<String>> getHolidays()
    {
        return service.getHolidays();
    }
}
