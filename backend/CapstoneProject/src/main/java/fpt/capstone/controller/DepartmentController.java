package fpt.capstone.controller;

import fpt.capstone.form_data.DepartmentSearch;
import fpt.capstone.form_data.UpdateDepartmentForm;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.service.DepartmentService;
import fpt.capstone.vo.DepartmentVo;
import fpt.capstone.vo.DepartmentVoV1;
import fpt.capstone.vo.DropDownVo;
import fpt.capstone.vo.DepartmentWithTypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class DepartmentController {
    @Autowired
    private DepartmentService service;

    @GetMapping(value = "/department/{parent_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<DepartmentVo>> getDepartmentsByParentID(@PathVariable("parent_id") Integer parentID) {
        return service.getDepartmentByParentID(parentID);
    }

    @PostMapping(value = "/department/search", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<DepartmentVoV1>> searchDepartment(@RequestBody DepartmentSearch form) {
        return service.searchDepartmentsByCodeOrName(form.getCode(), form.getName());
    }

    @DeleteMapping(value = "/department/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<Boolean> removeDepartment(@PathVariable("id") Integer id) {
        return service.deleteDepartment(id);
    }

    @DeleteMapping(value = "/department/check/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<Boolean> checkUpdateParentAndType(@PathVariable("id") Integer id) {
        return service.checkUpdateParentAndType(id);
    }

    @PostMapping(value = "/department/update", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<Boolean> updateDepartment(@RequestBody UpdateDepartmentForm form) {
        return service.updateDepartment(form);
    }

    @GetMapping(value = "/department/all", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<DepartmentVoV1>> getDepartmentsByParentID() {
        return service.getAllDepartments();
    }


    @GetMapping(value = "/department/dropdown", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<DropDownVo>> getDropDownValues() {
        return service.getDropDownValues();
    }

    @GetMapping(value = "/department/dropdown_with_type", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<DepartmentWithTypeVo>> getDropDownValuesWithType() {
        return service.getDropDownValuesWithType();
    }

    @PostMapping(value = "/department/insert", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<Boolean> insert(@RequestBody UpdateDepartmentForm form) {
        return service.insertDepartment(form);
    }

    @GetMapping(value = "/department/dropdown/classroom", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<DropDownVo>> getDropdownForClassRoom() {
        return service.getDropdownForClassRoom();
    }

    // cau hinh lop hoc
    @GetMapping(value = "/department/class-information", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<DropDownVo>> getDropdownForClassRoomInformation() {
        return service.getDropdownForClassRoom();
    }

    @GetMapping(value = "/department/dropdown/parent", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<DropDownVo>> getAllParentDepertment() {
        return service.getAllParentDepartment();
    }

    @GetMapping(value = "/department/dropdown/{parent_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ServiceResult<List<DropDownVo>> getParentDepertmentByParentId(@PathVariable("parent_id") int parentID) {
        return service.getDepartmentByParentId(parentID);
    }

    @GetMapping(value = "/department/parentDepartment/{user_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceResult<Map<String, Object>>> getDepartmentAndParentDepartment(@PathVariable("user_id") int userId) {
        return ResponseEntity.ok(service.getDepartmentAndParentDepartment(userId));
    }

}
