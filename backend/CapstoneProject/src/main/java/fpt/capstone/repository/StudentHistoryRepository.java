package fpt.capstone.repository;


import fpt.capstone.entities.Student;
import fpt.capstone.entities.*;
import fpt.capstone.vo.StudentVo;
import fpt.capstone.vo.StudentDetailInformationVo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StudentHistoryRepository extends JpaRepository<StudentHistory, Integer> {
    @Modifying
    @Query("UPDATE StudentHistory s SET s.currentClassCode = :currentClassCode WHERE s.studentCode like :studentCode and s.year = :year")
    int update(
                @Param("currentClassCode") String currentClassCode,
                @Param("studentCode") String studentCode,
                @Param("year") String year
            );

    Optional<StudentHistory> findByStudentCodeAndYear(String StudentCode, String Year);
}
