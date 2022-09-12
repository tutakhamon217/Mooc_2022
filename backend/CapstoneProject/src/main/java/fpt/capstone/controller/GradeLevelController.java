package fpt.capstone.controller;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.service.GradeLevelService;
import fpt.capstone.vo.DropDownVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class GradeLevelController {
    @Autowired
    private GradeLevelService service;

    @GetMapping(value = "/gradelevel/dropdown", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<DropDownVo>> getDropDownList() {
        return service.getDropdownList();
    }
}
