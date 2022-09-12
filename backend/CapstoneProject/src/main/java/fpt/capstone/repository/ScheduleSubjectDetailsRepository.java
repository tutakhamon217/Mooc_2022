package fpt.capstone.repository;

import fpt.capstone.entities.ScheduleSubjectDetails;
import fpt.capstone.vo.TimeTableVo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleSubjectDetailsRepository extends JpaRepository<ScheduleSubjectDetails, Integer> {
    @Query("SELECT new fpt.capstone.vo.TimeTableVo(wd.day, ssd.lession, wd.type ,ssd.id, ssd.subjectCode, wd.id, wd.code) FROM ScheduleTimetable st LEFT JOIN ScheduleGradeClass sgc ON st.code = sgc.parentCode LEFT JOIN WeekDetails wd ON wd.parentCode = sgc.code LEFT JOIN ScheduleSubjectDetails ssd ON ssd.parentCode = wd.code WHERE st.schoolYear = :year AND st.semester = :semester  AND sgc.gradeLevel = :gradeLevel AND sgc.classCode = :className AND sgc.applyDate = :applyDate")
    List<TimeTableVo> getTimetable(@Param("year") String year, @Param("semester") String semester, @Param("gradeLevel") String gradeLevel, @Param("className") String className, @Param("applyDate") LocalDate applyDate);



    @Query("SELECT COUNT(ssd) > 0 FROM ScheduleSubjectDetails ssd WHERE ssd.parentCode = :parentCode AND ssd.lession = :lession")
    Boolean isExist(@Param("parentCode") String parentCode, @Param("lession") String lession);

    @Query("SELECT ssd FROM ScheduleSubjectDetails ssd WHERE ssd.parentCode = :parentCode AND ssd.lession = :lession")
    Optional<ScheduleSubjectDetails> getEntity(@Param("parentCode") String parentCode, @Param("lession") String lession);
    
}
