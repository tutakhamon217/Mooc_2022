package fpt.capstone.repository;

import fpt.capstone.entities.ScheduleGradeClass;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ScheduleGradeClassRepository extends JpaRepository<ScheduleGradeClass, Integer> {
    @Query("SELECT COUNT(sgc) > 0 FROM ScheduleGradeClass sgc WHERE sgc.parentCode = :parentCode AND sgc.gradeLevel = :gradeLevel AND sgc.classCode = :className AND sgc.applyDate = :applyDate")
    Boolean isExist(@Param("parentCode") String parentCode, @Param("gradeLevel") String gradeLevel, @Param("className") String className, @Param("applyDate") LocalDate applyDate);

    @Query("SELECT sgc.code FROM ScheduleGradeClass sgc WHERE sgc.parentCode = :parentCode AND sgc.gradeLevel = :gradeLevel AND sgc.classCode = :className AND sgc.applyDate = :applyDate")
    String getCode(@Param("parentCode") String parentCode, @Param("gradeLevel") String gradeLevel, @Param("className") String className, @Param("applyDate") LocalDate applyDate);

    @Query(nativeQuery = true, value = "SELECT sgc.code FROM schedule_timetable st JOIN schedule_grade_class sgc ON st.code = sgc.parent_code JOIN grade_level gl ON gl.code = sgc.grade_level WHERE sgc.apply_date <= ?1 AND gl.id = ?2 AND class_code = ?3 AND st.semester = ?4 ORDER BY sgc.apply_date DESC LIMIT 1")
    Optional<String> getLatestScheduleGradeClassCode(LocalDate applyDate, String gradeLevel, String classCode, String semester);

    @Override
    <S extends ScheduleGradeClass> List<S> saveAll(Iterable<S> entities);
}
