package fpt.capstone.repository;

import fpt.capstone.entities.Gradebook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface GradebookRepository extends JpaRepository<Gradebook, Integer> {
    @Query(
            value =
                    "   SELECT  \n" +
                            "                tb1.id as idStudent, full_name as fullName, studentCode,  \n" +
                            "                csdId, csdName, csdCode, csdCoefficient,csdQuantity,csdMinimumScore,csdParent, \n" +
                            "                avgScore, gsdId, gsdCode, gsdParent,gsdEvaluate,gsdUpdater,gsdStatus, \n" +
                            "                gscoreId, gscoreCode,gscoreParent, gscoreTimes, gscoreValue, gscoreCoefficient \n" +
                            "           FROM \n" +
                            "               (SELECT DISTINCT  \n" +
                            "                   st.id,  \n" +
                            "                   st.full_name,  \n" +
                            "                   (CAST(st.code AS CHAR CHARACTER SET utf8mb4) COLLATE utf8mb4_bin) as studentCode,  \n" +
                            "                   st.status, \n" +
                            "                   csd.name as csdName,  \n" +
                            "                   csd.id as csdId, \n" +
                            "                   csd.code as csdCode,  \n" +
                            "                   csd.coefficient as csdCoefficient,  \n" +
                            "                   csd.quantity as csdQuantity, \n" +
                            "                   csd.minimum_score as csdMinimumScore, \n" +
                            "                   csd.parent_code as csdParent, \n" +
                            "                   (CAST(cl.code AS CHAR CHARACTER SET utf8mb4) COLLATE utf8mb4_bin) as classCode, \n" +
                            "                   (CAST(s.code AS CHAR CHARACTER SET utf8mb4) COLLATE utf8mb4_bin) as subjectCode  \n" +
                            "               FROM conf_score_details as csd \n" +
                            "               JOIN conf_score_subjects as css ON csd.parent_code = css.code  \n" +
                            "               JOIN subjects as s ON css.subject_code = s.code  \n" +
                            "               JOIN scoreboard_conf as scf ON scf.code = css.parent_code \n" +
                            "               JOIN subject_class as sc ON s.id = sc.subject_id \n" +
                            "               JOIN class_room as cl ON cl.id=sc.class_id  \n" +
                            "               JOIN student_history as sh  \n" +
                            "                    ON (sh.current_class_code = cl.code AND sh.year = scf.year)  \n" +
                            "               JOIN students as st ON sh.student_code = st.code  \n" +
                            "               WHERE UPPER(cl.code) =  BINARY UPPER(?1)  \n" +
                            "                   AND UPPER(s.code) =  BINARY UPPER(?2)  \n" +
                            "                   AND scf.year =?3  \n" +
                            "                   AND (st.status = ?4 OR st.status = ?5) and css.semester = ?6\n" +
                            "                   ORDER BY csd.id  \n" +
                            "               ) as tb1  \n" +
                            "           LEFT JOIN  \n" +
                            "               (SELECT DISTINCT \n" +
                            "                   student.id as idStudent, \n" +
                            "                   gscore.score_code as scoreCode, \n" +
                            "                   gsd.avg_score as avgScore,  \n" +
                            "                   gsd.id as gsdId, \n" +
                            "                   gsd.code as gsdCode, \n" +
                            "                   gsd.parent_code as gsdParent, \n" +
                            "                   gsd.evaluate as gsdEvaluate, \n" +
                            "                   gsd.updater as gsdUpdater, \n" +
                            "                   gsd.evaluate_status as gsdStatus, \n" +
                            "                   gscore.id as gscoreId, \n" +
                            "                   gscore.code as gscoreCode, \n" +
                            "                   gscore.parent_code as gscoreParent, \n" +
                            "                   gscore.times as gscoreTimes, \n" +
                            "                   gscore.value as gscoreValue, \n" +
                            "                   gscore.coefficient as gscoreCoefficient, \n" +
                            "                   (CAST(g.class_code AS CHAR CHARACTER SET utf8mb4) COLLATE utf8mb4_bin) as classCode, \n" +
                            "                   (CAST(gsd.subject_code AS CHAR CHARACTER SET utf8mb4) COLLATE utf8mb4_bin) as subjectCode  \n" +
                            "               FROM gradebook as g  \n" +
                            "               LEFT JOIN gradebook_subjects_details as gsd ON g.code = gsd.parent_code \n" +
                            "               JOIN students as student ON student.code = gsd.student_code  \n" +
                            "               LEFT JOIN gradebook_score_details as gscore ON gscore.parent_code = gsd.code  \n" +
                            "               WHERE UPPER(g.class_code) = BINARY UPPER(?1)  \n" +
                            "               AND g.school_year =?3  \n" +
                            "               AND g.semester =?6  \n" +
                            "               AND UPPER(gsd.subject_code) = BINARY UPPER(?2)  \n" +
                            "              ) as tb2   \n" +
                            "           ON  \n" +
                            "           case when tb2.scoreCode is not null  \n" +
                            "                then (     tb1.id = tb2.idStudent  \n" +
                            "                       AND tb1.classCode = BINARY tb2.classCode \n" +
                            "                       AND tb1.subjectCode = BINARY tb2.subjectCode  \n" +
                            "                       AND tb1.csdCode = tb2.scoreCode ) \n" +
                            "                else (     tb1.id = tb2.idStudent  \n" +
                            "                       AND tb1.classCode = BINARY tb2.classCode \n" +
                            "                       AND tb1.subjectCode = BINARY tb2.subjectCode ) end  \n" +
                            "           ORDER BY tb1.full_name ASC, studentCode ASC, csdId ASC, gscoreTimes ASC",
            nativeQuery = true
    )
    List<Object[]> getStudentListWithEachScore(
            String classCode,
            String subjectCode,
            String years,
            Integer status1,
            Integer status2,
            String semester
    );
    Gradebook findBySchoolYearAndSemesterAndClassCode(String schoolYear, String semester, String classCode);
    @Query(
            value = "SELECT " +
                    "   studentId,  studentName,studentCode," +
                    "   cgdId, cgdCode, cgdname, cgdTypeChoose, cgdSelectedValue," +
                    "   gsdId, gsdCode, gsdParent, gsdEvaluate, gsdUpdater, gsdStatus," +
                    "   gscoreId, gscoreCode, gscoreParent, gscoreTimes, gscoreValue, gscoreCoefficient " +
                    "FROM " +
                    "   (SELECT DISTINCT st.id as studentId, " +
                    "       (CAST(st.code AS CHAR CHARACTER SET utf8mb4) COLLATE utf8mb4_bin) as studentCode ,  " +
                    "       st.full_name as studentName," +
                    "       cgd.id as cgdId," +
                    "       cgd.code as cgdCode, " +
                    "       cgd.name as cgdname," +
                    "       cgd.type_choose as cgdTypeChoose," +
                    "       cgd.selected_value as cgdSelectedValue " +
                    "   FROM conf_grading_details as cgd    " +
                    "       JOIN conf_score_subjects as css ON cgd.parent_code = css.code" +
                    "       JOIN scoreboard_conf as scf ON scf.code = css.parent_code  " +
                    "       JOIN subjects as s ON css.subject_code = s.code     " +
                    "       JOIN subject_class as sc ON s.id = sc.subject_id       " +
                    "       JOIN class_room as cl ON cl.id=sc.class_id      " +
                    "       JOIN student_history as sh ON (sh.current_class_code = cl.code  AND sh.year = scf.year )" +
                    "       JOIN students as st ON sh.student_code = st.code " +
                    "   WHERE UPPER(cl.code) = BINARY UPPER(?1)" +
                    "       AND UPPER(s.code) = BINARY UPPER(?2)" +
                    "       AND scf.year =?3 " +
                    "       AND (st.status = ?4 OR st.status = ?5) AND css.semester = ?6" +
                    "   ) as tb1        " +
                    "LEFT JOIN         " +
                    "   (SELECT DISTINCT" +
                    "       student.id as idStudent," +
                    "       gsd.id as gsdId,  " +
                    "       gsd.code as gsdCode,    " +
                    "       gsd.parent_code as gsdParent," +
                    "       gsd.avg_score as gsdAvgScore,   " +
                    "       gsd.evaluate as gsdEvaluate," +
                    "       gsd.updater as gsdUpdater," +
                    "       gsd.evaluate_status as gsdStatus," +
                    "       gscore.id as gscoreId," +
                    "       gscore.code as gscoreCode," +
                    "       gscore.parent_code as gscoreParent," +
                    "       gscore.score_code as scoreCode ," +
                    "       gscore.times as gscoreTimes," +
                    "       gscore.value as gscoreValue," +
                    "       gscore.coefficient as gscoreCoefficient  " +
                    "   FROM gradebook as g   " +
                    "       LEFT JOIN gradebook_subjects_details as gsd ON g.code = gsd.parent_code " +
                    "       JOIN students as student ON student.code = gsd.student_code     " +
                    "       LEFT JOIN gradebook_score_details as gscore ON gscore.parent_code = gsd.code             " +
                    "   WHERE UPPER(g.class_code) = BINARY UPPER(?1)" +
                    "       AND UPPER(gsd.subject_code) = BINARY UPPER(?2)    " +
                    "       AND g.school_year =?3   " +
                    "       AND g.semester =?6" +
                    "   ) as tb2   " +
                    "ON case when tb2.scoreCode is not null then (tb1.studentId = tb2.idStudent AND tb1.cgdCode = tb2.scoreCode ) " +
                    "   else tb1.studentId = tb2.idStudent end       " +
                    "ORDER BY studentName ASC, studentCode ASC, cgdId ASC",
            nativeQuery = true
    )
    List<Object[]> getStudentListWithEachClassification(
            String classCode,
            String subjectCode,
            String years,
            Integer status1,
            Integer status2,
            String semester
    );

    @Query(nativeQuery = true, value = "select gb.school_year as year, gb.semester, gb.class_code as classCode, gsd.student_code as studentCode, sj.name as subjectName, sj.code as subjectCode, sj.sub_type as subType, case sj.sub_type \n" +
            "            when 0 then gsd.avg_score\n" +
            "\t\t\t\twhen 1 then (select t.value from (select d.student_code, gs.*, css.subject_code from gradebook as g \n" +
            "\t\t\t\tjoin gradebook_subjects_details as d  on g.code = d.parent_code \n" +
            "\t\t\t\tjoin gradebook_score_details as gs on d.code = gs.parent_code \n" +
            "\t\t\t\tjoin conf_grading_details cgf on cgf.code = gs.score_code  \n" +
            "\t\t\t\tjoin conf_score_subjects css on css.code = cgf.parent_code\n" +
            "\t\t\t\twhere (cgf.name like 'Xếp loại' or cgf.name like 'Điểm trung bình') \n" +
            "\t\t\t\tand d.student_code like :studentCode\n" +
            "\t\t\t\tand d.subject_code like sj.code\n" +
            "\t\t\t\tand g.school_year like :year\n" +
            "\t\t\t\tand g.semester = :semester\n" +
            "\t\t\t\tand g.class_code like :classCode) as t) end as avgScore, gsd.evaluate \n" +
            "from gradebook gb\n" +
            "join gradebook_subjects_details gsd on gb.code = gsd.parent_code\n" +
            "join subjects sj on gsd.subject_code = sj.code\n" +
            "where gb.school_year like :year and gb.semester = :semester and gb.class_code like :classCode and gsd.student_code like :studentCode")

    List<Map<String, Object>> getListScoreOfEachSubject(@Param("year") String year,
                                                        @Param("semester") Integer semester,
                                                        @Param("classCode") String classCode,
                                                        @Param("studentCode") String studentCode);

    @Query(nativeQuery = true, value = "SELECT  distinct studentId,studentCode,studentName,avgScore from\n" +
            "(select st.id as studentId, st.code as studentCode, st.full_name as studentName from subjects as s\n" +
            "JOIN subject_class as sc ON s.id = sc.subject_id \n" +
            "JOIN class_room as cl ON cl.id=sc.class_id  \n" +
            "JOIN student_history as sh ON (sh.current_class_code = cl.code)  \n" +
            "JOIN students as st ON sh.student_code = st.code  \n" +
            "WHERE UPPER(cl.code) =  BINARY UPPER(?1)  \n" +
            "AND UPPER(s.code) =  BINARY UPPER(?2)  \n" +
            "AND (st.status = 0 OR st.status = 1)  \n" +
            "AND sh.year=?3\n" +
            "ORDER BY st.id) as tb1\n" +
            "LEFT JOIN\n" +
            "(select student.id ,student.code, student.full_name,gscore.value  as avgScore from  gradebook as g  \n" +
            "LEFT JOIN gradebook_subjects_details as gsd ON g.code = gsd.parent_code \n" +
            "JOIN students as student ON student.code = gsd.student_code  \n" +
            "LEFT JOIN gradebook_score_details as gscore ON gscore.parent_code = gsd.code  \n" +
            "left join conf_grading_details cgd on gscore.score_code=cgd.code\n" +
            "WHERE UPPER(g.class_code) = BINARY UPPER(?1)  \n" +
            "  AND g.school_year =?3\n" +
            "AND g.semester =?4\n" +
            "and cgd.name='Điểm trung bình'\n" +
            "AND UPPER(gsd.subject_code) = BINARY UPPER(?2)) as tb2 ON tb1.studentId=tb2.id")
    List<Map<String, Object>> getForSemester0Grade( String classCode, String subjectCode, String years,Integer semester);
    @Query(nativeQuery = true, value = "SELECT  distinct studentId,studentCode,studentName,avgScore from\n" +
            "(select st.id as studentId, st.code as studentCode, st.full_name as studentName from subjects as s\n" +
            "JOIN subject_class as sc ON s.id = sc.subject_id \n" +
            "JOIN class_room as cl ON cl.id=sc.class_id  \n" +
            "JOIN student_history as sh ON (sh.current_class_code = cl.code)  \n" +
            "JOIN students as st ON sh.student_code = st.code  \n" +
            "WHERE UPPER(cl.code) =  BINARY UPPER(?1)  \n" +
            "AND UPPER(s.code) =  BINARY UPPER(?2)  \n" +
            "AND (st.status = 0 OR st.status = 1)  \n" +
            "AND sh.year=?3\n" +
            "ORDER BY st.id) as tb1\n" +
            "LEFT JOIN\n" +
            "(select student.id ,student.code, student.full_name,gsd.avg_score  as avgScore from  gradebook as g  \n" +
            "LEFT JOIN gradebook_subjects_details as gsd ON g.code = gsd.parent_code \n" +
            "JOIN students as student ON student.code = gsd.student_code  \n" +
            "WHERE UPPER(g.class_code) = BINARY UPPER(?1)  \n" +
            "    AND g.school_year =?3\n" +
            "AND g.semester =0\n" +
            "AND UPPER(gsd.subject_code) = BINARY UPPER(?2)) as tb2 ON tb1.studentId=tb2.id;")
    List<Map<String, Object>> getForSemester0Score( String classCode, String subjectCode, String years);

        //  man danh gia hoc luc
        @Query(nativeQuery = true, value = "select code from gradebook where school_year = ?2 and class_code = ?1 and semester = ?3" )
        List<Map<String, Object>> getCodeByClassYearSemester(String classCode, String year, Integer semester);

        @Query(nativeQuery = true, value = "SELECT a.student_code, a.academic_ability FROM gradebook g join academic_ability a on g.code = a.parent_code where school_year = ?2 and class_code = ?1 and semester = ?3" )
        List<Map<String, Object>> getStudentRateCodeByClassYearSemester(String classCode, String year, Integer semester);

        @Query(nativeQuery = true, value = "select * from academic_ability a where a.parent_code =?1  and a.student_code =?2 " )
        List<Map<String, Object>> getAcademicAbility(String parentCode, String studentCode);

        @Modifying
        @Query(nativeQuery = true, value="update academic_ability a set a.academic_ability = ?1 where a.student_code = ?2 and a.parent_code = ?3")
        void updateAcademicAbility(String academicAbility, String studentCode, String parentCode);

        @Query(nativeQuery = true, value = "SELECT IF(count(*), 'true', 'false') FROM gradebook where school_year like :year and semester = :semester and class_code like :classCode\n")
        Boolean checkExistGradeBook(@Param("year") String year,
                                          @Param("semester") Integer semester,
                                          @Param("classCode") String classCode);

        @Query(nativeQuery = true, value = " SELECT IF(count(g.id), 'true', 'false') from gradebook g join gradebook_subjects_details gsd on g.code = gsd.parent_code and gsd.student_code= :studentCode and g.school_year= :year join gradebook_score_details gscd on gscd.parent_code = gsd.code\n")
        Boolean checkExistScoreToChangeClass(@Param("year") String year,
                                          @Param("studentCode") String studentCode
                                          );
}