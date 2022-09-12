package fpt.capstone.service;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.vo.ApParamDropDownVo;

import java.util.List;

public interface ApParamService {
    ServiceResult<List<ApParamDropDownVo>> getDropDownValues(String type);
    ServiceResult<List<String>> getHolidays();
}
