package fpt.capstone.repository;

import fpt.capstone.entities.SchoolYear;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface SchoolYearRespository extends JpaRepository<SchoolYear, Integer> {
    @Query("SELECT DISTINCT schoolYear.years FROM SchoolYear schoolYear ORDER BY schoolYear.years DESC")
    List<String> getAllSchoolYear();


    @Override
    <S extends SchoolYear> List<S> saveAll(Iterable<S> entities);

    List<SchoolYear> getSchoolYearByYearsOrderBySemesterAsc(String years);

    @Query(nativeQuery = true, value = "select id, semester, SUBSTRING(from_date,1,10) as from_date , SUBSTRING(to_date,1,10) as to_date from school_year where years= :year order by semester asc")
    List<Map<String, Object>> getSemesterInfor(@Param("year") String year);

    @Query(nativeQuery = true, value = "select \n" +
    "case when (sy.years in (select distinct years from school_year s where s.years in (select distinct school_year from schedule_timetable))) \n" +
    "then 0 else 1 end as canUpdate,\n" +
    "sy.* from school_year sy  group by sy.years order by sy.years desc;")
        List<Map<String, Object>> findAllGroupYear();


    List<SchoolYear> getSchoolYearByYears(String years);

    void delete(SchoolYear schoolYears);


    @Query(nativeQuery = true, value = " select * from school_year sy group by sy.years order by sy.years desc")
    Page<SchoolYear> findAllGroupYear(Pageable pageable);

    @Query(nativeQuery = true, value = "select distinct semester_amount  from school_year where years=?1")
    Integer getSemesterAmount(String years);

    @Query(nativeQuery = true, value = "select from_date, to_date from school_year where years=?1 and semester=?2")
    List<Map<String, Object>> getStartEndSemester(String year, Integer semester);

    // @Query("SELECT sy.semester FROM SchoolYear sy WHERE sy.years = :year AND :month BETWEEN MONTH(sy.fromDate) AND MONTH(sy.toDate)")
    // Optional<String> getSemester(@Param("year") String year, @Param("month") Integer month);

    @Query("SELECT sy FROM SchoolYear sy WHERE sy.years = :year AND sy.semester = :semester")
    Optional<SchoolYear> getSchoolYearOptional(@Param("year") String year, @Param("semester") String semester);

    @Query(nativeQuery = true, value = "SELECT sy.semester FROM school_year sy WHERE ?1 BETWEEN sy.from_date AND sy.to_date")
    Optional<String> getSemester(LocalDateTime date);

    @Query(nativeQuery = true, value = "SELECT * FROM school_year sy WHERE ?1 BETWEEN sy.from_date AND sy.to_date")
    Optional<SchoolYear> getSemesterOf(LocalDateTime date);

    @Query(nativeQuery = true, value = "SELECT distinct year FROM student_history where student_code like :studentCode")
    List<String> getSchoolYearOfHistoryStudent (@Param("studentCode") String studentCode);

    @Query("select sy from SchoolYear sy")
    Optional<List<SchoolYear>> getAllSchoolYearNoDistinct();

    @Query(nativeQuery = true, value ="SELECT sy.years from school_year sy where from_date< CURRENT_TIMESTAMP AND CURRENT_TIMESTAMP<to_date;")
    String getCurrentSchoolYear();

    @Query(nativeQuery = true, value = "select * from school_year where semester = :semester and years like :year")
    List<Map<String, Object>> getRangeOfSemester(@Param("year") String year,
                                                 @Param("semester") Integer semester);
}
