package fpt.capstone.repository;


import fpt.capstone.entities.Student;
import fpt.capstone.vo.StudentVo;
import fpt.capstone.vo.StudentDetailInformationVo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {


    @Query(value="SELECT new fpt.capstone.vo.StudentVo(s.id, s.code, s.fullName, s.sex, s.birthDay, c.name, d.name, s.status) from Student s JOIN StudentHistory h on s.code = h.studentCode and h.year like :years join ClassRoom c on h.currentClassCode = c.code join Departments d on c.deptID = d.id WHERE (s.fullName LIKE %:search_word% or s.code LIKE %:search_word%) and (:class_code like '' or h.currentClassCode like :class_code)  and (:grade_level like '' or CAST(c.gradeLevel AS string) like :grade_level) and ( :status like '' or CAST(s.status AS string) like :status) order by SUBSTRING_INDEX(s.fullName, ' ', -1) ")
    Page<StudentVo> getAllStudentByNameOrCodeInClassAllStatus(@Param("status") String status, @Param("class_code") String class_code, @Param("search_word") String search_word,  @Param("grade_level") String grade_level, @Param("years") String years, Pageable pageable);
    
    List<Student> findByCode(String code);

    @Query(nativeQuery = true, value = "select s.* from students s\n" +
            "join student_history sh on s.code = sh.student_code\n" +
            "where year like :year and sh.current_class_code like :class_code and s.status = 0")
    List<Student> getStudentOfClass(@Param("year") String year,
                                    @Param("class_code") String class_code);


        @Query(nativeQuery = true, value = "select s.* from students s\n" +
        "join student_history sh on s.code = sh.student_code\n" +
        "where year like :year and sh.current_class_code like :class_code and ( s.status = 0 or s.status = 1)")
        List<Student> getStudentOfClassManDanhGiaHocLuc(@Param("year") String year,
                                @Param("class_code") String class_code);

    @Query(value="SELECT new fpt.capstone.vo.StudentDetailInformationVo(s.id, s.avatar, s.code, s.fullName, s.status, cr.gradeLevel, d.id, d.name, cr.code, cr.name, s.trainingSystem, s.phone, s.email, s.birthDay, s.religion, s.nation, s.homeTown, s.permanentAddress, s.temporaryAddress, s.socialInsuranceNumber, s.identityCard, s.issuedAddress, s.issuedDate, s.sex, s.startDate, s.electFormat, s.graduationType, c.relationship, c.fullName, c.phone,  cs.relationship, cs.fullName, cs.phone)  from Student s join Contact cx  on s.id = cx.studentId and s.code like :student_code left join  Contact c on s.id = c.studentId and c.primaryContact = 1 left join  Contact cs on s.id = cs.studentId and cs.primaryContact = 0  left join StudentHistory sh on s.code = sh.studentCode and sh.year like :year join ClassRoom cr on sh.currentClassCode = cr.code join Departments d on cr.deptID = d.id ")
    List<StudentDetailInformationVo> getStudentDetailInformation(@Param("student_code") String student_code, @Param("year") String year, Pageable pageable);

    @Query(value="SELECT new fpt.capstone.vo.StudentDetailInformationVo(s.id, s.avatar, s.code, s.fullName, s.status, cr.gradeLevel, d.id, d.name, cr.code, cr.name, s.trainingSystem, s.phone, s.email, s.birthDay, s.religion, s.nation, s.homeTown, s.permanentAddress, s.temporaryAddress, s.socialInsuranceNumber, s.identityCard, s.issuedAddress, s.issuedDate, s.sex, s.startDate, s.electFormat, s.graduationType, c.relationship, c.fullName, c.phone,  cs.relationship, cs.fullName, cs.phone)  from Student s join Contact cx  on s.id = cx.studentId and s.code like :student_code left join  Contact c on s.id = c.studentId and c.primaryContact = 1 left join  Contact cs on s.id = cs.studentId and cs.primaryContact = 0  left join StudentHistory sh on s.code = sh.studentCode left join ClassRoom cr on sh.currentClassCode = cr.code left join Departments d on s.deptId = d.id where sh.year like :year")
    List<StudentDetailInformationVo> getStudentDetailInformationSideParent(@Param("student_code") String student_code, @Param("year") String year, Pageable pageable);


    @Query("SELECT s FROM StudentHistory sh JOIN Student s ON sh.studentCode = s.code WHERE sh.currentClassCode = :classCode AND sh.year = :year")
    List<Student> getCurrentStudentsInClass(@Param("classCode") String classCode, @Param("year") String year);

    @Query("SELECT COUNT(sh) FROM StudentHistory sh WHERE sh.studentCode = :code AND sh.currentClassCode = :classCode AND sh.year = :year")
    Boolean isExistStudent(@Param("code") String studentCode, @Param("classCode") String classCode, @Param("year") String year);

    @Query("SELECT s FROM Student s WHERE s.code = :code")
    Optional<Student> getByCode(@Param("code") String code);
    
    @Modifying
    @Query("UPDATE Student s SET s.updateName = :updateName, s.updateTime = NOW(), s.fullName = :fullName, s.deptId = :deptId, s.startDate = :startDate, s.phone = :phone, s.email = :email, s.birthDay = :birthDay, s.homeTown = :homeTown, s.nation = :nation, s.permanentAddress = :permanentAddress, s.socialInsuranceNumber = :socialInsuranceNumber, s.identityCard = :identityCard, s.issuedAddress = :issuedAddress, s.issuedDate = :issuedDate, s.sex = :sex, s.avatar = :avatar, s.electFormat = :electFormat, s.graduationType = :graduationType, s.status = :status, s.trainingSystem = :trainingSystem, s.religion = :religion, s.temporaryAddress = :temporaryAddress  WHERE s.code like :code")
    int update(
                @Param("code") String code,
               @Param("updateName") String updateName,
               @Param("fullName") String fullName,
               @Param("deptId") Integer deptId,
               @Param("startDate") LocalDateTime startDate,
               @Param("phone") String phone,
               @Param("email") String email,
               @Param("birthDay") LocalDateTime birthDay,
               @Param("homeTown") String homeTown,
               @Param("nation") String nation,
               @Param("permanentAddress") String permanentAddress,
               @Param("socialInsuranceNumber") String socialInsuranceNumber,
               @Param("identityCard") String identityCard,
               @Param("issuedAddress") String issuedAddress,
               @Param("issuedDate") LocalDateTime issuedDate,
               @Param("sex") Integer sex,
               @Param("avatar") String avatar,
               @Param("electFormat") Integer electFormat,
               @Param("graduationType") Integer graduationType,
               @Param("status") Integer status,
               @Param("trainingSystem") Integer trainingSystem,
               @Param("religion") String religion,
               @Param("temporaryAddress") String temporaryAddress
            );

    @Query(nativeQuery = true, value = "SELECT sh.year, cr.id as classId, sh.current_class_code as classCode, cr.name as className  FROM student_history sh\n" +
            "join class_room cr on cr.code = sh.current_class_code\n" +
            "where student_code like :studentCode")
    List<Map<String, Object>> getAllYearHistoryOfStudent(@Param("studentCode") String studentCode);

    @Query(nativeQuery = true, value = "select t1.avgScore, t2.* from (select (b.totalAvg/b.totalCoefficient) as avgScore, semester from (\n" +
            "select sum((a.avg_score * coefficient)) as totalAvg, sum(coefficient) as totalCoefficient, semester from \n" +
            "(select avg_score, coefficient , semester from \n" +
            "\t(SELECT avg_score, sc.coefficient, gb.semester FROM gradebook gb\n" +
            "\tjoin gradebook_subjects_details gsd on gb.code = gsd.parent_code\n" +
            "\tjoin subjects s on gsd.subject_code = s.code\n" +
            "\tjoin subject_class sc on sc.subject_id = s.id\n" +
            "\twhere gb.school_year = :year and gb.semester = 1 and gb.class_code like :classCode and gsd.student_code like :studentCode and sc.class_id = :classId and s.sub_type = 0\n" +
            "    union all\n" +
            "\tSELECT avg_score, sc.coefficient, gb.semester FROM gradebook gb\n" +
            "\tjoin gradebook_subjects_details gsd on gb.code = gsd.parent_code\n" +
            "\tjoin subjects s on gsd.subject_code = s.code\n" +
            "\tjoin subject_class sc on sc.subject_id = s.id\n" +
            "\twhere gb.school_year = :year and gb.semester = 2 and gb.class_code like :classCode and gsd.student_code like :studentCode and sc.class_id = :classId and s.sub_type = 0\n" +
            "    union all\n" +
            "\tSELECT avg_score, sc.coefficient, gb.semester FROM gradebook gb\n" +
            "\tjoin gradebook_subjects_details gsd on gb.code = gsd.parent_code\n" +
            "\tjoin subjects s on gsd.subject_code = s.code\n" +
            "\tjoin subject_class sc on sc.subject_id = s.id\n" +
            "\twhere gb.school_year = :year and gb.semester = 0 and gb.class_code like :classCode and gsd.student_code like :studentCode and sc.class_id = :classId and s.sub_type = 0\n" +
            "    ) as t\n" +
            ") as a\n" +
            "group by semester) as b) as t1\n" +
            "join (SELECT ascd.academic_ability, ascd.conduct, ascd.competition_title, semester FROM assess_student_conduct as asco\n" +
            "join assess_student_conduct_details ascd on asco.code = ascd.parent_code\n" +
            "where asco.class_code like :classCode and asco.school_year like :year and asco.semester = 1 and ascd.student_code like :studentCode\n" +
            "union all \n" +
            "SELECT ascd.academic_ability, ascd.conduct, ascd.competition_title, semester FROM assess_student_conduct as asco\n" +
            "join assess_student_conduct_details ascd on asco.code = ascd.parent_code\n" +
            "where asco.class_code like :classCode and asco.school_year like :year and asco.semester = 2 and ascd.student_code like :studentCode\n" +
            "union all \n" +
            "SELECT ascd.academic_ability, ascd.conduct, ascd.competition_title, semester FROM assess_student_conduct as asco\n" +
            "join assess_student_conduct_details ascd on asco.code = ascd.parent_code\n" +
            "where asco.class_code like :classCode and asco.school_year like :year and asco.semester = 0 and ascd.student_code like :studentCode) as t2\n" +
            "on t1.semester = t2.semester")
    List<Map<String, Object>> getResultStudyEver(@Param("classCode") String classCode,
                                                 @Param("year") String year,
                                                 @Param("studentCode") String studentCode,
                                                 @Param("classId") Integer classId);

    @Query(nativeQuery = true, value = "SELECT count(*) FROM student_history sh\n" +
            "join students s on sh.student_code = s.code\n" +
            "where sh.current_class_code like :classCode and sh.year like :year and s.status = 0")
    Integer getAmountOfStudentByClassCodeAndYear(@Param("classCode") String classCode,
                                                 @Param("year") String year);
}
