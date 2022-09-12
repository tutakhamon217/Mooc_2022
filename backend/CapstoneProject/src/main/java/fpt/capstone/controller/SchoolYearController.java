package fpt.capstone.controller;

import fpt.capstone.entities.SchoolYear;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.repository.SchoolYearRespository;
import fpt.capstone.service.SchoolYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import fpt.capstone.form_data.SchoolYearForm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class SchoolYearController {

    private final SchoolYearRespository respository;
    @Autowired
    private SchoolYearService schoolYearService;


    SchoolYearController(SchoolYearRespository respository) {
        this.respository = respository;
    }


    @GetMapping(value = "/school_years/all", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<String>> getAllSchoolYears() {
        return schoolYearService.getAllSchoolYears();
    }


    @GetMapping(value = "/school_years/all/paging", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResult<Map<String, Object>>> getAllSchoolPaging() throws ParseException {
        ServiceResult<Map<String, Object>> allGroup = schoolYearService.getAllSchoolYearPaging();
        return ResponseEntity.ok(allGroup);

    }

    @PostMapping(value = "/school_years/save", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResult<Map<String, Object>>> saveSchoolYear(@RequestBody SchoolYearForm schoolYearForm) {
        ServiceResult<Map<String, Object>> result = schoolYearService.saveSchoolYear(schoolYearForm);
        return ResponseEntity.ok(result);

    }

    @GetMapping(value = "/school_years/semesterAmount", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getSemesterAmount(@RequestParam(required = false) String years) throws ParseException {
        Map<String, Object> output = schoolYearService.getSemesterAmount(years);
        return ResponseEntity.ok(output);

    }

    @GetMapping(value = "/school_years/checkRange", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<Boolean> checkSemesterRange(@RequestParam(required = false) String years, @RequestParam(required = false) Integer semester) throws ParseException {
        ServiceResult<Boolean> output = schoolYearService.checkStartEndSemester(years, semester);
        return output;

    }

    @GetMapping(value = "/school_years/curentSchoolyear", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResult<Map<String, Object>>> getCurrentSchoolyear() throws ParseException {
        ServiceResult<Map<String, Object>> result = schoolYearService.getCurrentSchoolyear();
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/school_years/years", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSchoolyearByYear(@RequestParam(required = false) String years) throws ParseException {
        return ResponseEntity.ok(schoolYearService.getSchoolYearByYears(years));
    }

    @GetMapping(value = "/school_years", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<SchoolYear> getSchoolYear(@RequestParam() String year, @RequestParam() String semester) {
        return schoolYearService.getSchoolYear(year, semester);
    }

    @GetMapping(value = "/school_year/of", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceResult<SchoolYear> getSchoolYearOf(@RequestParam() String date) {
        return schoolYearService.getSchoolYearOf(date);
    }

    @GetMapping(value = "/school_year/history")
    public ServiceResult<List<String>> getSchoolYearOfHistoryStudent(@RequestParam() String studentCode) {
        return schoolYearService.getSchoolYearOfHistoryStudent(studentCode);
    }


    @GetMapping(value = "/school_year/CurrentAndNextSchoolYear")
    public ServiceResult<List<String>> getCurrentAndNextSchoolYear() {
        return schoolYearService.getCurrentAndNextSchoolYear();
    }

    @GetMapping(value = "/school_year/obj_current_year")
    public ServiceResult<SchoolYear> getObjCurrentYear() {
        return schoolYearService.getObjCurrentYear();
    }

    @GetMapping(value = "/school_year/get-range")
    public ServiceResult<List<Map<String, Object>>> getRangeOfSemester(String year, Integer semester){
        return schoolYearService.getRangeOfSemester(year, semester);
    }
}
