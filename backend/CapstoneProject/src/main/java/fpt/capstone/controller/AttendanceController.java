package fpt.capstone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import fpt.capstone.entities.AttendanceStudentDetail;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.UpdateAttendanceDTO;
import fpt.capstone.service.AttendanceService;
import fpt.capstone.vo.StudentAttendanceVo;

@RestController
@CrossOrigin(origins = "*")
public class AttendanceController {

    @Autowired
    AttendanceService service;

    @GetMapping(value = "/attendance", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<AttendanceStudentDetail>> getAttendance(@RequestParam String month,@RequestParam String year,@RequestParam String classCode)
    {
        return service.getAttendance(month, year, classCode);
    }

    @PostMapping(value = "/attendance/update", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<Boolean> updateAttendance(@RequestBody UpdateAttendanceDTO form)
    {
        return service.updateAttendace(form);
    }

    @GetMapping(value = "/attendance/student", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<StudentAttendanceVo>> getStudentAttendance(@RequestParam String year, @RequestParam String month, @RequestParam String studentCode)
    {
        return service.getStudentAttendance(year, month, studentCode);
    }
}
