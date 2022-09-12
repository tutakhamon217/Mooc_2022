package fpt.capstone.repository;

import fpt.capstone.entities.SubjectDept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SubjectDeptRepository extends JpaRepository<SubjectDept, Integer> {
    @Override
    <S extends SubjectDept> S save(S entity);


    List<SubjectDept> getSubjectDeptsBySubjectId(int subjectId);

    @Query(nativeQuery = true, value = "SELECT sd.*, d.name FROM subject_dept sd\n" +
            "            join departments d on sd.dept_id = d.id\n" +
            "            where sd.subject_id = ?1")
    List<Map<String, Object>> getSubjectDeptBySubjectQueryNative(int subjectId);

    @Query("SELECT COUNT(subjectDept) > 0 FROM SubjectDept subjectDept WHERE subjectDept.deptId = :depID")
    Boolean hasSubjectDeptInInDeptID(@Param("depID") Integer depID);
}
