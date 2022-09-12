package fpt.capstone.service;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.repository.GradeLevelRepositpry;
import fpt.capstone.vo.DropDownVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class GradeLevelServiceImpl implements GradeLevelService {
    @Autowired
    private GradeLevelRepositpry repositpry;


    @Override
    public ServiceResult<List<DropDownVo>> getDropdownList() {
        return new ServiceResult<>(HttpStatus.OK, "Dropdown", repositpry.getDropDownList());
    }
}
