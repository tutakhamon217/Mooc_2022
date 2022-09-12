package fpt.capstone.repository;

import fpt.capstone.entities.ScheduleTimetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ScheduleTimetableRepository extends JpaRepository<ScheduleTimetable, Integer> {
    @Query("SELECT COUNT(st) > 0 FROM ScheduleTimetable st WHERE st.schoolYear = :year AND st.semester = :semester")
    Boolean isExisted(@Param("year") String year, @Param("semester") String semester);

    @Query("SELECT st.code FROM ScheduleTimetable st WHERE st.schoolYear = :year AND st.semester = :semester")
    String getCodeByYearAndSemester(@Param("year") String year, @Param("semester") String semester);

    @Override
    <S extends ScheduleTimetable> List<S> saveAll(Iterable<S> entities);

    @Query("SELECT COUNT(st) > 0 FROM ScheduleTimetable st WHERE st.schoolYear = :year")
    Boolean isExistedByYear(@Param("year") String year);

}
