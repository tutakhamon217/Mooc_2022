package fpt.capstone.repository;


import fpt.capstone.entities.ServiceResult;
import fpt.capstone.entities.Subjects;

import fpt.capstone.vo.DropDownVo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.security.auth.Subject;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface SubjectsRepository extends JpaRepository<Subjects, Integer> {


    @Query(nativeQuery = true, value = "select distinct s.id,s.code,s.name,gl.name as gradeName,\n" +
            "case when(select count(*) from subjects ss join conf_score_subjects css on ss.code=css.subject_code where ss.id=s.id >0) then 1 else 0 end as isConfigGrade,\n" +
            "case when(select count(*) from subjects ss join subject_class sc on ss.id=sc.subject_id where ss.id=s.id >0) then 1 else 0 end as isConfigClass,\n" +
            "s.type,s.sub_type,s.description \n" +
            " from subjects s join subject_dept sd on s.id=sd.subject_id join  departments d on sd.dept_id=d.id join grade_level gl\n" +
            " on s.grade_level = gl.id WHERE (?1 is null or sd.dept_id= ?1) and (?2 is null or upper(s.code) Like UPPER('%' ?2 '%')) \n" +
            "and (?3 is null or upper(s.name) Like UPPER('%' ?3 '%')) and (?4 is null or s.grade_level= ?4) order by s.name asc ")
    List<Map<String, Object>> getAllSubjectsPagingAndSearching(Integer deptId,String code, String name,Integer gradeLevel,Pageable pageable);

    @Query(nativeQuery = true, value = "select distinct s.id,s.code,s.name,gl.name as gradeName,\n" +
            "case when(select count(*) from subjects ss join conf_score_subjects css on ss.code=css.subject_code where ss.id=s.id >0) then 1 else 0 end as isConfigGrade,\n" +
            "case when(select count(*) from subjects ss join subject_class sc on ss.id=sc.subject_id where ss.id=s.id >0) then 1 else 0 end as isConfigClass,\n" +
            "s.type,s.sub_type,s.description \n" +
            " from subjects s join subject_dept sd on s.id=sd.subject_id join  departments d on sd.dept_id=d.id join grade_level gl\n" +
            " on s.grade_level = gl.id WHERE (?1 is null or sd.dept_id= ?1) and (?2 is null or upper(s.code) Like UPPER('%' ?2 '%')) \n" +
            "and (?3 is null or upper(s.name) Like UPPER('%' ?3 '%')) and (?4 is null or s.grade_level= ?4) order by s.name asc ")
    List<Map<String, Object>> getAllSubjectsSearching(Integer deptId, String code, String name, Integer gradeLevel);

    @Query(nativeQuery = true, value = "select distinct s.id,s.code,s.name,gl.name as gradeName,d.name as deptName,sd.dept_id as deptId,s.type,s.sub_type,s.description " +
            "from subjects s join subject_dept sd on s.id=sd.subject_id join  departments d on sd.dept_id=d.id join grade_level gl on s.grade_level = gl.id order by s.name asc ")
    List<Map<String, Object>> getAllSubjectsPaging(Pageable pageable);


    @Override
    <S extends Subjects> S save(S entity);

    @Query("SELECT new fpt.capstone.vo.DropDownVo(s.id, s.name) FROM Subjects s JOIN SubjectDept sd ON s.id = sd.subjectId WHERE sd.deptId = :deptID and s.gradeLevel = :gradeId")
    List<DropDownVo> getSubjectsByDeptIDAndGradeLevel(@Param("deptID") Integer deptID, @Param("gradeId") Integer gradeId);

    @Query("SELECT COUNT(s) > 0 FROM Subjects s JOIN SubjectDept sd ON s.id = sd.subjectId WHERE sd.deptId = :deptID AND s.id = :id")
    Boolean isExistByDeptID(@Param("deptID") Integer deptID, @Param("id") Integer id);

    @Query(nativeQuery = true, value = "select distinct s.id, s.name, s.code, s.grade_level, s.type, s.sub_type, css.code as cssCode, \n" +
            "CASE WHEN s.code not in ( select distinct gsd.subject_code from gradebook gb \n" +
            "\t\t\t\t\tjoin gradebook_subjects_details gsd on gb.code = gsd.parent_code\n" +
            "                    join conf_score_subjects css on css.subject_code = gsd.subject_code and gb.semester = css.semester\n" +
            "\t\t\t\t\tleft join conf_score_details csd on csd.parent_code = css.code\n" +
            "\t\t\t\t\tleft join gradebook_score_details g_score_d on g_score_d.score_code = csd.code \n" +
            "                    join subjects s on s.code = gsd.subject_code \n" +
            "\t\t\t\t\twhere  gb.school_year like :year and gb.semester = :semester and s.sub_type = :type ) \n" +
            "THEN 0\n" +
            "ELSE 1\n" +
            "END as status \n" +
            "            from scoreboard_conf sc join conf_score_subjects css on sc.code = css.parent_code\n" +
            "            join subjects s on css.subject_code = s.code\n" +
            "            where sc.year like :year and s.grade_level = :grade_level and s.sub_type = :type and sc.grade_id = :grade_id and css.semester = :semester")
    List<Map<String, Object>> getSubjectByDeptAndType(@Param("grade_level") Integer grade_level,
                                                      @Param("type") Integer type,
                                                      @Param("year") String year,
                                                      @Param("grade_id") Integer grade_id,
                                                      @Param("semester") Integer semester
    );


    @Query(nativeQuery = true, value = "SELECT * FROM conf_score_details\n" +
            "where  parent_code like :parent_code")
    List<Map<String, Object>> getConfigScoreSubject(@Param("parent_code") String parent_code);

    @Query(nativeQuery = true, value = "SELECT * FROM conf_grading_details \n" +
            " where  parent_code like :parent_code")
    List<Map<String, Object>> getConfigGradingDetails(@Param("parent_code") String parent_code);

    @Query(nativeQuery = true, value = "SELECT distinct s.id ,s.name, s.code FROM subjects s\n" +
            "where s.grade_level = :grade_level and s.sub_type = :sup_type  and s.code not in (\n" +
            "\tSELECT distinct s.code FROM scoreboard_conf sc join conf_score_subjects css on sc.code = css.parent_code\n" +
            "\tjoin subjects s on css.subject_code = s.code\n" +
            "\twhere sc.year like :year and sc.grade_id = :grade_id and s.sub_type = :sup_type and s.grade_level = :grade_level and css.semester = :semester\n" +
            ")")
    List<Map<String, Object>> getSubjectsNotYetConfigScoreboard(@Param("grade_level") Integer grade_level,
                                                                @Param("sup_type") Integer type,
                                                                @Param("year") String year,
                                                                @Param("grade_id") Integer grade_id,
                                                                @Param("semester") Integer semester);

    @Query(nativeQuery = true, value = "select count(*) >0 from gradebook g join gradebook_subjects_details gsd on g.code=gsd.parent_code join subjects s on gsd.subject_code=s.code where s.id =?1")
    Integer isMarked(int code);

    
    @Query("SELECT s FROM Subjects s WHERE s.code = :code")
    Optional<Subjects> findByCodeOptional(@Param("code") String code);
    Subjects findByCode(String code);

    @Query(nativeQuery = true, value = "SELECT count(*) FROM subjects;")
    Integer getTotalSubject();

    @Query(nativeQuery = true, value = "select s.id, s.name from subject_class sc join class_room cr on sc.class_id=cr.id join subjects s on sc.subject_id=s.id where cr.code=?1 and sc.flg_semester2=1")
    List<Map<String, Object>> listAllSubjectOfClassSemester1(String classCode);

    @Query(nativeQuery = true, value = "select s.id, s.name from subject_class sc join class_room cr on sc.class_id=cr.id join subjects s on sc.subject_id=s.id where cr.code=?1 and sc.flg_semester1=1")
    List<Map<String, Object>> listAllSubjectOfClassSemester2(String classCode);

    @Query("SELECT DISTINCT COUNT(s) > 0 FROM Subjects s WHERE s.code = :code")
    Boolean isExistForSubjectByCode(@Param("code") String code);
}
