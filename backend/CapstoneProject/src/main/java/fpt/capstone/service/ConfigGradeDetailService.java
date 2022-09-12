package fpt.capstone.service;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.ConfigGradeDetailForm;

import java.util.List;

public interface ConfigGradeDetailService {
    ServiceResult<List<ConfigGradeDetailForm>> saveAll(List<ConfigGradeDetailForm> list);

    ServiceResult<Boolean> deleteAll(Integer[] list);

    ServiceResult<Boolean> updateChange(List<ConfigGradeDetailForm> list);
}
