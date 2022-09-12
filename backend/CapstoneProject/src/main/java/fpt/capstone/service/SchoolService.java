package fpt.capstone.service;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.SchoolForm;

import java.util.List;
import java.util.Map;

public interface SchoolService {
    ServiceResult<List<Map<String, Object>>> getAllDistrict(Integer province);

    ServiceResult<List<Map<String, Object>>> getAllProvince();

    ServiceResult<List<Map<String, Object>>> getSchoolInfo();

    ServiceResult<Boolean> save(SchoolForm schoolForm);
}
