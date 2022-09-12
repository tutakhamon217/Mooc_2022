package fpt.capstone.utility;

import fpt.capstone.entities.Authority;
import fpt.capstone.entities.User;
import fpt.capstone.repository.AuthorityRepository;
import fpt.capstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class ConvertStringToAuthority {
    @Autowired
    private AuthorityRepository authorityRepository;

    public Set<Authority> convertStringToAuthority(Set authorities) {
        Set<String> strRoles = authorities;
        Set<Authority> roles = new HashSet<>();
        try {
            if (strRoles == null) {
                Authority userRole = authorityRepository.findByCode("ROLE_USER")
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            } else {
                strRoles.forEach(role -> {
                    switch (role) {
                        case "ROLE_GVBM":
                            Authority GVBMRole = authorityRepository.findByCode("ROLE_GVBM")
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(GVBMRole);
                            break;
                        case "ROLE_GVCN":
                            Authority GVCNRole = authorityRepository.findByCode("ROLE_GVCN")
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(GVCNRole);
                            break;
                        case "ROLE_HP":
                            Authority HPRole = authorityRepository.findByCode("ROLE_HP")
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(HPRole);
                            break;
                        case "ROLE_HT":
                            Authority HTRole = authorityRepository.findByCode("ROLE_HT")
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(HTRole);
                            break;
                        case "ROLE_TK":
                            Authority TKRole = authorityRepository.findByCode("ROLE_TK")
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(TKRole);
                            break;
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return roles;
    }
}
