package fpt.capstone;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.entities.Teacher;
import fpt.capstone.form_data.TeacherForm;
import fpt.capstone.repository.TeacherRepository;
import fpt.capstone.service.TeacherServiceImpl;
import fpt.capstone.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceImplTest {
    @InjectMocks
    TeacherServiceImpl teacherService;
    @Mock
    TeacherRepository teacherRepository;
    @Mock
    SecurityContextHolder securityContextHolder;
    @Mock
    UserService userService;

    //code empty
    @Test()
    void insertTest1() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "", "Lương Văn A", 1, 34, authorities, "2021-08-18 20:03:55", 0, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        ServiceResult<Boolean> actual = teacherService.insert(teacherForm);
        assertEquals("Mã cán bộ không được để trống", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    //code exist
    @Test()
    void insertTest2() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB012022", "Lương Văn A", 1, 34, authorities, "2021-08-18 20:03:55", 0, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        Teacher teacher = new Teacher(10, null, "admin", null, null, null, "tuanvtq", "MCB012022", 1, null, 1,
                "1979-05-09 00:00:00", "Thanh Hóa", null, null, null, null, null, null, null, null, null, null, null, null, null);
        List<Teacher> list = new ArrayList<>();
        list.add(teacher);
        when(teacherRepository.findByCode(any())).thenReturn(list);
        ServiceResult<Boolean> actual = teacherService.insert(teacherForm);
        assertEquals("Mã cán bộ đã tồn tại", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    //full name empty
    @Test()
    void insertTest3() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB012022", "", 1, 34, authorities, "2021-08-18 20:03:55", 0, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        ServiceResult<Boolean> actual = teacherService.insert(teacherForm);
        assertEquals("Thiếu thông tin tên cán bộ", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    //start date empty
    @Test()
    void insertTest4() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB012022", "tuan", 1, 34, authorities, null, 0, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        ServiceResult<Boolean> actual = teacherService.insert(teacherForm);
        assertEquals("Thiếu thông tin ngày vào trường", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    //contractType empty
    @Test()
    void insertTest5() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB012022", "tuan", 1, 34, authorities, "2021-08-18 20:03:55", null, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        ServiceResult<Boolean> actual = teacherService.insert(teacherForm);
        assertEquals("Thiếu thông tin loại hợp đồng", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    @Test()
    void insertTest6() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB012022", "tuan", 1, 34, authorities, "2021-08-18 20:03:55", 1, null, "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        ServiceResult<Boolean> actual = teacherService.insert(teacherForm);
        assertEquals("Thiếu thông tin số điện thoại", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    @Test()
    void insertTest7() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB012022", "tuan", 1, 34, authorities, "2021-08-18 20:03:55", 1, "03873993452", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        ServiceResult<Boolean> actual = teacherService.insert(teacherForm);
        assertEquals("Số điện thoại sai định dạng", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    @Test()
    void insertTest9() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "Lương Văn A", 1, 34, authorities, "2021-08-18 20:03:55", 0, "0387399342", "tuan@gmail.com", null, "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        ServiceResult<Boolean> actual = teacherService.insert(teacherForm);
        assertEquals("Thiếu thông tin ngày sinh", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    @Test()
    void insertTest10() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "Lương Văn A", 1, 34, authorities, "2021-08-18 20:03:55", 0, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794d", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        ServiceResult<Boolean> actual = teacherService.insert(teacherForm);
        assertEquals("Số CMND/CCCD sai định dạng", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

//    @Test()
//    void insertTest11() {
//        Set<String> authorities = new HashSet<>();
//        authorities.add("ROLE_GVBM");
//        TeacherForm teacherForm = new TeacherForm(
//                null, "MCB123456", "Lương Văn A", 1, 34, authorities, "2021-08-18 20:03:55", 0, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
//                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "01720000479411", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
//        );
//        when(teacherRepository.save(any())).thenReturn(false);
//        ServiceResult<Boolean> actual = teacherService.insert(teacherForm);
//        assertEquals("Thêm cán bộ thất bại", actual.getMessage());
//        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
//    }

    @Test()
    void insertTest12() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "Lương Văn A", 1, 34, authorities, "2021-08-18 20:03:55", 0, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        ServiceResult<Boolean> actual = teacherService.insert(teacherForm);
        assertEquals("Số CMND/CCCD sai định dạng", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    @Test()
    void insertTest13() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "Lương Văn A", 1, 34, authorities, "2021-08-18 20:03:55", 0, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", null, 0, 0
        );
        ServiceResult<Boolean> actual = teacherService.insert(teacherForm);
        assertEquals("Thiếu thông tin ngày cấp CMND/CCCD", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    @Test()
    void insertTest14() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "Lương Văn A", 1, 34, authorities, "2021-08-18 20:03:55", 0, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2000-08-18", 1, null
        );
        ServiceResult<Boolean> actual = teacherService.insert(teacherForm);
        assertEquals("Thiếu thông tin giới tính", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    @Test()
    void insertTest15() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                "null", "MCB123456", "Lương Văn A", 1, 34, authorities, "2021-08-18 20:03:55", 0, "0387399342", "tuan@gmail.com", "1993-05-29", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 1, 1
        );

        when(teacherRepository.save(any())).thenReturn(new Teacher());
/*//        when(userService.insertUserForTeacher(any(),any(),any(),any(),any(),any(),any())).thenReturn(false);*/

        ServiceResult<Boolean> actual = teacherService.insert(teacherForm);

        assertEquals("Tạo tài khoản cho giáo viên không thành công", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

        @Test()
    void insertTest16(){
        Set<String> authorities=new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm=new TeacherForm(
                null,"MCB123456","Lương Văn A",1,34,authorities,"2021-08-18 20:03:55",0,"0387399342","tuan@gmail.com","2000-08-18","Phat","Kinh","Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi","12, Huu Nghi, Hoa Binh","1234567890","017200004794","phong quan ly xã hoi","2021-08-18 20:03:55",1,1
        );

        when(userService.insertUserForTeacher(any(),any(),any(),any(),any(),any(),any())).thenReturn(true);
        ServiceResult<Boolean>actual=teacherService.insert(teacherForm);
        assertEquals("Thêm cán bộ thành công", actual.getMessage());
        assertEquals(HttpStatus.OK, actual.getStatus());
    }
    @Test()
    void insertTest17() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "Lương Văn A", 1, 34, authorities, "2021-08-18 20:03:55", 0, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 1, 1
        );
        when(teacherRepository.save(any())).thenThrow( RuntimeException.class);
        ServiceResult<Boolean> actual = teacherService.insert(teacherForm);
        assertEquals("Thêm cán bộ thất bại", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    @Test()
    void updateTest1() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "Lương Văn A", 1, 34, authorities, "2021-08-18 20:03:55", 0, "0387399342", "tuan@gmail.com", null, "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        Teacher teacher = new Teacher(10, null, "admin", null, null, null, "tuanvtq", "MCB012022", 1, null, 1,
                "1979-05-09 00:00:00", "Thanh Hóa", null, null, null, null, null, null, null, null, null, null, null, null, null);
        when(teacherRepository.getById(any())).thenReturn(teacher);
        when(teacherRepository.findById(any())).thenReturn(Optional.of(teacher));
        ServiceResult<Boolean> actual = teacherService.update(teacherForm, 10);
        assertEquals("Thiếu thông tin ngày sinh", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    @Test()
    void updateTest2() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "Lương Văn A", 1, 34, authorities, "2021-08-18 20:03:55", 0, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        when(teacherRepository.findById(any())).thenReturn(null);
        ServiceResult<Boolean> actual = teacherService.update(teacherForm, 10);
        assertEquals("Không tìm thấy cán bộ", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    @Test()
    void updateTest3() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "", 1, 34, authorities, "2021-08-18 20:03:55", 0, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        Teacher teacher = new Teacher(10, null, "admin", null, null, null, "tuanvtq", "MCB012022", 1, null, 1,
                "1979-05-09 00:00:00", "Thanh Hóa", null, null, null, null, null, null, null, null, null, null, null, null, null);
        when(teacherRepository.getById(any())).thenReturn(teacher);
        when(teacherRepository.findById(any())).thenReturn(Optional.of(teacher));
        ServiceResult<Boolean> actual = teacherService.update(teacherForm, 10);
        assertEquals("Thiếu thông tin tên giáo viên", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    @Test()
    void updateTest4() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "Tuấn ", 1, 34, authorities, null, 0, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        Teacher teacher = new Teacher(10, null, "admin", null, null, null, "tuanvtq", "MCB012022", 1, null, 1,
                "1979-05-09 00:00:00", "Thanh Hóa", null, null, null, null, null, null, null, null, null, null, null, null, null);
        when(teacherRepository.getById(any())).thenReturn(teacher);
        when(teacherRepository.findById(any())).thenReturn(Optional.of(teacher));
        ServiceResult<Boolean> actual = teacherService.update(teacherForm, 10);
        assertEquals("Thiếu thông tin ngày vào trường", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    @Test()
    void updateTest5() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "Tuấn ", 1, 34, authorities, "2021-08-18 20:03:55", null, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        Teacher teacher = new Teacher(10, null, "admin", null, null, null, "tuanvtq", "MCB012022", 1, null, 1,
                "1979-05-09 00:00:00", "Thanh Hóa", null, null, null, null, null, null, null, null, null, null, null, null, null);
        when(teacherRepository.getById(any())).thenReturn(teacher);
        when(teacherRepository.findById(any())).thenReturn(Optional.of(teacher));
        ServiceResult<Boolean> actual = teacherService.update(teacherForm, 10);
        assertEquals("Thiếu thông tin loại hợp đồng", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    @Test()
    void updateTest6() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "Tuấn ", 1, 34, authorities, "2021-08-18 20:03:55", 1, null, "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        Teacher teacher = new Teacher(10, null, "admin", null, null, null, "tuanvtq", "MCB012022", 1, null, 1,
                "1979-05-09 00:00:00", "Thanh Hóa", null, null, null, null, null, null, null, null, null, null, null, null, null);
        when(teacherRepository.getById(any())).thenReturn(teacher);
        when(teacherRepository.findById(any())).thenReturn(Optional.of(teacher));
        ServiceResult<Boolean> actual = teacherService.update(teacherForm, 10);
        assertEquals("Thiếu thông tin số điện thoại", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    @Test()
    void updateTest7() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "Tuấn ", 1, 34, authorities, "2021-08-18 20:03:55", 1, "03873993456", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        Teacher teacher = new Teacher(10, null, "admin", null, null, null, "tuanvtq", "MCB012022", 1, null, 1,
                "1979-05-09 00:00:00", "Thanh Hóa", null, null, null, null, null, null, null, null, null, null, null, null, null);
        when(teacherRepository.getById(any())).thenReturn(teacher);
        when(teacherRepository.findById(any())).thenReturn(Optional.of(teacher));
        ServiceResult<Boolean> actual = teacherService.update(teacherForm, 10);
        assertEquals("Số điện thoại sai định dạng", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    @Test()
    void updateTest8() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "Tuấn ", 1, 34, authorities, "2021-08-18 20:03:55", 1, "03873993456", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        Teacher teacher = new Teacher(10, null, "admin", null, null, null, "tuanvtq", "MCB012022", 1, null, 1,
                "1979-05-09 00:00:00", "Thanh Hóa", null, null, null, null, null, null, null, null, null, null, null, null, null);
        when(teacherRepository.getById(any())).thenReturn(teacher);
        when(teacherRepository.findById(any())).thenReturn(Optional.of(teacher));
        ServiceResult<Boolean> actual = teacherService.update(teacherForm, 10);
        assertEquals("Số điện thoại sai định dạng", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    @Test()
    void updateTest9() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "Tuấn ", 1, 34, authorities, "2021-08-18 20:03:55", 1, "038739934a", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        Teacher teacher = new Teacher(10, null, "admin", null, null, null, "tuanvtq", "MCB012022", 1, null, 1,
                "1979-05-09 00:00:00", "Thanh Hóa", null, null, null, null, null, null, null, null, null, null, null, null, null);
        when(teacherRepository.getById(any())).thenReturn(teacher);
        when(teacherRepository.findById(any())).thenReturn(Optional.of(teacher));
        ServiceResult<Boolean> actual = teacherService.update(teacherForm, 10);
        assertEquals("Số điện thoại sai định dạng", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    @Test()
    void updateTest10() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "Tuấn ", 1, 34, authorities, "2021-08-18 20:03:55", 1, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "01720000479a", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        Teacher teacher = new Teacher(10, null, "admin", null, null, null, "tuanvtq", "MCB012022", 1, null, 1,
                "1979-05-09 00:00:00", "Thanh Hóa", null, null, null, null, null, null, null, null, null, null, null, null, null);
        when(teacherRepository.getById(any())).thenReturn(teacher);
        when(teacherRepository.findById(any())).thenReturn(Optional.of(teacher));
        ServiceResult<Boolean> actual = teacherService.update(teacherForm, 10);
        assertEquals("Số CMND/CCCD sai định dạng", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    @Test()
    void updateTest16() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "Tuấn ", 1, 34, authorities, "2021-08-18 20:03:55", 1, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        Teacher teacher = new Teacher(10, null, "admin", null, null, null, "tuanvtq", "MCB012022", 1, null, 1,
                "1979-05-09 00:00:00", "Thanh Hóa", null, null, null, null, null, null, null, null, null, null, null, null, null);
        when(teacherRepository.getById(any())).thenReturn(teacher);
        when(teacherRepository.findById(any())).thenReturn(Optional.of(teacher));
        ServiceResult<Boolean> actual = teacherService.update(teacherForm, 10);
        assertEquals("Số CMND/CCCD sai định dạng", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    void updateTest11() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "Tuấn ", 1, 34, authorities, "2021-08-18 20:03:55", 1, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "01720000479a", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 0
        );
        Teacher teacher = new Teacher(10, null, "admin", null, null, null, "tuanvtq", "MCB012022", 1, null, 1,
                "1979-05-09 00:00:00", "Thanh Hóa", null, null, null, null, null, null, null, null, null, null, null, null, null);
        when(teacherRepository.getById(any())).thenReturn(teacher);
        when(teacherRepository.findById(any())).thenReturn(Optional.of(teacher));
        ServiceResult<Boolean> actual = teacherService.update(teacherForm, 10);
        assertEquals("Số CMND/CCCD sai định dạng", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    @Test()
    void updateTest12() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "Tuấn ", 1, 34, authorities, "2021-08-18 20:03:55", 1, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", null, 0, 0
        );
        Teacher teacher = new Teacher(10, null, "admin", null, null, null, "tuanvtq", "MCB012022", 1, null, 1,
                "1979-05-09 00:00:00", "Thanh Hóa", null, null, null, null, null, null, null, null, null, null, null, null, null);
        when(teacherRepository.getById(any())).thenReturn(teacher);
        when(teacherRepository.findById(any())).thenReturn(Optional.of(teacher));
        ServiceResult<Boolean> actual = teacherService.update(teacherForm, 10);
        assertEquals("Thiếu thông tin ngày cấp CMND/CCCD", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    @Test()
    void updateTest13() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "Tuấn ", 1, 34, authorities, "2021-08-18 20:03:55", 1, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, null
        );
        Teacher teacher = new Teacher(10, null, "admin", null, null, null, "tuanvtq", "MCB012022", 1, null, 1,
                "1979-05-09 00:00:00", "Thanh Hóa", null, null, null, null, null, null, null, null, null, null, null, null, null);
        when(teacherRepository.getById(any())).thenReturn(teacher);
        when(teacherRepository.findById(any())).thenReturn(Optional.of(teacher));
        ServiceResult<Boolean> actual = teacherService.update(teacherForm, 10);
        assertEquals("Thiếu thông tin giới tính", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    @Test()
    void updateTest14() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "Tuấn ", 1, 34, authorities, "2021-08-18 20:03:55", 1, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 1
        );
        Teacher teacher = new Teacher(10, null, "admin", null, null, null, "tuanvtq", "MCB012022", 1, null, 1,
                "1979-05-09 00:00:00", "Thanh Hóa", null, null, null, null, null, null, null, null, null, null, null, null, null);
        when(teacherRepository.getById(any())).thenReturn(teacher);
        when(teacherRepository.findById(any())).thenReturn(Optional.of(teacher));
        when(userService.updateUserForTeacher(any(), any(), any(), any(), any(), any(), any())).thenReturn(true);
        ServiceResult<Boolean> actual = teacherService.update(teacherForm, 10);
        assertEquals("Cập nhật thông tin thành công", actual.getMessage());
        assertEquals(HttpStatus.OK, actual.getStatus());
    }

    @Test()
    void updateTest15() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        TeacherForm teacherForm = new TeacherForm(
                null, "MCB123456", "Tuấn ", 1, 34, authorities, "2021-08-18 20:03:55", 1, "0387399342", "tuan@gmail.com", "2000-08-18", "Phat", "Kinh", "Huu Nghi, Hoa Binh",
                "Thach Hoa, Thach That, Ha Noi", "12, Huu Nghi, Hoa Binh", "1234567890", "017200004794", "phong quan ly xã hoi", "2021-08-18 20:03:55", 0, 1
        );
        Teacher teacher = new Teacher(10, null, "admin", null, null, null, "tuanvtq", "MCB012022", 1, null, 1,
                "1979-05-09 00:00:00", "Thanh Hóa", null, null, null, null, null, null, null, null, null, null, null, null, null);
        when(teacherRepository.getById(any())).thenReturn(teacher);
        when(teacherRepository.findById(any())).thenReturn(Optional.of(teacher));
        ServiceResult<Boolean> actual = teacherService.update(teacherForm, 10);
        assertEquals("Cập nhật tài khoản cán bộ không thành công", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }
}
