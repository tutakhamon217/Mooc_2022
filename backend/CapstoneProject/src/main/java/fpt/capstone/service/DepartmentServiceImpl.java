package fpt.capstone.service;


import fpt.capstone.form_data.UpdateDepartmentForm;
import fpt.capstone.entities.ApParam;
import fpt.capstone.entities.Departments;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.entities.Teacher;
import fpt.capstone.payroll.DepartmentNotFoundException;
import fpt.capstone.payroll.ValidationException;
import fpt.capstone.repository.*;
import fpt.capstone.repository.DepartmentRepository;
import fpt.capstone.repository.TeacherRepository;
import fpt.capstone.repository.SubjectDeptRepository;
import fpt.capstone.vo.DepartmentVo;
import fpt.capstone.vo.DepartmentVoV1;
import fpt.capstone.vo.DepartmentWithTypeVo;
import fpt.capstone.vo.DropDownVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository repository;


    @Autowired
    private ApParamRepository apParamRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubjectDeptRepository subjectDeptRepository;

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Override
    public ServiceResult<List<DepartmentVo>> getDepartmentByParentID(Integer parent_id) {
        return new ServiceResult<>(HttpStatus.OK, "deprartment by parent id", repository.getDepartmentsByParentID(parent_id));
    }


    @Override
    public ServiceResult<List<DepartmentVoV1>> searchDepartmentsByCodeOrName(String code, String name) {
        return new ServiceResult<>(HttpStatus.OK, "Search results", repository.searchDepartmentsByCodeOrName(code.toUpperCase(), name.toUpperCase()));
    }


    @Override
    public ServiceResult<Boolean> deleteDepartment(Integer id) {
        Departments departments;
        try {
            departments = repository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id.toString()));
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Mã phòng ban không tồn tại", true);
        }
        if (repository.getDepartmentsByParentID(id).size() > 0 ) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Không thể xóa phòng ban đang có phòng ban con thuộc", true);
        }
        if (teacherRepository.hasTeacherInDeptID(id)) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Không thể xóa phòng ban đang có cán bộ nhân viên thuộc", true);
        }
        if (subjectDeptRepository.hasSubjectDeptInInDeptID(id)) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Không thể xóa phòng ban đang có môn học thuộc thuộc", true);
        }
        if (classRoomRepository.hasClassroomInDeptID(id)) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Không thể xóa phòng ban đang có lớp học thuộc thuộc", true);
        }

        repository.delete(departments);
        return new ServiceResult<>(HttpStatus.OK, "Xóa thành công", true);
    }

    @Override
    public ServiceResult<Boolean> checkUpdateParentAndType(Integer id) {
        if (repository.getDepartmentsByParentID(id).size() > 0 ) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Đang có phòng ban con thuộc", true);
        }
        if (teacherRepository.hasTeacherInDeptID(id)) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Đang có cán bộ nhân viên thuộc", true);
        }
        if (subjectDeptRepository.hasSubjectDeptInInDeptID(id)) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Đang có môn học thuộc thuộc", true);
        }
        if (classRoomRepository.hasClassroomInDeptID(id)) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Đang có lớp học thuộc thuộc", true);
        }
        return new ServiceResult<>(HttpStatus.OK, "Có thể chỉnh sửa", true);
    }

    @Override
    public ServiceResult<Boolean> updateDepartment(UpdateDepartmentForm form) {

        Departments departments;
        Integer id = Integer.parseInt(form.getId());
        try {
            departments = repository.findById(id).orElseThrow(() -> new DepartmentNotFoundException(id.toString()));
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Mã phòng ban không tồn tại", true);
        }

        if(!departments.getParentID().equals(form.getParentID()) || !departments.getType().equals(form.getType())) {
            if (repository.getDepartmentsByParentID(id).size() > 0 ) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Không thể thay phòng ban cha hoặc loại đơn vị của phòng ban đang có phòng ban con thuộc", true);
            }
            if (teacherRepository.hasTeacherInDeptID(id)) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Không thể thay phòng ban cha hoặc loại đơn vị của phòng ban đang có cán bộ nhân viên thuộc", true);
            }
            if (subjectDeptRepository.hasSubjectDeptInInDeptID(id)) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Không thể thay phòng ban cha hoặc loại đơn vị của phòng ban đang có môn học thuộc thuộc", true);
            }
            if (classRoomRepository.hasClassroomInDeptID(id)) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Không thể thay phòng ban cha hoặc loại đơn vị của phòng ban đang có lớp học thuộc thuộc", true);
            }
        }


        if (form.getCode() == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Mã phòng ban rỗng", true);
        }
        if (form.getParentID() == null) {
            form.setParentID(0);
        }
        ApParam checkedType = apParamRepository.findFirstByCode(form.getType()).orElseThrow(() -> new ValidationException("type", "Type is not found"));
        if (form.getEmployeeID() != null) {
            Teacher foundTeacher = teacherRepository.findById(form.getEmployeeID()).orElseThrow(() -> new ValidationException("employee_id", "Không tìm thấy giáo viên"));
        }

        repository.update(form);

        return new ServiceResult<>(HttpStatus.OK, "Updated", true);
    }

    @Override
    public ServiceResult<Boolean> insertDepartment(UpdateDepartmentForm form) {
        if (form.getCode() == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Mã khoa ban rỗng", true);
        }

        Optional<DepartmentVoV1> d = repository.findFirstByCode(form.getCode());

        if (d.isPresent()) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Mã khoa ban đã tồn tại", true);
        }

        if (form.getParentID() == null) {
            form.setParentID(0);
        }
        try {
            ApParam checkedType = apParamRepository.findFirstByCode(form.getType()).orElseThrow(() -> new ValidationException("type", "Type is not found"));
        } catch (Exception e){
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Loại đơn vị không tồn tại", true);
        }
        
        if (form.getEmployeeID() != null) {
            Teacher foundTeacher = teacherRepository.findById(form.getEmployeeID()).orElseThrow(() -> new ValidationException("employee_id", "Employee not found"));
        }

        List<Departments> savedDepartments = new ArrayList<>();
        savedDepartments.add(new Departments(LocalDateTime.now(), "", LocalDateTime.now(), "", form.getName(), form.getCode(), form.getParentID(), form.getPosition(), form.getEmployeeID(), form.getDescription(), form.getType()));
        repository.saveAll(savedDepartments);

        return new ServiceResult<>(HttpStatus.OK, "Thêm mới khoa ban thành công", true);
    }

    @Override
    public ServiceResult<List<DepartmentVoV1>> getAllDepartments() {
        return new ServiceResult<>(HttpStatus.OK, "All departments", repository.getAllDepartments());
    }

    @Override
    public ServiceResult<List<DepartmentWithTypeVo>> getDropDownValuesWithType() {
        return new ServiceResult<>(HttpStatus.OK, "Department Dropdown values", repository.getDropDownWithTypeValues());
    }

    @Override
    public ServiceResult<List<DropDownVo>> getDropDownValues() {
        return new ServiceResult<>(HttpStatus.OK, "Department Dropdown values", repository.getDropDownValues());
    }

    @Override

    public ServiceResult<List<DropDownVo>> getDropdownForClassRoom() {
        return new ServiceResult<>(HttpStatus.OK, "Dropdown for classroom", repository.getDropdownForClassRoom());

    }

    @Override
    public ServiceResult<List<DropDownVo>> getAllParentDepartment() {
        return new ServiceResult<>(HttpStatus.OK, "Dropdown for parent department", repository.getAllParentDepartment());
    }

    @Override
    public ServiceResult<List<DropDownVo>> getDepartmentByParentId(int parentId) {
        return new ServiceResult<>(HttpStatus.OK, "Dropdown for department get by parentId", repository.getDepartmentByParentId(parentId));
    }

    @Override
    public ServiceResult<Map<String, Object>> getDepartmentAndParentDepartment(int id) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
//        try {
            List<Map<String, Object>> departments = repository.getDepartmentAndParentDepartment(id);
            System.out.println("size" + departments.size());
            Map<String, Object> output = new HashMap<>();
            output.put("departments", departments);
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(output);
//        } catch (Exception e) {
//            e.printStackTrace();
//            serviceResult.setMessage("fail");
//            serviceResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
        return serviceResult;
    }

}
