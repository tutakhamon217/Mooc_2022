package fpt.capstone.repository;

import fpt.capstone.entities.GradebookSubjectsDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface GradebookSubjectsDetailsRepository extends JpaRepository<GradebookSubjectsDetails, Integer> {
    Optional<GradebookSubjectsDetails> findById(Integer id);

    @Query(value = "SELECT * FROM gradebook_subjects_details " +
            "where UPPER(student_code) = binary UPPER(?1) AND UPPER(subject_code) = binary UPPER(?2) AND parent_code =?3",
            nativeQuery = true)
    Optional<GradebookSubjectsDetails> findFirstByStudentCodeAndSubjectCodeAndParentCode(String studentCode, String sbCode, String parentCode);

    @Query(value =
            "SELECT a.* FROM gradebook_subjects_details as a " +
                    "RIGHT JOIN gradebook as b ON a.parent_code=b.code" +
                    " WHERE UPPER(a.student_code) = BINARY UPPER(?1) " +
                    "AND UPPER(a.subject_code) = BINARY UPPER(?2) " +
                    "AND b.school_year =?3", nativeQuery = true)
    List<GradebookSubjectsDetails> getByStudentCodeAndSubjectCode(String studentCode, String subjectCode, String schoolYear);

        //     man danh gia hoc luc
    @Query(nativeQuery = true, value = "select d.student_code, d.subject_code, d.avg_score from gradebook as g join gradebook_subjects_details as d on g.code = d.parent_code  where g.class_code = ?1 and g.school_year = ?2  and g.semester = ?3")
    List<Map<String, Object>> findScoreOfClassInSemester(String classCode, String year, Integer semester);

    @Query(nativeQuery = true, value = "select d.student_code, d.subject_code, gs.value from gradebook as g join gradebook_subjects_details as d  on g.code = d.parent_code join gradebook_score_details as gs on d.code = gs.parent_code and gs.type=1 join conf_grading_details cgf on cgf.code = gs.score_code where (cgf.name like 'xếp loại' or cgf.name like 'Điểm trung bình') and g.class_code like ?1 and g.school_year like ?2 and g.semester = ?3 ")
    List<Map<String, Object>> findScoreOfClassInSemesterMonXepLoai(String classCode, String year, Integer semester);

    @Query(nativeQuery = true, value = "select * from gradebook_subjects_details where parent_code like :parentCode and student_code like :studentCode and subject_code like :subjectCode")
    List<GradebookSubjectsDetails> getGSDbyParentCodeAndStudentCodeAndSubjectCode(@Param("parentCode") String parentCode,
                                                                             @Param("studentCode") String studentCode,
                                                                             @Param("subjectCode") String subjectCode);
    @Query(nativeQuery = true, value = "select distinct s.code, s.full_name, gsd.evaluate from students s\n" +
            "join student_history sh on s.code = sh.student_code\n" +
            "join gradebook_subjects_details gsd on gsd.student_code = s.code\n" +
            "join gradebook gb on gb.code = gsd.parent_code \n" +
            "where sh.year like :year and gb.school_year like :year and sh.current_class_code like :classCode and s.status = 0 and gb.semester = :semester and gb.class_code like :classCode and gsd.subject_code like :subjectCode and evaluate_status = 1")
    List<Map<String, Object>> listStudentOfClassEvaluated(@Param("year") String year,
                                                          @Param("classCode") String classCode,
                                                          @Param("semester") Integer semester,
                                                          @Param("subjectCode") String subjectCode);

}
