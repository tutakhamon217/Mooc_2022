package fpt.capstone.controller;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.TimetableForm;
import fpt.capstone.service.TimetableService;
import fpt.capstone.vo.TeacherTimeTableVo;
import fpt.capstone.vo.TimeTableDropDown;
import fpt.capstone.vo.TimeTableVo;
import fpt.capstone.vo.TimeTableVoV2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@RestController
@CrossOrigin(origins = "*")
public class TimetableController {

    @Autowired
    private TimetableService service;

    @GetMapping(value = "/timetable", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<TimeTableVoV2>> getTimetable(@RequestParam() String year, @RequestParam() String semester, @RequestParam() String gradeLevel, @RequestParam() String className, @RequestParam() String applyDate)
    {
        return service.getScheduleTimetableByYearAndSemester(year, semester, gradeLevel, className, LocalDate.parse(applyDate));
    }

    @GetMapping(value = "/timetable/dropdown", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<TimeTableDropDown>> getDropDown(@RequestParam() String year, @RequestParam() String semester, @RequestParam() String className)
    {
        return service.getTimeTableDropdown(year, semester, className);
    }

    @PostMapping(value = "/timetable/update", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<Boolean> update(@RequestBody TimetableForm form)
    {
        return service.updateTimeTable(form);
    }

    @GetMapping(value="/timetable/export")
    public ResponseEntity<Resource> getMethodName(@RequestParam() String year, @RequestParam() String semester, @RequestParam() String gradeLevel, @RequestParam() String className, @RequestParam() String applyDate) {
        InputStreamResource file = new InputStreamResource(service.exportToExcel(year, semester, gradeLevel, className, LocalDate.parse(applyDate)));
        return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=timetable.xlsx")
        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
        .body(file);
    }

    @GetMapping(value="/timetable/template")
    public ResponseEntity<Resource> getTemplateFile(@RequestParam() String years, @RequestParam() String semester, @RequestParam() String gradeLevel)
    {
        InputStreamResource file = new InputStreamResource(service.downloadTemplateFile(years, semester, gradeLevel));
        return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=DS_TKB.xlsx")
        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
        .body(file);
    }
    

    @PostMapping(value = "/timetable/upload")
    public ServiceResult<Boolean> upload(@RequestParam("year") String year, @RequestParam("semester") String semester, @RequestParam("gradeLevel") String gradeLevel, @RequestParam("applyDate") String applyDate, @RequestParam("file") MultipartFile file)
    {
        //LocalDate date = LocalDate.parse(applyDate);
        return service.upload(year, semester, gradeLevel, applyDate, file);
    }

    @GetMapping(value="/timetable/teacher")
    public ServiceResult<List<List<TeacherTimeTableVo>>> getTimetableForTeacher(@RequestParam("applyDate") String applyDate, @RequestParam("teacherCode") String teacherCode)
    {
        return service.getTimetableForTeacher(applyDate, teacherCode);
    }
}
