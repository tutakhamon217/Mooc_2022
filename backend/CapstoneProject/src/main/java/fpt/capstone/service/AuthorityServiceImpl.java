package fpt.capstone.service;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public ServiceResult<Map<String, Object>> getAllAuthorityForTeacher() {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        try {
            List<Map<String, Object>> authorities = authorityRepository.getAllAuthorityForTeacher();
            Map<String, Object> output = new HashMap<>();
            output.put("authorities", authorities);
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(output);
        } catch (Exception e) {
            serviceResult.setMessage("fail");
            serviceResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return serviceResult;
    }
}
