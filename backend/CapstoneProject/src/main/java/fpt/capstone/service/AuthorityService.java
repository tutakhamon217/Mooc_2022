package fpt.capstone.service;

import fpt.capstone.entities.ServiceResult;

import java.util.Map;

public interface AuthorityService {
    ServiceResult<Map<String, Object>> getAllAuthorityForTeacher();
}
