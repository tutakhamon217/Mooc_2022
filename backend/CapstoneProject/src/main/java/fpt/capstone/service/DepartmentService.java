package fpt.capstone.service;


import fpt.capstone.form_data.UpdateDepartmentForm;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.vo.DepartmentVo;
import fpt.capstone.vo.DepartmentVoV1;
import fpt.capstone.vo.DepartmentWithTypeVo;
import fpt.capstone.vo.DropDownVo;

import java.util.List;
import java.util.Map;

public interface DepartmentService {
    ServiceResult<List<DepartmentVo>> getDepartmentByParentID(Integer parent_id);

    ServiceResult<List<DepartmentVoV1>> searchDepartmentsByCodeOrName(String code, String name);

    ServiceResult<Boolean> deleteDepartment(Integer id);

    ServiceResult<Boolean> checkUpdateParentAndType(Integer id);

    ServiceResult<Boolean> updateDepartment(UpdateDepartmentForm form);

    ServiceResult<Boolean> insertDepartment(UpdateDepartmentForm form);

    ServiceResult<List<DepartmentVoV1>> getAllDepartments();

    ServiceResult<List<DropDownVo>> getDropDownValues();

    ServiceResult<List<DepartmentWithTypeVo>> getDropDownValuesWithType();

    ServiceResult<List<DropDownVo>> getDropdownForClassRoom();

    ServiceResult<List<DropDownVo>> getAllParentDepartment();

    ServiceResult<List<DropDownVo>> getDepartmentByParentId(int parentId);

    ServiceResult<Map<String, Object>> getDepartmentAndParentDepartment(int id);


}
