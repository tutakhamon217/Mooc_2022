package fpt.capstone.repository;


import fpt.capstone.entities.Student;
import fpt.capstone.entities.*;
import fpt.capstone.vo.StudentVo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

    @Modifying
    @Query("UPDATE Contact c SET c.fullName = :fullName, c.phone = :phone, c.relationship = :relationship WHERE c.studentId = :studentId and c.primaryContact = 1")
    int updatePrimary(
                @Param("fullName") String fullName,
                @Param("phone") String phone,
                @Param("relationship") Integer relationship,
                @Param("studentId") Integer studentId
            );

    @Modifying
    @Query("UPDATE Contact c SET c.fullName = :fullName, c.phone = :phone, c.relationship = :relationship WHERE c.studentId = :studentId and c.primaryContact = 0")
    int updateSecond(
                @Param("fullName") String fullName,
                @Param("phone") String phone,
                @Param("relationship") Integer relationship,
                @Param("studentId") Integer studentId
            );

}
