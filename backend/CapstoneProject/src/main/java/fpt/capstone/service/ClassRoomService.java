package fpt.capstone.service;

import fpt.capstone.entities.ClassRoom;
import fpt.capstone.form_data.ClassRoomForm;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.vo.ClassRoomVo;
import fpt.capstone.vo.DropDownKeyValueVo;
import fpt.capstone.vo.DropDownVo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface ClassRoomService {
    ServiceResult<List<String>> getDropDownSchoolYears();

    ServiceResult<Page<ClassRoomVo>> getAllClassroomsByYears(String years, Pageable pageable);

    ServiceResult<List<DropDownVo>> getAllClassroomsByYearsAndGrade(Integer gradeLevel, String years, Integer deptID);

    ServiceResult<List<DropDownVo>> getAllClassroomsByYearsAndGradeAllDept(Integer gradeLevel, String years);

    ServiceResult<Page<ClassRoomVo>> searchClassRooms(String years, String code, String name, Integer gradeLevel, Integer deptID, Pageable pageable);

    ServiceResult<ClassRoomVo> getByID(Integer id);

    ServiceResult<Boolean> deleteClassRoom(String classCode);

    ServiceResult<Boolean> updateClassRoom(ClassRoomForm form);

    ServiceResult<Boolean> insert(ClassRoomForm form);


    ServiceResult<Map<String,Object>> getAllClassByGradeLevelAndYears(int gradeLevel, String years);

    ServiceResult<List<ClassRoom>> getClassroomByTeacherIdAndYear(Integer teacherId, String year);

    ServiceResult<List<ClassRoom>> getClassManagedBy(String teacherCode, String year);
}
