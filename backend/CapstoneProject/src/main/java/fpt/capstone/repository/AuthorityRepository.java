package fpt.capstone.repository;

import fpt.capstone.entities.Authority;
import fpt.capstone.entities.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {

    Optional<Authority> findByCode(String code);

    @Query(nativeQuery = true, value = "SELECT * FROM authority where code != 'ROLE_ADMIN' and code!='ROLE_USER'")
    List<Map<String, Object>> getAllAuthorityForTeacher();


}
