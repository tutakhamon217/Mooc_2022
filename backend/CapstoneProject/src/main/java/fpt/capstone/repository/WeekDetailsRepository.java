package fpt.capstone.repository;

import fpt.capstone.entities.WeekDetails;
import fpt.capstone.vo.TimeTableVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeekDetailsRepository extends JpaRepository<WeekDetails, Integer> {
    @Query("SELECT new fpt.capstone.vo.TimeTableVo(wd.day, wd.type, wd.type ,sgc.id, sgc.code, wd.id, wd.code) FROM ScheduleTimetable st LEFT JOIN ScheduleGradeClass sgc ON st.code = sgc.parentCode LEFT JOIN WeekDetails wd ON wd.parentCode = sgc.code WHERE st.schoolYear = :year AND st.semester = :semester  AND sgc.gradeLevel = :gradeLevel AND sgc.classCode = :className AND sgc.applyDate = :applyDate")
    List<TimeTableVo> getTimetable(@Param("year") String year, @Param("semester") String semester, @Param("gradeLevel") String gradeLevel, @Param("className") String className, @Param("applyDate") LocalDate applyDate);

    @Query("SELECT COUNT(wd) > 0 FROM WeekDetails wd WHERE wd.parentCode = :parentCode AND wd.type = :type AND wd.day = :day")
    Boolean isExist(@Param("parentCode") String parentCode, @Param("type") String type, @Param("day") String day);

    @Query("SELECT wd.code FROM WeekDetails wd WHERE wd.parentCode = :parentCode AND wd.type = :type AND wd.day = :day")
    String getCode(@Param("parentCode") String parentCode, @Param("type") String type, @Param("day") String day);

}
