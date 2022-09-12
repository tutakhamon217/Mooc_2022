package fpt.capstone.repository;

import fpt.capstone.entities.GradebookScoreDetails;
import fpt.capstone.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradebookScoreDetailsRepository extends JpaRepository<GradebookScoreDetails, Integer> {
    @Query(value = "SELECT gscore.* " +
            "            FROM students as stu " +
            "            JOIN gradebook_subjects_details as gsd ON stu.code = gsd.student_code " +
            "            JOIN gradebook as g ON g.code = gsd.parent_code " +
            "            JOIN gradebook_score_details as gscore ON gscore.parent_code = gsd.code " +
            "            JOIN subjects as su ON su.code=gsd.subject_code " +
            "            JOIN class_room as cl ON cl.code = g.class_code " +
            "            WHERE g.school_year =?3 AND g.semester =?4 AND su.code =?2 " +
            "            AND stu.id = ?1 AND gscore.times = ?5 AND gscore.score_code =?6", nativeQuery = true)
    List<GradebookScoreDetails> checkIsHaveScore(Long studentId, String subjectCode, String years, String semester, Long times, String scoreCode);

    @Query(value = "SELECT gsd.* FROM gradebook_score_details as gsd " +
            "JOIN gradebook_subjects_details as gsu ON gsd.parent_code = gsu.code " +
            "JOIN gradebook as g ON g.code=gsu.parent_code " +
            "WHERE gsd.score_code =?1 AND gsd.times =?2 AND g.school_year=?3 AND " +
            "g.semester=?4 AND g.class_code=?5 AND gsu.student_code=?6 AND gsu.subject_code =?7", nativeQuery = true)
    Optional<GradebookScoreDetails> findByScoreCodeAndTimes(String scoreCode, Integer times, String years,
                                                            String semester, String classCode, String studentCode, String subjectCode);
}
