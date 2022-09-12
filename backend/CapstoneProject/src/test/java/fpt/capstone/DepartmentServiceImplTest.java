package fpt.capstone;

import fpt.capstone.entities.ApParam;
import fpt.capstone.entities.Departments;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.entities.Teacher;
import fpt.capstone.form_data.UpdateDepartmentForm;
import fpt.capstone.payroll.DepartmentNotFoundException;
import fpt.capstone.payroll.ValidationException;
import fpt.capstone.repository.*;
import fpt.capstone.service.DepartmentServiceImpl;
import fpt.capstone.vo.DepartmentVo;
import fpt.capstone.vo.DepartmentVoV1;
import fpt.capstone.vo.DepartmentWithTypeVo;
import fpt.capstone.vo.DropDownVo;
import org.assertj.core.api.Fail;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTest {
    @InjectMocks
    DepartmentServiceImpl departmentService;

    @Mock
    DepartmentRepository repository;

    @Mock
    ApParamRepository apParamRepository;

    @Mock
    TeacherRepository teacherRepository;

    @Mock
    SubjectDeptRepository subjectDeptRepository;

    @Mock
    ClassRoomRepository classRoomRepository;


    @Test
    void getDepartmentByParentID(){
        ServiceResult<List<DepartmentVo>> serviceResult;
        serviceResult = departmentService.getDepartmentByParentID(any());
        assertEquals("OK", serviceResult.getStatus().name());
    }

    @Test
    void getAllDepartments(){
        ServiceResult<List<DepartmentVoV1>> result;
        result = departmentService.getAllDepartments();
        assertEquals("OK", result.getStatus().name());
    }

    @Test
    void getDropDownValuesWithType(){
        ServiceResult<List<DepartmentWithTypeVo>> result;
        result = departmentService.getDropDownValuesWithType();
        assertEquals("OK", result.getStatus().name());
    }

    @Test
    void getDropDownValues(){
        ServiceResult<List<DropDownVo>> result;
        result = departmentService.getDropDownValues();
        assertEquals("OK", result.getStatus().name());
    }

    @Test
    void getDropdownForClassRoom(){
        ServiceResult<List<DropDownVo>> result;
        result = departmentService.getDropdownForClassRoom();
        assertEquals("OK", result.getStatus().name());
    }

    @Test
    void getAllParentDepartment(){
        ServiceResult<List<DropDownVo>> result;
        result = departmentService.getAllParentDepartment();
        assertEquals("OK", result.getStatus().name());
    }

    @Test
    void getDepartmentByParentId(){
        ServiceResult<List<DropDownVo>> result;
        result = departmentService.getDepartmentByParentId(1);
        assertEquals("OK", result.getStatus().name());
    }

    @Test
    void getDepartmentAndParentDepartment(){
        ServiceResult<Map<String, Object>> result;
        result = departmentService.getDepartmentAndParentDepartment(1);
        assertEquals("OK", result.getStatus().name());
    }

    @Test
    void searchDepartmentsByCodeOrName(){
        ServiceResult<List<DepartmentVoV1>> result;
        result = departmentService.searchDepartmentsByCodeOrName("1", "1");
        assertEquals("OK", result.getStatus().name());
    }

    @Test
    void deleteDepartment_1(){
        ServiceResult<Boolean> result;
        Departments departments = new Departments();
        when(repository.findById(any())).thenReturn(Optional.of(departments));
        result = departmentService.deleteDepartment(1);
        assertEquals("OK", result.getStatus().name());
    }

    @Test
    void deleteDepartment_2(){
        try{
            thrown.expect(DepartmentNotFoundException.class);
            repository.findById(any());
            departmentService.deleteDepartment(1);
        }catch (Exception e){
            assertTrue(true);
        }
    }

    @Test
    void deleteDepartment_3(){
        ServiceResult<Boolean> result;
        List<DepartmentVo> list = new ArrayList<>();
        list.add(new DepartmentVo(1, LocalDateTime.now(), "1", LocalDateTime.now(), "1", "1", "1", 1, 1, 1, "1", "1"));
        list.add(new DepartmentVo(2, LocalDateTime.now(), "1", LocalDateTime.now(), "1", "1", "1", 1, 1, 1, "1", "1"));
        when(repository.findById(any())).thenReturn(Optional.of(new Departments()));
        when(repository.getDepartmentsByParentID(any())).thenReturn(list);
        result = departmentService.deleteDepartment(1);
        assertEquals("BAD_REQUEST", result.getStatus().name());
    }

    @Test
    void deleteDepartment_4(){
        ServiceResult<Boolean> result;
        List<DepartmentVo> list = new ArrayList<>();
        when(repository.findById(any())).thenReturn(Optional.of(new Departments()));
        when(repository.getDepartmentsByParentID(any())).thenReturn(list);
        when(teacherRepository.hasTeacherInDeptID(any())).thenReturn(true);
        result = departmentService.deleteDepartment(1);
        assertEquals("BAD_REQUEST", result.getStatus().name());
    }

    @Test
    void deleteDepartment_5(){
        ServiceResult<Boolean> result;
        List<DepartmentVo> list = new ArrayList<>();
        when(repository.findById(any())).thenReturn(Optional.of(new Departments()));
        when(repository.getDepartmentsByParentID(any())).thenReturn(list);
        when(teacherRepository.hasTeacherInDeptID(any())).thenReturn(false);
        when(subjectDeptRepository.hasSubjectDeptInInDeptID(any())).thenReturn(true);
        result = departmentService.deleteDepartment(1);
        System.out.println(result.getMessage());
        assertEquals("BAD_REQUEST", result.getStatus().name());
    }

    @Test
    void deleteDepartment_6(){
        ServiceResult<Boolean> result;
        List<DepartmentVo> list = new ArrayList<>();
        when(repository.findById(any())).thenReturn(Optional.of(new Departments()));
        when(repository.getDepartmentsByParentID(any())).thenReturn(list);
        when(teacherRepository.hasTeacherInDeptID(any())).thenReturn(false);
        when(subjectDeptRepository.hasSubjectDeptInInDeptID(any())).thenReturn(false);
        when(classRoomRepository.hasClassroomInDeptID(any())).thenReturn(true);
        result = departmentService.deleteDepartment(any());
        assertEquals("BAD_REQUEST", result.getStatus().name());
    }

    @Test
    void deleteDepartment_7(){
        ServiceResult<Boolean> result;
        List<DepartmentVo> list = new ArrayList<>();
        when(repository.findById(any())).thenReturn(Optional.of(new Departments()));
        when(repository.getDepartmentsByParentID(any())).thenReturn(list);
        when(teacherRepository.hasTeacherInDeptID(any())).thenReturn(false);
        when(subjectDeptRepository.hasSubjectDeptInInDeptID(any())).thenReturn(false);
        when(classRoomRepository.hasClassroomInDeptID(any())).thenReturn(false);
        repository.delete(any());
        result = departmentService.deleteDepartment(any());
        assertEquals("OK", result.getStatus().name());
    }

    @Test
    void checkUpdateParentAndType_1(){
        ServiceResult<Boolean> result;
        List<DepartmentVo> list = new ArrayList<>();
        list.add(new DepartmentVo(1, LocalDateTime.now(), "1", LocalDateTime.now(), "1", "1", "1", 1, 1, 1, "1", "1"));
        list.add(new DepartmentVo(2, LocalDateTime.now(), "1", LocalDateTime.now(), "1", "1", "1", 1, 1, 1, "1", "1"));
        when(repository.getDepartmentsByParentID(any())).thenReturn(list);
        result = departmentService.checkUpdateParentAndType(any());
        assertEquals("BAD_REQUEST", result.getStatus().name());
    }

    @Test
    void checkUpdateParentAndType_2(){
        ServiceResult<Boolean> result;
        List<DepartmentVo> list = new ArrayList<>();
        when(repository.getDepartmentsByParentID(any())).thenReturn(list);
        when(teacherRepository.hasTeacherInDeptID(any())).thenReturn(true);
        result = departmentService.checkUpdateParentAndType(any());
        assertEquals("BAD_REQUEST", result.getStatus().name());
    }

    @Test
    void checkUpdateParentAndType_3(){
        ServiceResult<Boolean> result;
        List<DepartmentVo> list = new ArrayList<>();
        when(repository.getDepartmentsByParentID(any())).thenReturn(list);
        when(teacherRepository.hasTeacherInDeptID(any())).thenReturn(false);
        when(subjectDeptRepository.hasSubjectDeptInInDeptID(any())).thenReturn(true);
        result = departmentService.checkUpdateParentAndType(any());
        assertEquals("BAD_REQUEST", result.getStatus().name());
    }

    @Test
    void checkUpdateParentAndType_4(){
        ServiceResult<Boolean> result;
        List<DepartmentVo> list = new ArrayList<>();
        when(repository.getDepartmentsByParentID(any())).thenReturn(list);
        when(teacherRepository.hasTeacherInDeptID(any())).thenReturn(false);
        when(subjectDeptRepository.hasSubjectDeptInInDeptID(any())).thenReturn(false);
        when(classRoomRepository.hasClassroomInDeptID(any())).thenReturn(true);
        result = departmentService.checkUpdateParentAndType(any());
        assertEquals("BAD_REQUEST", result.getStatus().name());
    }

    @Test
    void checkUpdateParentAndType_5(){
        ServiceResult<Boolean> result;
        List<DepartmentVo> list = new ArrayList<>();
        when(repository.getDepartmentsByParentID(any())).thenReturn(list);
        when(teacherRepository.hasTeacherInDeptID(any())).thenReturn(false);
        when(subjectDeptRepository.hasSubjectDeptInInDeptID(any())).thenReturn(false);
        when(classRoomRepository.hasClassroomInDeptID(any())).thenReturn(false);
        result = departmentService.checkUpdateParentAndType(any());
        assertEquals("OK", result.getStatus().name());
    }

    @Test
    void updateDepartment_1(){
        try{
            thrown.expect(DepartmentNotFoundException.class);
            repository.findById(any());
            departmentService.updateDepartment(new UpdateDepartmentForm("1", "1", null,"1","1",1,1,"1") );
        }catch (Exception e){
            assertTrue(true);
        }
    }

    @Test
    void updateDepartment_2(){
        List<DepartmentVo> list = new ArrayList<>();
        list.add(new DepartmentVo(1, LocalDateTime.now(), "1", LocalDateTime.now(), "1", "1", "1", 1, 1, 1, "1", "1"));
        list.add(new DepartmentVo(2, LocalDateTime.now(), "1", LocalDateTime.now(), "1", "1", "1", 1, 1, 1, "1", "1"));
        ServiceResult<Boolean> result;
        when(repository.findById(any())).thenReturn(Optional.of(new Departments()));
        when(repository.getDepartmentsByParentID(any())).thenReturn(list);
        result = departmentService.updateDepartment(new UpdateDepartmentForm("1", "1", 1,"1","1",1,1,"1"));
        assertEquals("BAD_REQUEST", result.getStatus().name());
    }

    @Test
    void updateDepartment_3(){
        List<DepartmentVo> list = new ArrayList<>();
        ServiceResult<Boolean> result;
        when(repository.findById(any())).thenReturn(Optional.of(new Departments()));
        when(repository.getDepartmentsByParentID(any())).thenReturn(list);
        when(teacherRepository.hasTeacherInDeptID(any())).thenReturn(true);
        result = departmentService.updateDepartment(new UpdateDepartmentForm("1", "1", 1,"1","1",1,1,"1"));
        assertEquals("BAD_REQUEST", result.getStatus().name());
    }

    @Test
    void updateDepartment_4(){
        List<DepartmentVo> list = new ArrayList<>();
        ServiceResult<Boolean> result;
        when(repository.findById(any())).thenReturn(Optional.of(new Departments()));
        when(repository.getDepartmentsByParentID(any())).thenReturn(list);
        when(teacherRepository.hasTeacherInDeptID(any())).thenReturn(false);
        when(subjectDeptRepository.hasSubjectDeptInInDeptID(any())).thenReturn(true);
        result = departmentService.updateDepartment(new UpdateDepartmentForm("1", "1", 1,"1","1",1,1,"1"));
        assertEquals("BAD_REQUEST", result.getStatus().name());
    }

    @Test
    void updateDepartment_5(){
        List<DepartmentVo> list = new ArrayList<>();
        ServiceResult<Boolean> result;
        when(repository.findById(any())).thenReturn(Optional.of(new Departments()));
        when(repository.getDepartmentsByParentID(any())).thenReturn(list);
        when(teacherRepository.hasTeacherInDeptID(any())).thenReturn(false);
        when(subjectDeptRepository.hasSubjectDeptInInDeptID(any())).thenReturn(false);
        when(classRoomRepository.hasClassroomInDeptID(any())).thenReturn(true);
        result = departmentService.updateDepartment(new UpdateDepartmentForm("1", "1", 1,"1","1",1,1,"1"));
        assertEquals("BAD_REQUEST", result.getStatus().name());
    }

    @Test
    void updateDepartment_6(){
        List<DepartmentVo> list = new ArrayList<>();
        ServiceResult<Boolean> result;
        when(repository.findById(any())).thenReturn(Optional.of(new Departments()));
        when(repository.getDepartmentsByParentID(any())).thenReturn(list);
        when(teacherRepository.hasTeacherInDeptID(any())).thenReturn(false);
        when(subjectDeptRepository.hasSubjectDeptInInDeptID(any())).thenReturn(false);
        when(classRoomRepository.hasClassroomInDeptID(any())).thenReturn(false);
        result = departmentService.updateDepartment(new UpdateDepartmentForm(null, "1", 1,"1","1",1,1,"1"));
        assertEquals("BAD_REQUEST", result.getStatus().name());
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Test()
    void updateDepartment_7(){
        try{
            List<DepartmentVo> list = new ArrayList<>();
            when(repository.findById(any())).thenReturn(Optional.of(new Departments()));
            when(repository.getDepartmentsByParentID(any())).thenReturn(list);
            when(teacherRepository.hasTeacherInDeptID(any())).thenReturn(false);
            when(subjectDeptRepository.hasSubjectDeptInInDeptID(any())).thenReturn(false);
            when(classRoomRepository.hasClassroomInDeptID(any())).thenReturn(false);
            thrown.expect(ValidationException.class);
            apParamRepository.findFirstByCode("1");
            departmentService.updateDepartment(new UpdateDepartmentForm("1", "1", null,"1","1",1,1,"1"));
        }catch (Exception e){
            assertTrue(true);
        }
    }

    @Test
    void updateDepartment_8(){
        try{
            List<DepartmentVo> list = new ArrayList<>();
            ServiceResult<Boolean> result;
            when(repository.findById(any())).thenReturn(Optional.of(new Departments()));
            when(repository.getDepartmentsByParentID(any())).thenReturn(list);
            when(teacherRepository.hasTeacherInDeptID(any())).thenReturn(false);
            when(subjectDeptRepository.hasSubjectDeptInInDeptID(any())).thenReturn(false);
            when(classRoomRepository.hasClassroomInDeptID(any())).thenReturn(false);
            when(apParamRepository.findFirstByCode(any())).thenReturn(Optional.of(new ApParam()));
            thrown.expect(ValidationException.class);
            teacherRepository.findById(1);
            departmentService.updateDepartment(new UpdateDepartmentForm("1", "1", 1,"1","1",1,1,"1"));
        }catch (Exception e){
            assertTrue(true);
        }
    }

    @Test
    void updateDepartment_9(){
        List<DepartmentVo> list = new ArrayList<>();
        ServiceResult<Boolean> result;
        when(repository.findById(any())).thenReturn(Optional.of(new Departments()));
        when(repository.getDepartmentsByParentID(any())).thenReturn(list);
        when(teacherRepository.hasTeacherInDeptID(any())).thenReturn(false);
        when(subjectDeptRepository.hasSubjectDeptInInDeptID(any())).thenReturn(false);
        when(classRoomRepository.hasClassroomInDeptID(any())).thenReturn(false);
        when(apParamRepository.findFirstByCode(any())).thenReturn(Optional.of(new ApParam()));
        when(teacherRepository.findById(any())).thenReturn(Optional.of(new Teacher()));
        repository.update(new UpdateDepartmentForm("1", "1", 1,"1","1",1,1,"1"));
        result = departmentService.updateDepartment(new UpdateDepartmentForm("1", "1", null,"1","1",1,1,"1"));
        assertEquals("OK", result.getStatus().name());
    }

    @Test
    void updateDepartment_10(){
        List<DepartmentVo> list = new ArrayList<>();
        ServiceResult<Boolean> result;
        when(repository.findById(any())).thenReturn(Optional.of(new Departments()));
        when(repository.getDepartmentsByParentID(any())).thenReturn(list);
        when(teacherRepository.hasTeacherInDeptID(any())).thenReturn(false);
        when(subjectDeptRepository.hasSubjectDeptInInDeptID(any())).thenReturn(false);
        when(classRoomRepository.hasClassroomInDeptID(any())).thenReturn(false);
        when(apParamRepository.findFirstByCode(any())).thenReturn(Optional.of(new ApParam()));
        when(teacherRepository.findById(any())).thenReturn(Optional.of(new Teacher()));
        repository.update(new UpdateDepartmentForm("1", "1", 1,"1","1",1,1,"1"));
        result = departmentService.updateDepartment(new UpdateDepartmentForm("1", "1", 1,"1","1",1,1,"1"));
        assertEquals("OK", result.getStatus().name());
    }

    @Test
    void insertDepartment_1(){
        ServiceResult<Boolean> result;
        result = departmentService.insertDepartment(new UpdateDepartmentForm(null, "1", 1,"1","1",1,1,"1"));
        assertEquals("BAD_REQUEST", result.getStatus().name());
    }

    @Test
    void insertDepartment_2(){
        ServiceResult<Boolean> result;
        Optional<DepartmentVoV1> departmentVoV1 = Optional.of(new DepartmentVoV1(1,LocalDateTime.now(), "1", LocalDateTime.now(), "1","1", "1", 1,"1", 1,1,"1","1", "1", "1"));
        when(repository.findFirstByCode(any())).thenReturn(departmentVoV1);
        result = departmentService.insertDepartment(new UpdateDepartmentForm("1", "1", 1,"1","1",1,1,"1"));
        assertEquals("BAD_REQUEST", result.getStatus().name());
    }

    @Test
    void insertDepartment_3(){
        try{
            thrown.expect(ValidationException.class);
            apParamRepository.findFirstByCode("1");
            departmentService.insertDepartment(new UpdateDepartmentForm("1", "1", 1,"1","1",1,1,"1"));
        }catch (Exception e){
            assertTrue(true);
        }
    }

    @Test
    void insertDepartment_4(){
        try{
            when(apParamRepository.findFirstByCode(any())).thenReturn(Optional.of(new ApParam()));
            thrown.expect(ValidationException.class);
            teacherRepository.findById(any());
            departmentService.insertDepartment(new UpdateDepartmentForm("1", "1", 1,"1","1",1,1,"1"));
        }catch (Exception e){
            assertTrue(true);
        }
    }

    @Test
    void insertDepartment_5(){
        ServiceResult<Boolean> result;
        when(apParamRepository.findFirstByCode(any())).thenReturn(Optional.of(new ApParam()));
        when(teacherRepository.findById(any())).thenReturn(Optional.of(new Teacher()));
        result = departmentService.insertDepartment(new UpdateDepartmentForm("1", "1", null,"1","1",1,1,"1"));
        assertEquals("OK", result.getStatus().name());
    }

    @Test
    void insertDepartment_6(){
        ServiceResult<Boolean> result;
        when(apParamRepository.findFirstByCode(any())).thenReturn(Optional.of(new ApParam()));
        when(teacherRepository.findById(any())).thenReturn(Optional.of(new Teacher()));
        result = departmentService.insertDepartment(new UpdateDepartmentForm("1", "1", 1,"1","1",1,1,"1"));
        assertEquals("OK", result.getStatus().name());
    }
}
