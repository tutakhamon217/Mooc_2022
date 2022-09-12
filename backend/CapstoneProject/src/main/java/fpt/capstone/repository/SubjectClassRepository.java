package fpt.capstone.repository;

import fpt.capstone.entities.SubjectClass;
import fpt.capstone.vo.TimeTableDropDown;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface SubjectClassRepository extends JpaRepository<SubjectClass, Integer> {
    @Query(nativeQuery = true, value = "(select \n" +
            "case when(select count(*) from gradebook g join gradebook_subjects_details gsd on g.code=gsd.parent_code where gsd.subject_code=s.code and g.class_code=cr.code>0) then 1 else 0 end as marked,\n" +
            "case when (select count(*) from schedule_timetable st join schedule_grade_class sgc on st.code=sgc.parent_code join week_details wd on sgc.code=wd.parent_code join schedule_subjects_details ssd\n" +
            "on wd.code=ssd.parent_code where st.school_year=cr.years and sgc.class_code=cr.code and ssd.subject_code=s.code >0 ) then 1 else 0 end as scheduled,\n" +
            "case when (select count(*) from teaching_assignment where subject_code=s.code and  class_code=cr.code and year=cr.years >0) then 1 else 0 end as teachingAssignment\n" +
            ",CASE WHEN sc.flg_semester1 = 1 or sc.flg_semester2 = 1 THEN 1 ELSE 0 END as study, \n" +
            "sc.coefficient, sc.flg_semester1,sc.flg_semester2,s.name as subjectName, s.id as subjectId, sc.id as subjectClassId from class_room cr \n" +
            "join subject_class sc on cr.id=sc.class_id join subjects s on sc.subject_id=s.id join subject_dept sd on s.id=sd.subject_id \n" +
            "where (?1 is null or cr.id=?1 )and(?2 is null or (upper(s.code) Like UPPER('%' ?2 '%') or upper(s.name)Like UPPER('%' ?2 '%')))and (?3 is null or s.grade_level=?3) and (?4 is null or sd.dept_id=?4)) \n" +
            "union all \n" +
            "(select  distinct null as marked, null as scheduled, null as teachingAssignment,null as study,null as coefficient, null as flg_semester1,null as flg_semester2, s.name as subjectName,s.id as subjectId ,null as subjectClassId\n" +
            "from subjects s left join subject_class sc on s.id=sc.subject_id join subject_dept sd on s.id=sd.subject_id\n" +
            "where (?1 is null or sc.class_id!=?1 or sc.class_id is null) and ( ?2 is null or (upper(s.code) Like UPPER('%' ?2 '%') or upper(s.name)Like UPPER('%' ?2 '%')))and (?3 is null or s.grade_level=?3) and (?4 is null or sd.dept_id=?4) AND s.id NOT IN (select \n" +
            "s.id as subjectId from class_room cr\n" +
            "join subject_class sc on cr.id=sc.class_id join subjects s on sc.subject_id=s.id join subject_dept sd on s.id=sd.subject_id \n" +
            "where (?1 is null or cr.id=?1 )and(?2 is null or (upper(s.code) Like UPPER('%' ?2 '%') or upper(s.name)Like UPPER('%' ?2 '%')))and (?3 is null or s.grade_level=?3) and (?4 is null or sd.dept_id=?4)));")
    List<Map<String, Object>> getAllSubjectClassPagingAndSearching(Integer classId, String nameCode, Integer gradeLevel, Integer deptId);

    @Override
    SubjectClass getById(Integer integer);

    @Override
    <S extends SubjectClass> S save(S entity);

    //        @Query(value = "select new fpt.capstone.entities.SubjectClass(sc.id,sc.classId,sc.subjectId,sc.coefficient,sc.flgSemester1,sc.flgSemester2,sc.createdTime,sc.createdName,sc.updateTime,sc.updateName) from SubjectClass sc join ClassRo cr on sc.classId=cr.id join Subject s on sc.subjectId=s.id where cr.code=?1 and s.code=?2")
//    @Query(nativeQuery = true, value = "select sc.* from subject_class sc join class_room cr on sc.class_id=cr.id join subjects s on sc.subject_id=s.id  where cr.code=?1 and s.code=?2")
    @Query("select sc from SubjectClass sc JOIN ClassRoom cr on sc.classId=cr.id join Subjects s on sc.subjectId=s.id where cr.code=?1 and s.code=?2")
    SubjectClass findByClassCodeAndSubjectCode(String classCode, String subjectCode);

    //    select sc from subject_class sc join class_room cr on sc.class_id=cr.id join subjects s on sc.subject_id=s.id  where cr.code=?1 and s.code=?2
    @Query(nativeQuery = true, value = "select sc.* from subject_class sc join class_room cr on sc.class_id=cr.id join subjects s on sc.subject_id=s.id  where cr.code=?1 and s.code=?2 and sc.flg_semester1=1")
    Optional<SubjectClass> findByClassCodeAndSubjectCodeSemester1(String classCode, String subjectCode);

    @Query(nativeQuery = true, value = "select sc.* from subject_class sc join class_room cr on sc.class_id=cr.id join subjects s on sc.subject_id=s.id  where cr.code=?1 and s.code=?2 and sc.flg_semester2=1")
    Optional<SubjectClass> findByClassCodeAndSubjectCodeSemester2(String classCode, String subjectCode);
//    @Query(value =
//            "SELECT a.* FROM gradebook_subjects_details as a " +
//                    "RIGHT JOIN gradebook as b ON a.parent_code=b.code" +
//                    " WHERE UPPER(a.student_code) = BINARY UPPER(?1) " +
//                    "AND UPPER(a.subject_code) = BINARY UPPER(?2) " +
//                    "AND b.school_year =?3", nativeQuery = true)
//    List<GradebookSubjectsDetails> getByStudentCodeAndSubjectCode(String studentCode, String subjectCode, String schoolYear);

    @Query(nativeQuery = true, value = "select sc.subject_id, s.code, sc.coefficient, s.name from subject_class sc join subjects s on sc.subject_id = s.id where sc.class_id=?1")
    List<Map<String, Object>> findByClassIdAndSubjectId(Integer classId);

    @Query(nativeQuery = true, value = "select sc.subject_id, s.code, sc.coefficient, s.name, s.sub_type from subject_class sc join subjects s on sc.subject_id = s.id where sc.class_id=?1 and sc.flg_semester1=?2 and sc.flg_semester2=?3")
    List<Map<String, Object>> findByClassIdAndSubjectIdSemester(Integer classId, Integer semester1, Integer semester2);

    //@Query(nativeQuery = true, value = "SELECT s.id, s.name, t.id, t.full_name FROM subject_class sc JOIN class_room cr ON sc.class_id = cr.id JOIN subjects s ON s.id = sc.subject_id LEFT JOIN teaching_assignment ta ON ta.subject_code = s.code AND ta.class_code = cr.code AND ((('2' = '1' AND ta.semester_1 = 1) OR ('2' = '2' AND ta.semester_2 = 1))) LEFT JOIN teachers t ON ta.teacher_code = t.code WHERE cr.code = 'L6B'  AND cr.years = '2021-2022'  AND (('2' = '1' AND sc.flg_semester1 = 1) OR ('2' = '2' AND sc.flg_semester2 = 1))")
    @Query("SELECT new fpt.capstone.vo.TimeTableDropDown(s.id, s.name, s.code, t.id, t.fullName, t.code) FROM SubjectClass sc JOIN ClassRoom cr ON sc.classId = cr.id JOIN Subjects s ON s.id = sc.subjectId LEFT JOIN TeachingAssignment ta ON ta.subjectCode = s.code AND ta.classCode = cr.code AND (((:semester = '1' AND ta.semester1 = 1) OR (:semester = '2' AND ta.semester2 = 1))) LEFT JOIN Teacher t ON ta.teacherCode = t.code WHERE cr.code = :classCode  AND cr.years = :years  AND ((:semester = '1' AND sc.flgSemester1 = 1) OR (:semester = '2' AND sc.flgSemester2 = 1))")
    List<TimeTableDropDown> getTimeTableDropdown(@Param("years") String years, @Param("semester") String semester, @Param("classCode") String classCode);

    @Query("SELECT COUNT(sc) > 0 FROM SubjectClass sc JOIN ClassRoom cr ON sc.classId = cr.id JOIN Subjects s ON s.id = sc.subjectId LEFT JOIN TeachingAssignment ta ON ta.subjectCode = s.code AND ta.classCode = cr.code AND (((:semester = '1' AND ta.semester1 = 1) OR (:semester = '2' AND ta.semester2 = 1))) LEFT JOIN Teacher t ON ta.teacherCode = t.code WHERE cr.code = :classCode  AND cr.years = :years  AND ((:semester = '1' AND sc.flgSemester1 = 1) OR (:semester = '2' AND sc.flgSemester2 = 1)) AND s.code = :subjectCode")
    Boolean isValidForTimetable(@Param("years") String years, @Param("semester") String semester, @Param("classCode") String classCode, @Param("subjectCode") String subjectCode);
}
    