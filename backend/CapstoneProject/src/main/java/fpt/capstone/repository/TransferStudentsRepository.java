package fpt.capstone.repository;

import fpt.capstone.entities.TransferStudents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransferStudentsRepository extends JpaRepository<TransferStudents, Integer> {
    Optional<TransferStudents> findBySchoolYearAndGradeCodeAndClassCode(String schoolYear, String gradeCode, String classCode);

}
