package fpt.capstone.controller;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.SchoolForm;
import fpt.capstone.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class SchoolController {

    @Autowired
    private SchoolService schoolService;

    @GetMapping(value = "/district")
    public ServiceResult<List<Map<String, Object>>> getAllDistrict(@RequestParam Integer province){
        return schoolService.getAllDistrict(province);
    }

    @GetMapping(value = "/province")
    public ServiceResult<List<Map<String, Object>>> getAllProvince(){
        return schoolService.getAllProvince();
    }

    @GetMapping(value = "/school-infor")
    public ServiceResult<List<Map<String, Object>>> getSchoolInfo(){
        return schoolService.getSchoolInfo();
    }

    @PostMapping(value = "/school-infor/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<Boolean> save(@RequestBody SchoolForm schoolForm){ return schoolService.save(schoolForm);}
}
