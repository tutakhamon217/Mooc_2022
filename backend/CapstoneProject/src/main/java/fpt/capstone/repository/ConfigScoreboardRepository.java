package fpt.capstone.repository;

import fpt.capstone.entities.ConfigScoreboard;
import fpt.capstone.entities.ServiceResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ConfigScoreboardRepository extends JpaRepository<ConfigScoreboard, Integer> {

    @Query(nativeQuery = true, value = "select distinct year, grade_id, gl.name, gl.code from scoreboard_conf sc join grade_level gl on sc.grade_id = gl.id where year like ?1")
    List<Map<String, Object>> getDropDownListGradeByYear(String year);
}
