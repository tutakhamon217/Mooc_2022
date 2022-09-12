package fpt.capstone.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fpt.capstone.entities.AttendanceStudent;

public interface AttendanceStudentRepository extends JpaRepository<AttendanceStudent, Integer>{
    @Query("SELECT ast.code FROM AttendanceStudent ast WHERE ast.schoolYear = :year AND ast.semester = :semester AND ast.month = :month")
    Optional<String> getCode(@Param("year") String year, @Param("semester") String semester, @Param("month") String month);
    @Override
    <S extends AttendanceStudent> List<S> saveAll(Iterable<S> entities);

    @Query(nativeQuery = true, value = "SELECT sum(case asd.check_date when 'P' then 1 else 0 end ) as number_off_allowed, sum(case asd.check_date when 'K' then 1 else 0 end ) as number_off FROM attendance_student as att\n" +
            "join attendance_student_details asd on att.code = asd.parent_code\n" +
            "where att.shool_year like :year and (:semester = 0 or att.semester = :semester) and asd.student_code like :studentCode")
    List<Map<String, Object>> getPandKbyYearAndSemesterAndStudentCode(@Param("year") String year,
                                                                      @Param("semester") Integer semester,
                                                                      @Param("studentCode") String studentCode);
}
