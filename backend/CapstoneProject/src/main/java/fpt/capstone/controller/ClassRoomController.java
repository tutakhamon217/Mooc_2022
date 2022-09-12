package fpt.capstone.controller;

import fpt.capstone.entities.ClassRoom;
import fpt.capstone.form_data.ClassRoomForm;
import fpt.capstone.form_data.ClassRoomSearchForm;
import fpt.capstone.security.services.UserDetailsImpl;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.entities.Teacher;
import fpt.capstone.service.ClassRoomService;
import fpt.capstone.service.TeacherService;
import fpt.capstone.vo.ClassRoomVo;
import fpt.capstone.vo.DropDownKeyValueVo;
import fpt.capstone.vo.DropDownVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class ClassRoomController {
    @Autowired
    private ClassRoomService service;

    @Autowired
    private TeacherService teacherService;

    @GetMapping(value = "/classroom/years", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<String>> getAllSchoolYears() {
        return service.getDropDownSchoolYears();
    }

    @GetMapping(value = "/classroom/all", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<Page<ClassRoomVo>> getAllClassroom(@RequestParam() String years, @RequestParam() int pageIndex, @RequestParam() int pageSize) {
        return service.getAllClassroomsByYears(years, PageRequest.of(pageIndex, pageSize));
    }

    @GetMapping(value = "/classroom/school_years", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<DropDownVo>> getAllClassroomByYearGrade(@RequestParam() Integer gradeLevel, @RequestParam() String years, @RequestParam() Integer deptID) {
        return service.getAllClassroomsByYearsAndGrade(gradeLevel, years, deptID);
    }

    @GetMapping(value = "/classroom/school_years_alldept", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<DropDownVo>> getAllClassroomByYearGradeAllDept(@RequestParam() Integer gradeLevel, @RequestParam() String years) {
        return service.getAllClassroomsByYearsAndGradeAllDept(gradeLevel, years);
    }

    @PostMapping(value = "/classroom/search", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<Page<ClassRoomVo>> search(@RequestBody ClassRoomSearchForm form) {
        return service.searchClassRooms(form.getYears(), form.getCode(), form.getName(), form.getGradeLevel(), form.getDeptID(), PageRequest.of(form.getPageIndex(), form.getPageSize()));
    }

    @GetMapping(value = "/classroom/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<ClassRoomVo> getByID(@PathVariable("id") Integer id) {
        return service.getByID(id);
    }

    @DeleteMapping(value = "/classroom/delete/{classCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<Boolean> deleteClassRoom(@PathVariable("classCode") String classCode) {
        return service.deleteClassRoom(classCode);
    }

    @PostMapping(value = "/classroom/update", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<Boolean> updateClassRoom(@RequestBody ClassRoomForm form) {
        return service.updateClassRoom(form);
    }

    @PostMapping(value = "/classroom/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<Boolean> insertClassRoom(@RequestBody ClassRoomForm form) {
        return service.insert(form);
    }

    @GetMapping(value = "/classroom/gradeLevelAndYears", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceResult<Map<String, Object>>> getClassByGradeLevelAndYears(@RequestParam() String years, @RequestParam() int gradeLevel) {
        return ResponseEntity.ok(service.getAllClassByGradeLevelAndYears(gradeLevel, years));
    }

    @GetMapping(value = "/classroom/teacher-year", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceResult<List<ClassRoom>>> getClassroomByTeacherIdAndYear(@RequestParam() Integer id,
                                                                                  @RequestParam() String year){
        return ResponseEntity.ok(service.getClassroomByTeacherIdAndYear(id, year));
    };

    @GetMapping(value = "/classroom/manage", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<ClassRoom>> getClassroomManagedBy(@RequestParam() String teacherCode, @RequestParam() String year)
    {
        return service.getClassManagedBy(teacherCode, year);
    }

    @GetMapping(value = "/classroom/teacher", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceResult<List<ClassRoom>>> getAllClassroomOfTeacher(@RequestParam() String years)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        String login = userDetails.getUsername();
        List<Teacher> teacher= teacherService.getTeacherByCode(login);
        if (teacher.size() == 0) {
            ServiceResult<List<ClassRoom>> serviceResult = new ServiceResult<>();
            serviceResult.setMessage("Không quyền truy cập");
            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(serviceResult);
        }
        return ResponseEntity.ok(service.getClassroomByTeacherIdAndYear(userDetails.getId(), years));
    }


}
