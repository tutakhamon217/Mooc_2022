package fpt.capstone.repository;


import fpt.capstone.form_data.UpdateDepartmentForm;
import fpt.capstone.entities.Departments;
import fpt.capstone.vo.DepartmentVo;
import fpt.capstone.vo.DepartmentVoV1;
import fpt.capstone.vo.*;
import fpt.capstone.vo.DropDownVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Departments, Integer> {
    @Query("SELECT new fpt.capstone.vo.DepartmentVo(department.id, department.createdTime, department.createdName, department.updateTime, department.updateName, department.name, department.code, department.parentID, department.position, department.employeeID, department.descption, department.type) FROM Departments department WHERE department.parentID = ?1")
    List<DepartmentVo> getDepartmentsByParentID(Integer parentID);

    @Query(value = "SELECT new fpt.capstone.vo.DepartmentVoV1(d.id, d.createdTime, d.createdName, d.updateTime, d.updateName, d.name, d.code, d.parentID, d1.name, d.position, d.employeeID, t.fullName, d.descption, d.type, ap.name) FROM Departments d LEFT JOIN Departments d1 ON (d.parentID = d1.id) LEFT JOIN Teacher t on (d.employeeID = t.id) LEFT JOIN ApParam ap ON (d.type = ap.code) WHERE UPPER(d.code) LIKE %:code% AND UPPER(d.name) LIKE %:name%")
    List<DepartmentVoV1> searchDepartmentsByCodeOrName(@Param("code") String code, @Param("name") String name);

    @Query("SELECT new fpt.capstone.vo.DepartmentVoV1(d.id, d.createdTime, d.createdName, d.updateTime, d.updateName, d.name, d.code, d.parentID, d1.name, d.position, d.employeeID, t.fullName, d.descption, d.type, ap.name) FROM Departments d LEFT JOIN Departments d1 ON d.parentID = d1.id LEFT JOIN Teacher t on d.employeeID = t.id LEFT JOIN ApParam ap ON d.type = ap.code WHERE d.code = :code")
    Optional<DepartmentVoV1> findFirstByCode(String code);


    @Query("SELECT new fpt.capstone.vo.DropDownVo(department.id, department.name) FROM Departments department")
    List<DropDownVo> getDropDownValues();

    @Query("SELECT new fpt.capstone.vo.DepartmentWithTypeVo(department.id, department.name, department.type) FROM Departments department")
    List<DepartmentWithTypeVo> getDropDownWithTypeValues();

    @Modifying
    @Query("UPDATE Departments d SET d.name = :#{#form.name}, d.parentID = :#{#form.parentID}, d.type = :#{#form.type}, d.descption = :#{#form.description}, d.employeeID = :#{#form.employeeID}, d.position = :#{#form.position}, d.updateTime = NOW() WHERE d.code = :#{#form.code} ")
    int update(@Param("form") UpdateDepartmentForm form);

    @Override
    <S extends Departments> List<S> saveAll(Iterable<S> entities);

    @Query("SELECT new fpt.capstone.vo.DepartmentVoV1(d.id, d.createdTime, d.createdName, d.updateTime, d.updateName, d.name, d.code, d.parentID, d1.name, d.position, d.employeeID, t.fullName, d.descption, d.type, ap.name) FROM Departments d LEFT JOIN Departments d1 ON d.parentID = d1.id LEFT JOIN Teacher t on d.employeeID = t.id LEFT JOIN ApParam ap ON d.type = ap.code")
    List<DepartmentVoV1> getAllDepartments();


    @Query("SELECT DISTINCT new fpt.capstone.vo.DropDownVo(d.id, d.name, d.code) FROM Departments d JOIN ApParam ap ON d.type = ap.code WHERE ap.type = 'DEPT' and d.type ='DVDT' and d.parentID != 0 and d.parentID is not null")
    List<DropDownVo> getDropdownForClassRoom();

    @Query("SELECT DISTINCT COUNT(d) > 0 FROM Departments d JOIN ApParam ap ON d.type = ap.code WHERE ap.type = 'DEPT' AND d.id = :id")
    Boolean isExistForClassRoom(@Param("id") Integer id);

    @Query(nativeQuery = true, value = "SELECT d.* FROM departments as d INNER JOIN ap_param as ap \n" +
            "\tON d.type = ap.code \n" +
            "\tWHERE ap.type = 'DEPT' AND ap.code = 'DVDT' AND d.parent_id != 0")
    List<Map<String, Object>> getAllDepartment();

    @Query("SELECT DISTINCT new fpt.capstone.vo.DropDownVo(id,name) FROM Departments  where parentID=0 or parentID is null")
    List<DropDownVo> getAllParentDepartment();

    @Query("SELECT DISTINCT new fpt.capstone.vo.DropDownVo(id,name) FROM Departments  where parentID=:id")
    List<DropDownVo> getDepartmentByParentId(@Param("id") int id);

    @Query(nativeQuery = true, value = "select u.id as userId,d2.id,d2.name,d3.id as parentId ,d3.name as parentName from user u join teachers t on u.login=t.code \n" +
            "join departments d2 on t.dept_id = d2.id  join departments d3 on d2.parent_id =d3.id  where u.id=?1")
    List<Map<String, Object>> getDepartmentAndParentDepartment(int id);


}
