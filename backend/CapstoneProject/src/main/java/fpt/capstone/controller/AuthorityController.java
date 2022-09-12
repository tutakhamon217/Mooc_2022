package fpt.capstone.controller;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class AuthorityController {
    @Autowired
    private AuthorityService authorityService;

    @GetMapping(value = "/authority/all", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceResult<Map<String, Object>>> getAllAuthority() {
        return ResponseEntity.ok(authorityService.getAllAuthorityForTeacher());
    }
}
