package fpt.capstone.repository;

import fpt.capstone.entities.ConfigScoreDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ConfigScoreDetailRepository extends JpaRepository<ConfigScoreDetail, Integer> {

    @Override
    <S extends ConfigScoreDetail> List<S> saveAll(Iterable<S> entities);

    @Query(nativeQuery = true, value = "" +
            "select gsd.student_code,s.full_name,gsd.avg_score,csd.id as confScoreDetailsId,csd.name as confScoreDetailsName,gsd2.id as gradebookScoreDetailsId  ,gsd2.times , gsd2.value\n" +
            "    from gradebook g  join gradebook_subjects_details gsd on g.code=gsd.parent_code\n" +
            "    join conf_score_subjects css on gsd.subject_code=css.subject_code\n" +
            "    join scoreboard_conf sc on sc.code=css.parent_code join conf_score_details csd on css.code=csd.parent_code\n" +
            "    join gradebook_score_details gsd2 on gsd.code=gsd2.parent_code join students s on gsd.student_code=s.code\n" +
            "    where css.subject_code=?1 and g.semester=?2 and g.class_code=?3")
    List<Map<String, Object>> getScoreBySubjectCodeAndClassCodeAndSemester(String subjectCode, Integer semester, String classCode);

    @Query(nativeQuery = true, value = "select gsd.student_code,s.full_name,gsd.avg_score,csd.id as confScoreDetailsId,csd.name as confScoreDetailsName,\n" +
            "csd.coefficient,csd.quantity,csd.minimum_score as minimumScore,gsd2.id as gradebookScoreDetailsId  ,gsd2.times , gsd2.value\n" +
            " from gradebook g  join gradebook_subjects_details gsd on g.code=gsd.parent_code \n" +
            "join conf_score_subjects css on gsd.subject_code=css.subject_code\n" +
            "join scoreboard_conf sc on sc.code=css.parent_code join conf_score_details csd on css.code=csd.parent_code \n" +
            " join gradebook_score_details gsd2 on gsd.code=gsd2.parent_code join students s on gsd.student_code=s.code\n" +
            " where css.subject_code=?1 and g.semester=?2 and g.class_code=?3  ")
    List<Map<String, Object>> getScoreStudent(String subjectCode, Integer semester, String classCode);

    @Query(nativeQuery = true, value = " select csd.id as confScoreDetailsId,csd.name as confScoreDetailsName, csd.coefficient,csd.quantity,csd.minimum_score as miniumScore\n" +
            " from gradebook g  join gradebook_subjects_details gsd on g.code=gsd.parent_code \n" +
            "join conf_score_subjects css on gsd.subject_code=css.subject_code\n" +
            "join scoreboard_conf sc on sc.code=css.parent_code join conf_score_details csd on css.code=csd.parent_code \n" +
            " join gradebook_score_details gsd2 on gsd.code=gsd2.parent_code join students s on gsd.student_code=s.code\n" +
            " where css.subject_code=?1 and g.semester=?2 and g.class_code=?3 and gsd.student_code=?4")
    List<Map<String, Object>> getScore(String subjectCode, Integer semester, String classCode, String studentCode);

    @Query(nativeQuery = true, value = " select gsd2.id as gradebookScoreDetailsId , gsd2.times , gsd2.value\n" +
            " from gradebook g  join gradebook_subjects_details gsd on g.code=gsd.parent_code \n" +
            "join conf_score_subjects css on gsd.subject_code=css.subject_code\n" +
            "join scoreboard_conf sc on sc.code=css.parent_code join conf_score_details csd on css.code=csd.parent_code \n" +
            " join gradebook_score_details gsd2 on gsd.code=gsd2.parent_code join students s on gsd.student_code=s.code\n" +
            " where css.subject_code=?1 and g.semester=?2 and g.class_code=?3 and gsd.student_code=?4 and csd.id =?5")
    List<Map<String, Object>> getScoreDetail(String subjectCode, Integer semester, String classCode, String studentCode, Integer confScoreDetailsId);

    @Query(nativeQuery = true, value = "SELECT if(count(*), true, false) FROM gradebook_score_details where score_code like :code")
    Integer isScored(@Param("code") String code);

    @Query(nativeQuery = true, value = "select csd.* from conf_score_subjects css\n" +
            "join conf_score_details csd on csd.parent_code = css.code\n" +
            "where css.code like :code")
    List<ConfigScoreDetail> getAllSubjectScoreByParentCode(@Param("code") String code);
}
