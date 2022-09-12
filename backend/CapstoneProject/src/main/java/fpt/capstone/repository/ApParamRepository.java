package fpt.capstone.repository;

import fpt.capstone.entities.ApParam;
import fpt.capstone.vo.ApParamDropDownVo;
import fpt.capstone.vo.DepartmentParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApParamRepository extends JpaRepository<ApParam, Integer> {
    @Query("SELECT new fpt.capstone.vo.DepartmentParam(param.code, param.value, param.name) FROM ApParam param WHERE param.type = 'DEPT'")
    List<DepartmentParam> getAllDepartmentsParam();

    Optional<ApParam> findFirstByCode(String code);

    @Query("SELECT new fpt.capstone.vo.ApParamDropDownVo(param.code, param.name) FROM ApParam param WHERE param.type = :type")
    List<ApParamDropDownVo> getDropDownValues(@Param("type") String type);

    @Query("SELECT ap.value FROM ApParam ap WHERE ap.type = 'HOLIDAY'")
    Optional<String> getHoliday();
}
