package fpt.capstone.repository;

import fpt.capstone.entities.ConfigScoreSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConfigScoreSubjectRepository extends JpaRepository<ConfigScoreSubject, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM conf_score_subjects where subject_code like ?1 limit 1")
    List<ConfigScoreSubject> getCodeOfCSS(String subject_code);


}
