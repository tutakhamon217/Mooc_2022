package fpt.capstone.service;

import fpt.capstone.form_data.ClassRoomForm;
import fpt.capstone.entities.ClassRoom;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.payroll.ClassRoomNotFoundException;
import fpt.capstone.payroll.ValidationException;
import fpt.capstone.repository.*;
import fpt.capstone.utility.Range;
import fpt.capstone.vo.ClassRoomVo;
import fpt.capstone.vo.DropDownVo;
import fpt.capstone.vo.DropDownKeyValueVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ClassRoomServiceImpl implements ClassRoomService {

    @Autowired
    private ClassRoomRepository repository;

    @Autowired
    private GradeLevelRepositpry gradeLevelRepositpry;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private SubjectsRepository subjectsRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public ServiceResult<List<String>> getDropDownSchoolYears() {
        return new ServiceResult<>(HttpStatus.OK, "Dropdown", repository.getDropDownSchoolYears());
    }

    @Override
    public ServiceResult<Page<ClassRoomVo>> getAllClassroomsByYears(String years, Pageable pageable) {
        return new ServiceResult<>(HttpStatus.OK, "All classroom", repository.getAllClassRoomByYears(years, pageable));
    }

    @Override
    public ServiceResult<List<DropDownVo>> getAllClassroomsByYearsAndGrade(Integer gradeLevel, String years, Integer deptID) {
        return new ServiceResult<>(HttpStatus.OK, "All classroom", repository.getAllClassRoomByYearsAndGrade(years, gradeLevel, deptID));
    }

    @Override
    public ServiceResult<List<DropDownVo>> getAllClassroomsByYearsAndGradeAllDept(Integer gradeLevel, String years) {

        if(gradeLevel == 0) {
            return new ServiceResult<>(HttpStatus.OK, "All classroom", repository.getAllClassRoomByYearsAndGradeAllDeptAllGrade(years));
        } else return new ServiceResult<>(HttpStatus.OK, "All classroom", repository.getAllClassRoomByYearsAndGradeAllDept(years, gradeLevel));
    }
    

    @Override
    public ServiceResult<Page<ClassRoomVo>> searchClassRooms(String years, String code, String name, Integer gradeLevel, Integer deptID, Pageable pageable) {
        code = (code == null) ? "" : code;
        name = (name == null) ? "" : name;
        Range<Integer> gradeLevelRange = (gradeLevel == null) ? new Range<>(-1, Integer.MAX_VALUE) : new Range<>(gradeLevel, gradeLevel);
        Range<Integer> deptIDRange = (deptID == null) ? new Range<>(-1, Integer.MAX_VALUE) : new Range<>(deptID, deptID);

        return new ServiceResult<>(HttpStatus.OK, "Search", repository.findClassRoomByCodeAndNameAndGradeLevelRangeAndDepartmentRange(years, code, name, gradeLevelRange, deptIDRange, pageable));
    }

    @Override
    public ServiceResult<ClassRoomVo> getByID(Integer id) {
        ClassRoomVo classRoomVo = repository.getClassRoomById(id).orElseThrow(() -> new ClassRoomNotFoundException(id));
        return new ServiceResult<>(HttpStatus.OK, "classroom", classRoomVo);
    }

    @Override
    public ServiceResult<Boolean> deleteClassRoom(String classCode) {
 
        try {
            if (repository.checkClassHasStudent(classCode).size() > 0 ) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Không thể xóa lớp học có học sinh", true);
            }
            repository.deleteClassRoomByCode(classCode);
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Lỗi khi xóa lớp", true);
        }
        
        // repository.delete(classRoom);
        return new ServiceResult<>(HttpStatus.OK, "Xóa lớp học thành công", true);
    }

    @Override
    public ServiceResult<Boolean> updateClassRoom(ClassRoomForm form) {
        try {
            ClassRoom classRoom = repository.getClassRoomByCode(form.getCode()).orElseThrow(() -> new ClassRoomNotFoundException(null));
            if (!gradeLevelRepositpry.isExist(form.getGradeLevel())) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Khối không tồn tại", true);
            }

            if (!departmentRepository.isExistForClassRoom(form.getDeptID())) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Khoa ban không tồn tại", true);
            }

            if (form.getSpecialize() != null && !subjectsRepository.isExistByDeptID(form.getDeptID(), form.getSpecialize())) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Môn chuyên không tồn tại", true);
            }

            if (!teacherRepository.hasTeacherById(form.getTeacher())) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Giáo viên không tồn tại", true);
            }

            repository.update(form.getCode(), form.getYears(), form.getName(), form.getGradeLevel(), form.getDeptID(), form.getSpecialize(), form.getTeacher());
            return new ServiceResult<>(HttpStatus.OK, "Cập nhật lớp học thành công", true);
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.OK, "Cập nhật lớp học thất bại", true);
        }
    }

    @Override
    public ServiceResult<Boolean> insert(ClassRoomForm form) {
        try {
            if (repository.getClassRoomByCode(form.getCode()).isPresent()) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Mã lớp học đã tồn tại", true);
            }

            if (!gradeLevelRepositpry.isExist(form.getGradeLevel())) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Khối không tồn tại", true);
            }

            if (!departmentRepository.isExistForClassRoom(form.getDeptID())) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Khoa ban không tồn tại", true);
            }

            if (form.getSpecialize() != null && !subjectsRepository.isExistByDeptID(form.getDeptID(), form.getSpecialize())) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Môn chuyên không tồn tại", true);
            }

            if (!teacherRepository.hasTeacherById(form.getTeacher())) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Giáo viên không tồn tại", true);
            }
            

            
            List<ClassRoom> classRooms = new ArrayList<>();
            classRooms.add(new ClassRoom(1000, LocalDateTime.now(), "", LocalDateTime.now(), "", form.getName(), form.getCode(), form.getDeptID(), form.getGradeLevel(), form.getYears(), form.getSpecialize(), form.getTeacher(), 0));
            repository.saveAll(classRooms);

            return new ServiceResult<>(HttpStatus.OK, "Thêm mới lớp học thành công", true);
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thêm mới lớp học thất bại", true);
        }
    }

    @Override
    public ServiceResult<Map<String, Object>> getAllClassByGradeLevelAndYears(int gradeLevel, String years) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        try {
            List<Map<String, Object>> subjects = repository.getAllClassByGradeLevelAndYears(gradeLevel, years);
            Map<String, Object> output = new HashMap<>();
            output.put("classrooms", subjects);
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(output);
        } catch (Exception e) {
            serviceResult.setMessage("fail");
            serviceResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<List<ClassRoom>> getClassroomByTeacherIdAndYear(Integer teacherId, String year) {
        ServiceResult<List<ClassRoom>> serviceResult = new ServiceResult<>();
        try{
            List<ClassRoom> list = repository.getClassroomByTeacherIdAndYear(teacherId, year);
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(list);
        } catch (Exception e) {
            serviceResult.setMessage("fail");
            serviceResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<List<ClassRoom>> getClassManagedBy(String teacherCode, String year) {
        return new ServiceResult<>(HttpStatus.OK, "sucess", repository.getManageClassRooms(teacherCode, year));
    }
}
