package fpt.capstone.service;

import fpt.capstone.entities.Teacher;
import fpt.capstone.entities.User;
import fpt.capstone.form_data.TeacherForm;
import fpt.capstone.payroll.ValidationException;
import fpt.capstone.repository.TeacherRepository;
import fpt.capstone.repository.UserRepository;
import fpt.capstone.repository.DepartmentRepository;
import fpt.capstone.utility.PhoneCheck;
import fpt.capstone.utility.Regex;
import fpt.capstone.vo.DepartmentVoV1;
import fpt.capstone.vo.DropDownVo;
import org.springframework.beans.factory.annotation.Autowired;
import fpt.capstone.entities.ServiceResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherRepository repository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserService userService;

    @Override
    public ServiceResult<List<DropDownVo>> getDropDownValues(Integer depID) {
        return new ServiceResult<>(HttpStatus.OK, "Teacher drop down", repository.getDropDownValues(depID));
    }

    @Override
    public ServiceResult<List<DropDownVo>> getAllTeacherOfRoot(Integer depID) {

        List<DepartmentVoV1> allDep = departmentRepository.getAllDepartments();
        if (allDep.size() == 0) {
            return new ServiceResult<>(HttpStatus.OK, "Teacher drop down", new ArrayList<>());
        }
        Map<Integer, List<Integer>> depTreChildMap = new HashMap<>();
        Map<Integer, Integer> depTreParentMap = new HashMap<>();
        for (DepartmentVoV1 d : allDep) {
            int child = d.getId();
            int parent = d.getParent().getId();
            depTreParentMap.put(child, parent);
            if (!depTreChildMap.containsKey(parent)) {
                depTreChildMap.put(parent, new ArrayList<>());
            }
            depTreChildMap.get(parent).add(child);
        }

        int root = findRootParent(depTreParentMap, depID);
        List<Integer> allChild = depTreChildMap.get(root);
        if (allChild == null) {
            allChild = new ArrayList<>();
        }
        allChild.add(root);
        List<DropDownVo> result = new ArrayList<>();
        for (Integer leaf : allChild) {
            result.addAll(repository.getDropDownValues(leaf));
        }


        return new ServiceResult<>(HttpStatus.OK, "Teacher drop down", result);
    }

    private int findRootParent(Map<Integer, Integer> depTreParentMap, Integer leafId) {
        if (!depTreParentMap.containsKey(leafId)) {
            return 0;
        }
        if (depTreParentMap.get(leafId) == 0) {
            return leafId;
        } else {
            return findRootParent(depTreParentMap, depTreParentMap.get(leafId));
        }
    }

    @Override
    public ServiceResult<Map<String, Object>> getAllteacher() {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        Map<String, Object> output = new HashMap<>();
        output.put("Teachers", repository.getAllTeacher());
        serviceResult.setStatus(HttpStatus.OK);
        serviceResult.setMessage("Success");
        serviceResult.setData(output);
        return serviceResult;
    }

    @Override
    public ServiceResult<Map<String, Object>> getAllteacherGVCN() {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        Map<String, Object> output = new HashMap<>();
        output.put("Teachers", repository.getAllTeacherDangLamOrTamNghi());
        serviceResult.setStatus(HttpStatus.OK);
        serviceResult.setMessage("Success");
        serviceResult.setData(output);
        return serviceResult;
    }

    @Override
    public ServiceResult<Map<String, Object>> getAllTeacherByCodeOrFullNameAndAuthority(String codeName, String authorityCode, Integer deptId) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        Map<String, Object> output = new HashMap<>();
        output.put("Teachers", repository.getAllTeacherByCodeOrFullNameAndAuthority(codeName, authorityCode, deptId));
        serviceResult.setStatus(HttpStatus.OK);
        serviceResult.setMessage("Success");
        serviceResult.setData(output);
        return serviceResult;
    }

    @Override
    public ServiceResult<Map<String, Object>> getTeacherDetailById(Integer teacherId) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        Map<String, Object> checkDepart = repository.checkDepartParent(repository.getById(teacherId).getDeptID());
        Map<String, Object> teacherFullData = new HashMap<>();
        System.out.println("id : " + teacherId);
        try {
            if (checkDepart.get("check3").toString().equals("1")) {
                teacherFullData = repository.getTeacherDetailById3(teacherId);

            }
            if (checkDepart.get("check2").toString().equals("1")) {
                teacherFullData = repository.getTeacherDetailById2(teacherId);

            }
            if (checkDepart.get("check1").toString().equals("1")) {
                teacherFullData = repository.getTeacherDetailById1(teacherId);

            }
            if (teacherFullData.size() == 0) {
                serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                serviceResult.setMessage("Kh??ng t??m th???y gi??o vi??n");
            } else {
                serviceResult.setStatus(HttpStatus.OK);
                serviceResult.setMessage("Success");
                serviceResult.setData(teacherFullData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
            serviceResult.setMessage("fail");
            serviceResult.setData(teacherFullData);
        }

        return serviceResult;
    }

    @Override
    public List<Teacher> getTeacherByCode(String teacherCode) {
        List<Teacher> teacher = repository.findByCode(teacherCode);
        return teacher;
    }

    @Override
    public ServiceResult<Boolean> insert(TeacherForm form) {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username;
//        if (principal instanceof UserDetails) {
//            username = ((UserDetails) principal).getUsername();
//        } else {
//            username = principal.toString();
//        }
        DateTimeFormatter ft = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter ft2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (form.getCode().length() == 0) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "M?? c??n b??? kh??ng ???????c ????? tr???ng", true);
        } else {
            if (repository.findByCode(form.getCode()).size() > 0) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Ma?? c??n b??? ??a?? t????n ta??i", true);
            }
        }
        if (form.getFullName().length() == 0) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thi????u th??ng tin t??n ca??n b????", true);
        }
        if (form.getStartDate() == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thi????u th??ng tin ng??y v??o tr?????ng", true);
        }
        if (form.getContractType() == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thi????u th??ng tin lo???i h???p ?????ng", true);
        }
        if (form.getPhone() == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thi????u th??ng tin s??? ??i???n tho???i", true);
        } else {
            boolean check = false;
            for (String phone : PhoneCheck.phoneHead) {
                if (form.getPhone().startsWith(phone) && form.getPhone().length() == 10) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "S??? ??i???n tho???i sai ?????nh d???ng", true);
            }
        }
        if (form.getBirthDay() == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thi???u th??ng tin ng??y sinh", true);
        }

        if (form.getIdentityCard().length() != 0) {
            if (!form.getIdentityCard().matches(Regex.onlyNumber)) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "S??? CMND/CCCD sai ?????nh d???ng", true);
            }
        } else {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "S??? CMND/CCCD sai ?????nh d???ng", true);
        }
        if (form.getIssuedDate() == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thi???u th??ng tin ng??y c???p CMND/CCCD", true);
        }
        if (form.getSex() == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thi???u th??ng tin gi???i t??nh", true);
        }
        try {
            Teacher teacher = new Teacher(form.getImage(), LocalDateTime.now(), "admin", form.getFullName(), form.getCode(), form.getDeptId(), LocalDateTime.parse(form.getStartDate(), ft), form.getContractType(), form.getPhone(), form.getEmail(), LocalDate.parse(form.getBirthDay(), ft2), form.getHomeTown(), form.getNation(), form.getPermanentAddress(), form.getSocialInsuranceNumber(), form.getIdentityCard(), form.getIssuedAddress(), LocalDateTime.parse(form.getIssuedDate(), ft), form.getMarriageStatus(), form.getSex(), 0, form.getReligion(), form.getTemporaryAddress());
            repository.save(teacher);
            boolean insertUser = userService.insertUserForTeacher(form.getCode(), form.getFullName(), form.getPhone(), form.getImage(), form.getEmail(), form.getAuthorities(), "admin");
            if (!insertUser) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "T???o t??i kho???n cho gi??o vi??n kh??ng th??nh c??ng", true);
            }
            return new ServiceResult<>(HttpStatus.OK, "Th??m c??n b??? th??nh c??ng", true);
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Th??m c??n b??? th???t b???i", true);
        }
    }

    @Override
    public ServiceResult<Boolean> update(TeacherForm form, Integer id) {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username;
//        if (principal instanceof UserDetails) {
//            username = ((UserDetails) principal).getUsername();
//        } else {
//            username = principal.toString();
//        }
        DateTimeFormatter ft = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter ft2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Teacher teacherOld = repository.getById(id);
        if (repository.findById(id) == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Kh??ng t??m th???y c??n b???", true);
        }

        if (form.getFullName().length() == 0) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thi????u th??ng tin t??n gi??o vi??n", true);
        }
        if (form.getStartDate() == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thi????u th??ng tin ng??y v??o tr?????ng", true);
        }
        if (form.getContractType() == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thi????u th??ng tin lo???i h???p ?????ng", true);
        }
        if (form.getPhone() == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thi????u th??ng tin s??? ??i???n tho???i", true);
        } else {
            boolean check = false;
            if (!form.getPhone().matches(Regex.onlyNumber)) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "S??? ??i???n tho???i sai ?????nh d???ng", true);
            }
            for (String phone : PhoneCheck.phoneHead) {
                if (form.getPhone().startsWith(phone) && form.getPhone().length() == 10) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "S??? ??i???n tho???i sai ?????nh d???ng", true);

            }
        }
        if (form.getBirthDay() == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thi???u th??ng tin ng??y sinh", true);
        }
        if (form.getIdentityCard().length() != 0) {
            if (!form.getIdentityCard().matches(Regex.onlyNumber)) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "S??? CMND/CCCD sai ?????nh d???ng", true);
            }
        } else {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "S??? CMND/CCCD sai ?????nh d???ng", true);
        }
        if (form.getIssuedDate() == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thi???u th??ng tin ng??y c???p CMND/CCCD", true);
        }
        if (form.getSex() == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thi???u th??ng tin gi???i t??nh", true);
        }
        teacherOld.setBirthDay(LocalDate.parse(form.getBirthDay(), ft2));
        teacherOld.setContractType(form.getContractType());
        teacherOld.setDeptID(form.getDeptId());
        teacherOld.setEmail(form.getEmail());
        teacherOld.setFullName(form.getFullName());
        teacherOld.setHomeTown(form.getHomeTown());
        teacherOld.setIdentityCard(form.getIdentityCard());
        teacherOld.setUpdateName("admin");
        teacherOld.setUpdateTime(LocalDateTime.now());
        teacherOld.setStartDate(LocalDateTime.parse(form.getStartDate(), ft));
        teacherOld.setPermanentAddress(form.getPermanentAddress());
        teacherOld.setMarriageStatus(form.getMarriageStatus());
        teacherOld.setSex(form.getSex());
        teacherOld.setIssuedAddress(form.getIssuedAddress());
        teacherOld.setIssuedDate(LocalDateTime.parse(form.getIssuedDate(), ft));
        teacherOld.setReligion(form.getReligion());
        teacherOld.setNation(form.getNation());
        teacherOld.setTemporaryAddress(form.getTemporaryAddress());
        teacherOld.setPhone(form.getPhone());
        teacherOld.setAvatar(form.getImage());
        teacherOld.setStatus(form.getStatus());
        repository.save(teacherOld);
        boolean updateUser = userService.updateUserForTeacher(form.getCode(), form.getFullName(), form.getPhone(), form.getImage(), form.getEmail(), form.getAuthorities(), "admin");
        if (!updateUser) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "C???p nh???t t??i kho???n c??n b??? kh??ng th??nh c??ng", true);
        }
        return new ServiceResult<>(HttpStatus.OK, "C???p nh???t th??ng tin th??nh c??ng", true);

    }

    @Override
    public ServiceResult<Map<String, Object>> getAllTeacherForTeachingAssignment(Integer subjectId) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        Map<String, Object> output = new HashMap<>();
        try {
            output.put("Teachers",  repository.getAllTeacherForTeachingAssignment(subjectId));
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setMessage("Success");
            serviceResult.setData(output);
        } catch (Exception e) {
            e.printStackTrace();
            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
            serviceResult.setMessage("Fail");
            serviceResult.setData(output);
        }
        return serviceResult;
    }
}
