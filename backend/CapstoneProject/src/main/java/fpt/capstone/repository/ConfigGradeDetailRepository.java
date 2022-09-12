package fpt.capstone.repository;

import fpt.capstone.entities.ConfigGradeDetail;
import fpt.capstone.entities.ConfigScoreDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConfigGradeDetailRepository extends JpaRepository<ConfigGradeDetail, Integer> {
    @Override
    <S extends ConfigGradeDetail> List<S> saveAll(Iterable<S> entities);

    @Query(nativeQuery = true, value = "select csd.* from capstone_db_v1.conf_score_subjects css\n" +
            "join conf_grading_details csd on csd.parent_code = css.code\n" +
            "where css.code like :code")
    List<ConfigGradeDetail> getAllConfGradeDetailByParentCode(@Param("code") String code);

    @Query(nativeQuery = true, value = "SELECT if(count(*), true, false) FROM gradebook_score_details where score_code like :code")
    Integer isScored(@Param("code") String code);
}
