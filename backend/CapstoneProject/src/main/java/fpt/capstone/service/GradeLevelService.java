package fpt.capstone.service;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.vo.DropDownVo;

import java.util.List;

public interface GradeLevelService {
    ServiceResult<List<DropDownVo>> getDropdownList();
}
