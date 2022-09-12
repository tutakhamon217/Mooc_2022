package fpt.capstone.repository;

import fpt.capstone.entities.AssessStudentConductDetail;
import fpt.capstone.entities.ConfigGradeDetail;
import fpt.capstone.entities.ServiceResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface AssessStudentConductDetailRepository extends JpaRepository<AssessStudentConductDetail, Integer> {
    @Query(nativeQuery = true, value = "select b.*, sum(case a.check_date when 'K' then 1 else 0 end) as number_off, sum(case a.check_date when 'P' then 1 else 0 end) as number_off_allowed from (SELECT asd.* FROM attendance_student as ats \n" +
            "            join attendance_student_details asd on asd.parent_code = ats.code\n" +
            "            where ats.shool_year like :year) as a\n" +
            "            right join ( select x.*, y.id, (case y.evaluate when null then 0 else y.evaluate end) as evaluate, y.conduct, y.competition_title, y.parent_code from \n" +
            "            (select s.full_name, s.code as student_code, aa.academic_ability\n" +
            "            from students s\n" +
            "            join student_history sh on s.code = sh.student_code\n" +
            "            join class_room cr on cr.code = sh.current_class_code\n" +
            "            left join gradebook gb on gb.class_code = cr.code and gb.school_year = sh.year\n" +
            "            left join gradebook_subjects_details gsd on gsd.parent_code = gb.code and gsd.student_code = s.code\n" +
            "            left join academic_ability aa on aa.parent_code = gb.code and s.code = aa.student_code\n" +
            "            where s.status in (0, 1) and sh.current_class_code like :classCode and sh.year like :year and gb.semester = :semester\n" +
            "            group by student_code) as x\n" +
            "            left join (select ascd.id, s.full_name, s.code as student_code, sum(case gsd.evaluate_status when 1 then 1 else 0 end) as evaluate , ascd.conduct, ascd.competition_title, ascd.parent_code\n" +
            "            from students s\n" +
            "            join student_history sh on s.code = sh.student_code\n" +
            "            join class_room cr on cr.code = sh.current_class_code\n" +
            "            left join gradebook gb on gb.class_code = cr.code and gb.school_year = sh.year\n" +
            "            left join gradebook_subjects_details gsd on gsd.parent_code = gb.code and gsd.student_code = s.code\n" +
            "            left join assess_student_conduct astc on astc.class_code = gb.class_code and astc.semester = gb.semester and astc.school_year = gb.school_year\n" +
            "            left join assess_student_conduct_details ascd on astc.code = ascd.parent_code and ascd.student_code = s.code\n" +
            "            where s.status in (0, 1) and sh.current_class_code like :classCode and sh.year like :year and gb.semester = :semester\n" +
            "            group by student_code) as y\n" +
            "                            on x.student_code = y.student_code\n" +
            "                        ) as b\n" +
            "            on b.student_code = a.student_code\n" +
            "            group by b.student_code")
    List<Map<String, Object>> getInfoConductOfClass(@Param("semester") Integer semester,
                                                    @Param("year") String year,

                                                    @Param("classCode") String class_code);

    @Query(nativeQuery = true, value = "select sj.code,gsd.id, s.code as student_code, s.full_name, sj.name, gb.semester, t.full_name as teacher_name, ta.teacher_code, gsd.evaluate \n" +
            "from gradebook gb\n" +
            "join gradebook_subjects_details gsd on gb.code = gsd.parent_code\n" +
            "join students s on s.code = gsd.student_code\n" +
            "join subjects sj on sj.code = gsd.subject_code\n" +
            "join teaching_assignment ta on ta.subject_code = sj.code and ta.class_code = gb.class_code\n" +
            "join teachers t on t.code = ta.teacher_code\n" +
            "where (:semester = 0 or gb.semester = :semester) and gb.school_year like :year  and gb.class_code like :class_code and s.code like :student_code and gsd.evaluate_status = 1\n" +
            "order by gb.semester asc")
    List<Map<String, Object>> getEvaluateOfTeacher(@Param("semester") Integer semester,
                                                    @Param("year") String year,
                                                    @Param("student_code") String student_code,
                                                    @Param("class_code") String class_code);

    @Override
    <S extends AssessStudentConductDetail> List<S> saveAll(Iterable<S> entities);

    @Query(nativeQuery = true, value = "select a.*, (case when b.semester is null then :semester else b.semester end) as semester, (case when b.excellent is null then 0 else b.excellent end) as excellent, (case when b.good is null then 0 else b.good end) as good, (case when b.medium is null then 0 else b.medium end) as medium, (case when b.weak is null then 0 else b.weak end) as weak from \n" +
            "(SELECT cr.grade_level as gradeLevel, d.id as deptId, d.name as deptName, cr.name as className, cr.code as classCode, t.full_name as teacherName FROM class_room cr \n" +
            "join departments d on cr.dept_id = d.id\n" +
            "join teachers t on cr.teacher_id = t.id\n" +
            "where (:grade_level is null or cr.grade_level = :grade_level) and (:dept_id is null or d.id = :dept_id) and (:class_code is null or cr.code = :class_code) and cr.years like :year)\n" +
            "as a\n" +
            "left join (SELECT  gl.id as gradeLevel, asconduct.semester, d.id as deptId, d.name as deptName, cr.name as className, cr.code as classCode, t.full_name as teacherName,\n" +
            "            SUM(if(ascd.competition_title = 'excellent', 1, 0)) as excellent,\n" +
            "            SUM(if(ascd.competition_title = 'good', 1, 0)) as good,\n" +
            "            SUM(if(ascd.competition_title = 'medium', 1, 0)) as medium,\n" +
            "            SUM(if(ascd.competition_title = 'weak', 1, 0)) as weak\n" +
            "            FROM class_room cr\n" +
            "            join teachers t on cr.teacher_id = t.id\n" +
            "            join grade_level gl on cr.grade_level = gl.id\n" +
            "            join departments d on cr.dept_id = d.id\n" +
            "            join student_history sh on cr.code = sh.current_class_code\n" +
            "            join students s on s.code = sh.student_code\n" +
            "            join assess_student_conduct_details ascd on ascd.student_code = sh.student_code\n" +
            "            join assess_student_conduct asconduct on asconduct.class_code = cr.code and asconduct.code = ascd.parent_code\n" +
            "            where cr.years like :year and sh.year like :year and s.status in (0, 1) and asconduct.semester = :semester\n" +
            "            and (:dept_id is null or d.id = :dept_id) and (:grade_level is null or gl.id = :grade_level) and (:class_code is null or cr.code like :class_code)\n" +
            "            group by cr.id\n" +
            "            order by cr.dept_id) \n" +
            "as b on a.classCode = b.classCode order by a.deptId asc\n" +
            "\n")
    List<Map<String, Object>> getReportTheResultOfCompetition(
            @Param("grade_level") Integer gradeLevel,
            @Param("dept_id") Integer deptId,
            @Param("semester") Integer semester,
            @Param("class_code") String classCode,
            @Param("year") String year
    );

    @Query(nativeQuery = true, value = "select x.studentCode, x.coefficient, x.studentName, x.subjectName, y.avg_score, y.academic_ability, y.conduct, y.number_off, y.number_off_allowed, y.competition_title  from (\n" +
            "\tSELECT subject_id, s.name, s.name as subjectName, st.full_name as studentName ,sh.student_code as studentCode, sc.coefficient FROM subject_class sc\n" +
            "\tjoin class_room cr on cr.id = sc.class_id\n" +
            "\tjoin subjects s on s.id = sc.subject_id\n" +
            "\tjoin student_history sh on sh.current_class_code = cr.code\n" +
            "\tjoin students st on st.code = sh.student_code\n" +
            "\twhere sh.year like :year and sh.current_class_code like :classCode and st.status in (0, 1)\n" +
            ") as x\n" +
            "left join (\n" +
            "\tselect b.subjectId, a.code as studentCode, a.full_name as studentName, b.subjectName,b.coefficient, b.avg_score, b.academic_ability, b.conduct, b.number_off, b.number_off_allowed, b.competition_title  from (\n" +
            "\t\tselect s.* from students s\n" +
            "\t\tjoin student_history sh on s.code = sh.student_code\n" +
            "\t\twhere sh.current_class_code like :classCode and s.status in (0, 1) and sh.year like :year\n" +
            "\t) as a\n" +
            "\tleft join (\n" +
            "\tselect distinct sj.id as subjectId, gsd.student_code as studentCode, sc.coefficient, s.full_name as studentName, sj.name as subjectName, case sj.sub_type \n" +
            "\t\t\t\twhen 0 then gsd.avg_score\n" +
            "\t\t\t\twhen 1 then (select t.value from (select d.student_code, gs.*, css.subject_code from gradebook as g \n" +
            "\t\t\t\tjoin gradebook_subjects_details as d  on g.code = d.parent_code \n" +
            "\t\t\t\tjoin gradebook_score_details as gs on d.code = gs.parent_code \n" +
            "\t\t\t\tjoin conf_grading_details cgf on cgf.code = gs.score_code  \n" +
            "\t\t\t\tjoin conf_score_subjects css on css.code = cgf.parent_code\n" +
            "\t\t\t\twhere (cgf.name like 'Xếp loại' or cgf.name like 'Điểm trung bình') \n" +
            "\t\t\t\tand d.student_code like studentCode\n" +
            "\t\t\t\t\tand d.subject_code like sj.code\n" +
            "\t\t\t\tand g.school_year like :year\n" +
            "\t\t\t\tand g.semester = :semester\n" +
            "\t\t\t\tand g.class_code like :classCode) as t)\n" +
            "\t\t\t\tend as avg_score, ascd.academic_ability, ascd.conduct, ascd.number_off, ascd.number_off_allowed, ascd.competition_title\n" +
            "\t\t\t\tfrom student_history sh\n" +
            "\t\t\t\tjoin students s on sh.student_code = s.code\n" +
            "\t\t\t\tjoin gradebook gb on sh.current_class_code = gb.class_code\n" +
            "\t\t\t\tjoin gradebook_subjects_details gsd on gsd.parent_code = gb.code and gsd.student_code = s.code\n" +
            "\t\t\t\tjoin subjects sj on sj.code = gsd.subject_code and gsd.subject_code = sj.code\n" +
            "\t\t\t\tjoin assess_student_conduct astc on astc.class_code = sh.current_class_code\n" +
            "\t\t\t\tjoin assess_student_conduct_details ascd on astc.code = ascd.parent_code and ascd.student_code = s.code\n" +
            "\t\t\t\tjoin class_room cr on cr.code = gb.class_code\n" +
            "\t\t\t\tjoin subject_class sc on sc.subject_id = sj.id and sc.class_id = cr.id\n" +
            "\t\t\t\twhere sh.year like :year and gb.school_year like :year and sh.current_class_code like :classCode and s.status in (0, 1) and gb.semester = :semester and astc.semester = :semester\n" +
            "\t\t\t\torder by subjectName asc, studentCode asc\n" +
            "\t) as b\n" +
            "\ton a.code = b.studentCode\n" +
            ") as y\n" +
            "on x.subject_id = y.subjectId and x.studentCode = y.studentCode \n" +
            "order by x.studentCode asc\n" +
            "\n")
    List<Map<String, Object>> getReportTheResultOfEachClass(@Param("year") String year,
                                                            @Param("classCode") String classCode,
                                                            @Param("semester") Integer semester
                                                            );
    @Query(nativeQuery = true, value = "select b.*, sum(case a.check_date when 'K' then 1 else 0 end) as number_off, sum(case a.check_date when 'P' then 1 else 0 end) as number_off_allowed from (SELECT asd.* FROM attendance_student as ats \n" +
            "join attendance_student_details asd on asd.parent_code = ats.code\n" +
            "where ats.shool_year like :year and semester = :semester) as a\n" +
            "right join (select s.full_name, s.code as student_code, SUM(case gsd.evaluate_status when 1 then 1 else 0 end) as evaluate, aa.academic_ability\n" +
            "            from students s\n" +
            "            join student_history sh on s.code = sh.student_code\n" +
            "            join class_room cr on cr.code = sh.current_class_code\n" +
            "\t\t\tleft join gradebook gb on gb.class_code = cr.code and gb.school_year = sh.year\n" +
            "            left join gradebook_subjects_details gsd on gsd.parent_code = gb.code and gsd.student_code = s.code\n" +
            "            left join academic_ability aa on aa.parent_code = gb.code and s.code = aa.student_code\n" +
            "            where s.status in (0, 1) and sh.current_class_code like :classCode and sh.year like :year and gb.semester = :semester\n" +
            "            group by student_code) as b\n" +
            "on b.student_code = a.student_code\n" +
            "group by b.student_code\n")
    List<Map<String, Object>> getInfoConductOfClassNotYet(@Param("classCode") String classCode,
                                                          @Param("year") String year,
                                                          @Param("semester") Integer semester);

    @Query(nativeQuery = true, value = "select b.*, sum(case a.check_date when 'K' then 1 else 0 end) as number_off, sum(case a.check_date when 'P' then 1 else 0 end) as number_off_allowed from (SELECT asd.* FROM attendance_student as ats \n" +
            "            join attendance_student_details asd on asd.parent_code = ats.code\n" +
            "            where ats.shool_year like :year) as a\n" +
            "            right join ( select x.*, y.id, (case y.evaluate when null then 0 else y.evaluate end) as evaluate, y.conduct, y.competition_title, y.parent_code from \n" +
            "            (select s.full_name, s.code as student_code, aa.academic_ability\n" +
            "            from students s\n" +
            "            join student_history sh on s.code = sh.student_code\n" +
            "            join class_room cr on cr.code = sh.current_class_code\n" +
            "            left join gradebook gb on gb.class_code = cr.code and gb.school_year = sh.year\n" +
            "            left join gradebook_subjects_details gsd on gsd.parent_code = gb.code and gsd.student_code = s.code\n" +
            "            left join academic_ability aa on aa.parent_code = gb.code and s.code = aa.student_code\n" +
            "            where s.status in (0, 1) and sh.current_class_code like :classCode and sh.year like :year and gb.semester = 0\n" +
            "            group by student_code) as x\n" +
            "            left join (select ascd.id, s.full_name, s.code as student_code, (SELECT sum(case gsd.evaluate_status when 1 then 1 else 0 end)  as totalEva FROM assess_student_conduct asco\n" +
            "\t\t\t\tright join gradebook gb on gb.school_year = asco.school_year and gb.semester = asco.semester and gb.class_code = asco.class_code\n" +
            "\t\t\t\tright join gradebook_subjects_details gsd on gsd.parent_code = gb.code\n" +
            "\t\t\t\twhere (gb.semester = 1 or gb.semester = 2) and  gb.class_code like :classCode and gb.school_year like :year and gsd.student_code like s.code) as evaluate ,\n" +
            "\t\t\t ascd.conduct, ascd.competition_title, ascd.parent_code\n" +
            "            from students s\n" +
            "            join student_history sh on s.code = sh.student_code\n" +
            "            join class_room cr on cr.code = sh.current_class_code\n" +
            "            left join gradebook gb on gb.class_code = cr.code and gb.school_year = sh.year\n" +
            "            left join gradebook_subjects_details gsd on gsd.parent_code = gb.code and gsd.student_code = s.code\n" +
            "            left join assess_student_conduct astc on astc.class_code = gb.class_code and astc.semester = gb.semester and astc.school_year = gb.school_year\n" +
            "            left join assess_student_conduct_details ascd on astc.code = ascd.parent_code and ascd.student_code = s.code\n" +
            "            where s.status in (0, 1) and sh.current_class_code like :classCode and sh.year like :year and gb.semester = 0\n" +
            "            group by student_code) as y\n" +
            "                            on x.student_code = y.student_code\n" +
            "                        ) as b\n" +
            "            on b.student_code = a.student_code\n" +
            "            group by b.student_code")
    List<Map<String, Object>> getConductOfClassByAllStudyYear(@Param("year") String year,
                                                              @Param("classCode") String classCode
                                                              );

    @Query(nativeQuery = true, value = "select ascd.conduct, ascd.competition_title from assess_student_conduct asco\n" +
            "join assess_student_conduct_details ascd on asco.code = ascd.parent_code\n" +
            "where ascd.student_code like :studentCode and asco.semester = :semester and asco.school_year like :year")
    List<Map<String, Object>> getEvaluateAndCompetitionBySemester(@Param("studentCode") String studentCode,
                                                                  @Param("semester") Integer semester,
                                                                  @Param("year") String year);
}
