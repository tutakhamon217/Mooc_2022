package fpt.capstone.repository;


import fpt.capstone.form_data.UpdateDepartmentForm;
import fpt.capstone.entities.AcademicAbility;
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
public interface AcademicAbilityRepository extends JpaRepository<AcademicAbility, Integer> {
   
}
