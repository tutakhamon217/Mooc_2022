package fpt.capstone.repository;

import fpt.capstone.entities.ClassRoom;
import fpt.capstone.utility.Range;
import fpt.capstone.vo.ClassRoomVo;
import fpt.capstone.vo.DropDownKeyValueVo;
import fpt.capstone.vo.DropDownVo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Integer> {

    @Query("SELECT DISTINCT c.years FROM ClassRoom c")
    List<String> getDropDownSchoolYears();

    @Query("SELECT new fpt.capstone.vo.ClassRoomVo(c.id, c.createdTime, c.createdName, c.updatedTime, c.updatedName, c.name, c.code, c.deptID, d.name, c.gradeLevel, gl.name, c.years, c.specialize, s.name, c.teacherID, t.fullName, c.status) FROM ClassRoom c LEFT JOIN Departments d on c.deptID = d.id LEFT JOIN GradeLevel gl on c.gradeLevel = gl.id LEFT JOIN Subjects s on c.specialize = s.id LEFT JOIN Teacher t on c.teacherID = t.id WHERE c.years = :years")
    Page<ClassRoomVo> getAllClassRoomByYears(@Param("years") String years, Pageable pageable);

    @Query("SELECT new fpt.capstone.vo.DropDownVo(c.id, c.name, c.code) FROM ClassRoom c WHERE c.years = :years AND c.gradeLevel = :gradeLevel and c.deptID = :deptId")
    List<DropDownVo> getAllClassRoomByYearsAndGrade(@Param("years") String years, @Param("gradeLevel") Integer gradeLevel, @Param("deptId") Integer deptId );

    @Query("SELECT new fpt.capstone.vo.DropDownVo(c.id, c.name, c.code) FROM ClassRoom c WHERE c.years = :years AND c.gradeLevel = :gradeLevel")
    List<DropDownVo> getAllClassRoomByYearsAndGradeAllDept(@Param("years") String years, @Param("gradeLevel") Integer gradeLevel);

    @Query("SELECT new fpt.capstone.vo.DropDownVo(c.id, c.name, c.code) FROM ClassRoom c WHERE c.years = :years")
    List<DropDownVo> getAllClassRoomByYearsAndGradeAllDeptAllGrade(@Param("years") String years);


    @Query("SELECT new fpt.capstone.vo.ClassRoomVo(c.id, c.createdTime, c.createdName, c.updatedTime, c.updatedName, c.name, c.code, c.deptID, d.name, c.gradeLevel, gl.name, c.years, c.specialize, s.name, c.teacherID, t.fullName, c.status) FROM ClassRoom c LEFT JOIN Departments d on c.deptID = d.id LEFT JOIN GradeLevel gl on c.gradeLevel = gl.id LEFT JOIN Subjects s on c.specialize = s.id LEFT JOIN Teacher t on c.teacherID = t.id WHERE c.years = :years AND c.code LIKE %:code% AND c.name LIKE %:name% AND c.gradeLevel BETWEEN :#{#gradeLevel.minimum} AND :#{#gradeLevel.maximum} AND c.deptID BETWEEN :#{#Department.minimum} AND :#{#Department.maximum}")
    Page<ClassRoomVo> findClassRoomByCodeAndNameAndGradeLevelRangeAndDepartmentRange(
            @Param("years") String years,
            @Param("code") String code,
            @Param("name") String name,
            @Param("gradeLevel") Range<Integer> gradeLevelRange,
            @Param("Department") Range<Integer> deptIdRange,
            Pageable pageable
    );

    @Query("SELECT new fpt.capstone.vo.ClassRoomVo(c.id, c.createdTime, c.createdName, c.updatedTime, c.updatedName, c.name, c.code, c.deptID, d.name, c.gradeLevel, gl.name, c.years, c.specialize, s.name, c.teacherID, t.fullName, c.status) FROM ClassRoom c LEFT JOIN Departments d on c.deptID = d.id LEFT JOIN GradeLevel gl on c.gradeLevel = gl.id LEFT JOIN Subjects s on c.specialize = s.id LEFT JOIN Teacher t on c.teacherID = t.id WHERE c.id = :id")
    Optional<ClassRoomVo> getClassRoomById(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE ClassRoom c SET c.name = :name, c.gradeLevel = :gradeLevel, c.deptID = :deptID, c.specialize = :specialize, c.teacherID = :teacher, c.updatedTime = NOW() WHERE c.code = :code AND c.years = :years")
    int update(@Param("code") String code,
               @Param("years") String years,
               @Param("name") String name,
               @Param("gradeLevel") Integer gradeLevel,
               @Param("deptID") Integer deptID,
               @Param("specialize") Integer specialize,
               @Param("teacher") Integer teacher);

    @Override
    <S extends ClassRoom> List<S> saveAll(Iterable<S> entities);

    Optional<ClassRoom> getClassRoomByCode(String code);

    @Query(nativeQuery = true, value = "select id, code, name from class_room where (:deptId is null or dept_id = :deptId)  and (:gradeLevel is null or grade_level=:gradeLevel)  and years like :years order by name asc ")
    List<Map<String, Object>> getAllClassByDeptIdAndGradeLevelAndYears(@Param("deptId") Integer deptId,
                                                                       @Param("gradeLevel") Integer gradeLevel,
                                                                       @Param("years") String years);

    @Query(nativeQuery = true, value = "select dept_id from class_room where code = ?1 ")
    List<Map<String, Object>> getDepIdOfClassroom(@Param("classCode") String classCode);

    @Query(nativeQuery = true, value = "select code, name, id from class_room where grade_level =?1  and years=?2 order by name asc ")
    List<Map<String, Object>> getAllClassByGradeLevelAndYears(int gradeLevel, String years);

    @Query("select count(cr) > 0 from ClassRoom cr  where cr.gradeLevel = :gradeLevel and cr.years= :year and cr.code = :classCode")
    Boolean isExist(@Param("gradeLevel") int gradeLevel, @Param("year") String years, @Param("classCode") String classCode);

    @Query(nativeQuery = true, value = "SELECT cr.* FROM class_room cr\n" +
            "join teachers t on cr.teacher_id = t.id\n" +
            "join user u on u.login = t.code\n" +
            "where u.id = :userId and years like :year")
    List<ClassRoom> getClassroomByTeacherIdAndYear(@Param("userId") Integer userId, @Param("year") String year);

    @Query("SELECT c FROM ClassRoom c JOIN Teacher t ON c.teacherID = t.id WHERE t.code = :teacher_code AND c.years = :year")
    List<ClassRoom> getManageClassRooms(@Param("teacher_code") String teacherCode, @Param("year") String year);

    @Query("SELECT COUNT(classRoom) > 0 FROM ClassRoom classRoom WHERE classRoom.deptID = :depID")
    Boolean hasClassroomInDeptID(@Param("depID") Integer depID);

    @Query("SELECT DISTINCT COUNT(cr) > 0 FROM ClassRoom cr WHERE cr.code = :code")
    Boolean isExistForClassRoomByCode(@Param("code") String code);

    @Query(nativeQuery = true, value = "SELECT * FROM student_history where current_class_code = ?1")
    List<Map<String, Object>> checkClassHasStudent(String classCode);

    @Modifying
    @Query(nativeQuery = true, value = "delete from class_room where code = ?1")
    int deleteClassRoomByCode(String classCode);

}
