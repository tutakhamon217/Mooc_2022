package fpt.capstone.repository;

import fpt.capstone.entities.SchoolYear;
import fpt.capstone.entities.TeachingAssignment;
import fpt.capstone.vo.TimeTableDropDown;
import fpt.capstone.vo.TimeTableVo;
import fpt.capstone.vo.TimeTableVoV2;
import fpt.capstone.vo.TimetableExcel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface TeachingAssignmentRepository extends JpaRepository<TeachingAssignment, Integer> {

    @Query(nativeQuery = true, value ="select t.code, ta.subject_code as subjectCode,ta.class_code as classCode, case when(select count(*) from gradebook g join gradebook_subjects_details gsd on g.code=gsd.parent_code where gsd.subject_code=s.code and g.class_code=cr.code>0) then 0 else 1 end as canDelete," +
            "ta.id,gl.name as grade, cr.name,s.name as subjectName, s.id as subjectId, t.dept_id as deptId,t.id as teacherId,t.full_name as teacherName, ta.apply_all_semester,ta.semester_1,ta.semester_2 \n" +
            "from teaching_assignment ta join teachers t on ta.teacher_code=t.code join subjects s on ta.subject_code=s.code join \n" +
            "class_room cr on ta.class_code =cr.code join grade_level gl on cr.grade_level=gl.id where \n" +
            "((?1 is null or upper(t.code) Like UPPER('%' ?1 '%')) or (?1 is null or upper(t.full_name)Like UPPER('%' ?1 '%'))) \n" +
            "and((?2 is null or upper(s.code) Like UPPER('%' ?2 '%')) or (?2 is null or upper(s.name)Like UPPER('%' ?2 '%'))) and cr.id=?3")
    List<Map<String, Object>> getAllTeachingAssignmentSearching(String nameCodeTeacher, String nameCodeSubject, int classId);

    List<TeachingAssignment> findBySubjectCodeAndClassCode(String SubjectCode, String ClassCode);

    @Query("SELECT new fpt.capstone.vo.TimetableExcel(wd.day, ssd.lession, wd.type ,s.code, s.name, t.code, t.fullName) FROM ScheduleTimetable st LEFT JOIN ScheduleGradeClass sgc ON st.code = sgc.parentCode LEFT JOIN WeekDetails wd ON wd.parentCode = sgc.code LEFT JOIN ScheduleSubjectDetails ssd ON ssd.parentCode = wd.code LEFT JOIN TeachingAssignment ta ON ta.subjectCode = ssd.subjectCode JOIN Teacher t ON t.code = ta.teacherCode JOIN Subjects s ON s.code = ta.subjectCode WHERE st.schoolYear = :year AND st.semester = :semester  AND sgc.gradeLevel = :gradeLevel AND sgc.classCode = :className AND sgc.applyDate = :applyDate AND ta.year = :year AND (((st.semester = '1') AND (ta.semester1 = 1)) OR ((st.semester = '2') AND (ta.semester2 = 1)) ) AND ta.classCode = :className")
    List<TimetableExcel> getTimetableExcel(@Param("year") String year, @Param("semester") String semester, @Param("gradeLevel") String gradeLevel, @Param("className") String className, @Param("applyDate") LocalDate applyDate);

    @Query("SELECT new fpt.capstone.vo.TimeTableDropDown(s.id, s.name, t.id, t.fullName) FROM TeachingAssignment ta JOIN Teacher t ON t.code = ta.teacherCode JOIN Subjects s ON s.code = ta.subjectCode WHERE ta.year = :year AND (((:semester = '1') AND (ta.semester1 = 1)) OR ((:semester = '2') AND (ta.semester2 = 1))) AND ta.classCode = :className")
    List<TimeTableDropDown> getAssigmentDropdown(@Param("year") String year, @Param("semester") String semester, @Param("className") String className);

    @Query("SELECT COUNT(ta) > 0 FROM TeachingAssignment ta JOIN Teacher t ON t.code = ta.teacherCode JOIN Subjects s ON s.code = ta.subjectCode WHERE ta.year = :year AND (((:semester = '1') AND (ta.semester1 = 1)) OR ((:semester = '2') AND (ta.semester2 = 1))) AND ta.classCode = :className AND ta.teacherCode = :teacherCode AND ta.subjectCode = :subjectCode")
    Boolean isAssigned(@Param("year") String year, @Param("semester") String semester, @Param("className") String className, @Param("subjectCode") String subjectCode, @Param("teacherCode") String teacherCode);
    @Query(nativeQuery = true, value = "select ta.id,ta.class_code,cr.name as class_name,ta.subject_code,s.name as subject_name from  teaching_assignment ta join subjects s on ta.subject_code=s.code join class_room cr on ta.class_code=cr.code" +
            " where year=?2 and semester_1=?3 and teacher_code=?1")
    List<Map<String, Object>> getClassSubjectByYearAndSemesterAndTeacherCode(String teacherCode, String year, Integer semester);

    @Query(nativeQuery = true, value = " select distinct ta.class_code,cr.name from teaching_assignment ta join class_room cr on ta.class_code=cr.code where teacher_code=?1 and years=?2 and semester_1=1 ")
    List<Map<String, Object>> getClassRoomByTeacherCodeAndYearsAndSemester1(String teacherCode, String year);

    @Query(nativeQuery = true, value = " select distinct ta.class_code,cr.name from teaching_assignment ta join class_room cr on ta.class_code=cr.code where teacher_code=?1 and years=?2 and semester_2=1")
    List<Map<String, Object>> getClassRoomByTeacherCodeAndYearsAndSemester2(String teacherCode, String year);

    @Query(nativeQuery = true, value = "select ta.subject_code,s.name from teaching_assignment ta join subjects s on ta.subject_code=s.code where teacher_code=?1 and year=?2  and class_code=?3  and (semester_1=1 or semester_2=1)")
    List<Map<String, Object>> getSubjectByTeacherCodeAndYearsAndClassCode(String teacherCode, String year, String classCode);

    @Query(nativeQuery = true, value = "select ta.subject_code,s.name from teaching_assignment ta join subjects s on ta.subject_code=s.code where teacher_code=?1 and year=?2  and class_code=?3  and semester_2=1")
    List<Map<String, Object>> getSubjectByTeacherCodeAndYearsAndClassCodeAndSemester2(String teacherCode, String year, String classCode);
    
    @Query(nativeQuery = true, value = "SELECT sgc.apply_date AS applyDate, wd.day AS weekDay, ssd.lesson AS lesson, wd.type AS type, s.id AS subjectID, s.name AS subjectName, s.code AS subjectCode, t.id AS teacherID, t.full_name AS teacherName, t.code AS teacherCode FROM schedule_timetable st JOIN schedule_grade_class sgc ON st.code = sgc.parent_code JOIN week_details wd ON sgc.code = wd.parent_code JOIN schedule_subjects_details ssd ON wd.code = ssd.parent_code JOIN class_room cr ON cr.code = sgc.class_code LEFT JOIN subjects s ON ssd.subject_code = s.code LEFT JOIN subject_class sc ON sc.class_id = cr.id AND sc.subject_id = s.id AND ((st.semester = 1 AND sc.flg_semester1 = '1') OR (st.semester = 2 AND sc.flg_semester2 = '1'))  LEFT JOIN teaching_assignment ta ON ta.class_code = cr.code AND ((st.semester = 1 AND ta.semester_1 = '1') OR (st.semester = 2 AND ta.semester_2 = '1')) AND ta.subject_code = s.code LEFT JOIN teachers t ON ta.teacher_code = t.code WHERE sgc.code = ?1")
    List<Map<String, Object>> getLatestTimetable(String code);

    // @Query("SELECT new fpt.capstone.vo.TimeTableVoV2(sgc.applyDate, wd.day, ssd.lession, wd.type ,s.id, s.name, s.code, t.id, t.fullName, t.code) FROM ScheduleTimetable st JOIN ScheduleGradeClass sgc ON st.code = sgc.parentCode JOIN WeekDetails wd ON sgc.code = wd.parentCode JOIN ScheduleSubjectDetails ssd ON wd.code = ssd.parentCode LEFT JOIN TeachingAssignment ta ON ssd.subjectCode = ta.subjectCode AND ((st.semester = '1' AND ta.semester1 = '1') OR (st.semester = '2' AND ta.semester2 = '1')) AND ta.classCode = sgc.classCode LEFT JOIN Teacher t ON ta.teacherCode = t.code LEFT JOIN Subjects s ON s.code = ta.subjectCode WHERE sgc.code = :code")
    // List<TimeTableVoV2> getLatestTimetable(@Param("code") String code);

    @Query("select count(*)>0 from TeachingAssignment where subjectCode=?1 and classCode=?2 and teacherCode=?3 and semester1=1")
    Boolean isTeachingforSemester1(String subjectCode,String classCode,String teacherCode);

    @Query("select count(*)>0 from TeachingAssignment where subjectCode=?1 and classCode=?2 and teacherCode=?3 and semester2=1")
    Boolean isTeachingforSemester2(String subjectCode,String classCode,String teacherCode);

    //@Query(nativeQuery = true, value = "SELECT DISTINCT class_code FROM teaching_assignment WHERE teacher_code = ?3 AND year = ?1 AND ((?2 = '1' AND semester_1 = '1') OR (?2 = '2' AND semester_2 = '1'))")
    @Query(nativeQuery = true, value = "SELECT DISTINCT ta.class_code AS class_code, cr.grade_level AS grade_level FROM teaching_assignment ta JOIN class_room cr ON ta.class_code = cr.code AND ta.year = cr.years WHERE ta.teacher_code = ?3 AND ta.year = ?1 AND ((?2 = '1' AND ta.semester_1 = '1') OR (?2 = '2' AND ta.semester_2 = '1'))")
    List<Map<String, Object>> getTeachingClassCodes(String year, String semester, String teacherCode);

}
