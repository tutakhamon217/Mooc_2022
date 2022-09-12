package fpt.capstone.controller;

import fpt.capstone.entities.ServiceResult;

import fpt.capstone.entities.Teacher;
import fpt.capstone.service.TeacherService;
import fpt.capstone.service.AuthorityService;
import fpt.capstone.form_data.TeacherForm;
import fpt.capstone.vo.DropDownVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class TeacherController {
    @Autowired
    private TeacherService service;

    @GetMapping(value = "/teacher/dropdown/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<DropDownVo>> getDropDownValues(@PathVariable("id") Integer depId) {
        return service.getDropDownValues(depId);
    }

    @GetMapping(value = "/teacher/dropdown_root/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<DropDownVo>> getAllTeacherOfRoot(@PathVariable("id") Integer depId) {
        return service.getAllTeacherOfRoot(depId);
    }

    // @GetMapping(value = "/teacher/nguoiphutrach/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    // ServiceResult<List<DropDownVo>> getNguoiPhuTrachSchoolInformation(@PathVariable("id") Integer depId) {
    //     return service.getNguoiPhuTrachSchoolInformation(depId);
    // }

    @GetMapping(value = "/teacher/all", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceResult<Map<String, Object>>> getAllTeacher() {
        ServiceResult<Map<String, Object>> teachers = service.getAllteacher();
        return ResponseEntity.ok(teachers);
    }

    @GetMapping(value = "/teacher/gvcn", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceResult<Map<String, Object>>> getAllTeacherGVCN() {
        ServiceResult<Map<String, Object>> teachers = service.getAllteacherGVCN();
        return ResponseEntity.ok(teachers);
    }

    @GetMapping(value = "/teacher/all/search", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceResult<Map<String, Object>>> getAllTeacherByCodeOrFullNameAndAuthority(@RequestParam(required = true) String nameCode,
                                                                                                 @RequestParam(required = true) String authorityCode,                                                                                 @RequestParam(required = true) Integer deptId) throws ParseException {
        ServiceResult<Map<String, Object>> teachers = service.getAllTeacherByCodeOrFullNameAndAuthority(nameCode, authorityCode, deptId);
        return ResponseEntity.ok(teachers);
    }

    //    @GetMapping(value = "/teacher/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    ResponseEntity<ServiceResult<Map<String, Object>>> getTeacherDetailById(@PathVariable("id") Integer teacherId) {
//        ServiceResult<Map<String, Object>> teacher = service.getTeacherDetailById(teacherId);
//        return ResponseEntity.ok(teacher);
//    }
    @GetMapping(value = "/teacher/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceResult<Map<String, Object>>> getTeacherDetailById(@PathVariable("id") Integer teacherId) {
        ServiceResult<Map<String, Object>> teacher = service.getTeacherDetailById(teacherId);
        return ResponseEntity.ok(teacher);
    }

    @PostMapping(value = "/teacher/add", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<Boolean> addTeacher(@RequestBody TeacherForm teacherForm) {
        return service.insert(teacherForm);
    }

    @PutMapping(value = "/teacher/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<Boolean> updateTeacher(@RequestBody TeacherForm teacherForm, @PathVariable("id") Integer id) {
        return service.update(teacherForm, id);
    }
    @GetMapping(value = "/teacher/teachingAssignment", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceResult<Map<String, Object>>> getAllTeacherForTeachingAssignment(@RequestParam(required = true) Integer subjectId)throws ParseException {
        ServiceResult<Map<String, Object>> teachers = service.getAllTeacherForTeachingAssignment(subjectId);
        return ResponseEntity.ok(teachers);
    }

    @GetMapping(value = "/teacher/find-by-code", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<Teacher>> getTeacherByCode (@RequestParam String code){
        try{
            return new ServiceResult<>(HttpStatus.OK, "success", service.getTeacherByCode(code));
        }catch (Exception e){
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Không tồn tại giáo viên này", null);
        }
    }
}
