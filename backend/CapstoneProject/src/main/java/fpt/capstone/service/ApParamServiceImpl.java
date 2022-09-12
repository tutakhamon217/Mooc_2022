package fpt.capstone.service;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.repository.ApParamRepository;
import fpt.capstone.vo.ApParamDropDownVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Service
@Transactional
public class ApParamServiceImpl implements ApParamService {

    @Autowired
    private ApParamRepository repository;

    @Override
    public ServiceResult<List<ApParamDropDownVo>> getDropDownValues(String type) {
        return new ServiceResult<>(HttpStatus.OK, "Drop down value for " + type, repository.getDropDownValues(type));
    }

    @Override
    public ServiceResult<List<String>> getHolidays() {
        String holiday = repository.getHoliday().orElse("");
        StringTokenizer token = new StringTokenizer(holiday, "|");
        List<String> holidays = new ArrayList<>();
        while(token.hasMoreTokens())
        {
            holidays.add(token.nextToken());
        }

        return new ServiceResult<>(HttpStatus.OK, "holiday", holidays);
    }

}
