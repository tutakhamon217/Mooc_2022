package fpt.capstone.repository;

import fpt.capstone.entities.TransferStudentsDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TransferStudentsDetailsRepository extends JpaRepository<TransferStudentsDetails, Integer> {

    @Query(nativeQuery = true, value ="SELECT  sh.student_code,  \n" +
            "             s.full_name,  \n" +
            "             assd.id AS assdId,  \n" +
            "             assd.student_code AS assdStudentCode,  \n" +
            "             assd.academic_ability AS assdAcademicAbility,  \n" +
            "             assd.conduct AS assdConduct,  \n" +
            "             assd.competition_title AS assdCompetitionTitle,  \n" +
            "             assd.parent_code AS assdParentCode\n" +
            "            FROM  \n" +
            "             student_history sh  \n" +
            "             JOIN students s ON sh.student_code = s.code  \n" +
            "             JOIN class_room cr ON sh.current_class_code = cr.code  \n" +
            "             JOIN grade_level gl ON gl.id = cr.grade_level  \n" +
            "             LEFT JOIN assess_student_conduct ass ON sh.year = ass.school_year  \n" +
            "             AND sh.current_class_code = ass.class_code  \n" +
            "             AND ass.semester =   0\n" +
            "             LEFT JOIN assess_student_conduct_details assd ON ass.code = assd.parent_code  \n" +
            "             AND sh.student_code = assd.student_code  \n" +
            "             LEFT JOIN ap_param ap ON assd.competition_title = ap.code  \n" +
            "             AND ap.type = 'COMPETITION'  \n" +
            "             where assd.competition_title is not null and  sh.student_code =?1 and sh.year=?2 and sh.current_class_code=?3 ")
    List<Map<String, Object>> GetCompetitionTitleByStudentCodeYearClassCode (String studentCode, String year, String classCode);
}
