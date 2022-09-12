package fpt.capstone.repository;


import fpt.capstone.entities.GradeLevel;

import fpt.capstone.vo.DropDownVo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface GradeLevelRepositpry extends JpaRepository<GradeLevel, Integer> {


    @Query("SELECT new fpt.capstone.vo.DropDownVo(gl.id, gl.name, gl.code) FROM GradeLevel gl")
    List<DropDownVo> getDropDownList();

    @Query("SELECT COUNT(gl) > 0 FROM GradeLevel gl WHERE gl.id = :id")
    Boolean isExist(@Param("id") Integer id);
 
}
