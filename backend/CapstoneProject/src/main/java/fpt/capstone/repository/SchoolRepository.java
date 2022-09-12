package fpt.capstone.repository;

import fpt.capstone.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SchoolRepository extends JpaRepository<School, Integer> {
    @Query(nativeQuery = true, value = "select * from district where province_id = :provinceId")
    List<Map<String, Object>> getAllDistrict(@Param("provinceId") Integer provinceId);

    @Query(nativeQuery = true, value = "select * from province")
    List<Map<String, Object>> getAllProvince();

    @Query(nativeQuery = true, value = "SELECT s.*, p.name as provinceName, d.name as districtName FROM schools s\n" +
            "join province p on s.province_id = p.id\n" +
            "join district d on s.district_id = d.id")
    List<Map<String, Object>> getSchoolInfo();
}
