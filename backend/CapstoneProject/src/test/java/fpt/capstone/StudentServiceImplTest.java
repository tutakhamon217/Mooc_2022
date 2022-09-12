package fpt.capstone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.entities.Student;
import fpt.capstone.form_data.StudentForm;
import fpt.capstone.repository.StudentRepository;
import fpt.capstone.service.StudentServiceImpl;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {
    @InjectMocks
    StudentServiceImpl service;

    @Mock
    StudentRepository repository;

    @Test()
    void getStudentOfClassScreenDanhGiaHocLuc()
    {
        List<Student> expected = new ArrayList<>();
        expected.add(new Student());
        expected.add(new Student());
        
        when(repository.getStudentOfClassManDanhGiaHocLuc("2022-2023", "L6B")).thenReturn(expected);
        ServiceResult<List<Student>> actual =  service.getStudentOfClassScreenDanhGiaHocLuc("2022-2023", "L6B");
        assertEquals("OK", actual.getStatus().name());
        assertEquals("success", actual.getMessage());

        Map<String, Object> data = (Map<String, Object>)(actual.getData());
        assertEquals(expected, data.get("data"));
    }

    @Test()
    void getStudentOfClassScreenDanhGiaHocLucReturnNull()
    { 
        when(repository.getStudentOfClassManDanhGiaHocLuc("2022-2023", "L6B")).thenReturn(null);
        ServiceResult<List<Student>> actual =  service.getStudentOfClassScreenDanhGiaHocLuc("2022-2023", "L6B");
        assertEquals("OK", actual.getStatus().name());
        assertEquals("success", actual.getMessage());

        Map<String, Object> data = (Map<String, Object>)(actual.getData());
        assertEquals(null, data.get("data"));
    }

    @Test()
    void getStudentOfClassScreenDanhGiaHocLucGetException()
    { 
        when(repository.getStudentOfClassManDanhGiaHocLuc("2022-2023", "L6B")).thenAnswer(ans -> {throw new Exception();});
        ServiceResult<List<Student>> actual =  service.getStudentOfClassScreenDanhGiaHocLuc("2022-2023", "L6B");
        assertEquals("BAD_REQUEST", actual.getStatus().name());
        assertEquals("failed", actual.getMessage());
        assertEquals(null, actual.getData());
    }

    @Test()
    void insertStudentWithEmptyCode()
    {
        StudentForm form = new StudentForm();
        form.setCode("");

        ServiceResult<Boolean> actual = service.insert(form);
        assertEquals("BAD_REQUEST", actual.getStatus().name());
        assertEquals("Thiếu thông tin mã học sinh", actual.getMessage());
        assertEquals(true, actual.getData());
    }

    @Test()
    void insertStudentWithExistedCode()
    {
        StudentForm form = new StudentForm();
        form.setCode("hailt");

        List<Student> students = new ArrayList<>();
        students.add(new Student());
        when(repository.findByCode("hailt")).thenReturn(students);
        ServiceResult<Boolean> actual = service.insert(form);
        assertEquals("BAD_REQUEST", actual.getStatus().name());
        assertEquals("Mã học sinh đã tồn tại", actual.getMessage());
        assertEquals(true, actual.getData());
    }

    @Test()
    void insertStudentWithEmptyFullName()
    {
        StudentForm form = new StudentForm();
        form.setCode("hailt");
        form.setFullName("");

        when(repository.findByCode("hailt")).thenReturn(new ArrayList<>());
        ServiceResult<Boolean> actual = service.insert(form);
        assertEquals("BAD_REQUEST", actual.getStatus().name());
        assertEquals("Thiếu thông tin tên học sinh", actual.getMessage());
        assertEquals(true, actual.getData());
    }

    @Test()
    void insertStudentWithNullStatus()
    {
        StudentForm form = new StudentForm();
        form.setCode("hailt");
        form.setFullName("Loeng Hoi");
        form.setStatus(null);

        when(repository.findByCode("hailt")).thenReturn(new ArrayList<>());
        ServiceResult<Boolean> actual = service.insert(form);
        assertEquals("BAD_REQUEST", actual.getStatus().name());
        assertEquals("Thiếu thông tin trạng thái", actual.getMessage());
        assertEquals(true, actual.getData());
    }
    

    @Test()
    void insertStudentWithNullTrainningSystem()
    {
        StudentForm form = new StudentForm();
        form.setCode("hailt");
        form.setFullName("Loeng Hoi");
        form.setStatus(2);
        form.setTrainingSystem(null);

        when(repository.findByCode("hailt")).thenReturn(new ArrayList<>());
        ServiceResult<Boolean> actual = service.insert(form);
        assertEquals("BAD_REQUEST", actual.getStatus().name());
        assertEquals("Thiếu thông tin hệ đào tạo", actual.getMessage());
        assertEquals(true, actual.getData());
    }

    @Test()
    void insertStudentWithNullQuanHe()
    {
        StudentForm form = new StudentForm();
        form.setCode("hailt");
        form.setFullName("Loeng Hoi");
        form.setStatus(2);
        form.setTrainingSystem(1);
        form.setQuanHe(null);

        when(repository.findByCode("hailt")).thenReturn(new ArrayList<>());
        ServiceResult<Boolean> actual = service.insert(form);
        assertEquals("BAD_REQUEST", actual.getStatus().name());
        assertEquals("Thiếu thông tin phụ huynh", actual.getMessage());
        assertEquals(true, actual.getData());
    }

    @Test()
    void insertStudentWithEmptyParentName()
    {
        StudentForm form = new StudentForm();
        form.setCode("hailt");
        form.setFullName("Loeng Hoi");
        form.setStatus(2);
        form.setTrainingSystem(1);
        form.setQuanHe(1);
        form.setHoTenPhuHuynh("");

        when(repository.findByCode("hailt")).thenReturn(new ArrayList<>());
        ServiceResult<Boolean> actual = service.insert(form);
        assertEquals("BAD_REQUEST", actual.getStatus().name());
        assertEquals("Thiếu thông tin họ tên phụ huynh", actual.getMessage());
        assertEquals(true, actual.getData());
    }

    @Test()
    void insertStudentWithEmptyParentPhoneNumber()
    {
        StudentForm form = new StudentForm();
        form.setCode("hailt");
        form.setFullName("Loeng Hoi");
        form.setStatus(2);
        form.setTrainingSystem(1);
        form.setQuanHe(1);
        form.setHoTenPhuHuynh("Loeng Sei Hoi");
        form.setSoDienThoaiPhuHuynh("");

        when(repository.findByCode("hailt")).thenReturn(new ArrayList<>());
        ServiceResult<Boolean> actual = service.insert(form);
        assertEquals("BAD_REQUEST", actual.getStatus().name());
        assertEquals("Thiếu thông tin số điện thoại phụ huynh", actual.getMessage());
        assertEquals(true, actual.getData());
    }

    @Test()
    void insertStudentWithEmptyClassCode()
    {
        StudentForm form = new StudentForm();
        form.setCode("hailt");
        form.setFullName("Loeng Hoi");
        form.setStatus(2);
        form.setTrainingSystem(1);
        form.setQuanHe(1);
        form.setHoTenPhuHuynh("Loeng Sei Hoi");
        form.setSoDienThoaiPhuHuynh("0123456789");
        form.setClassCode("");

        when(repository.findByCode("hailt")).thenReturn(new ArrayList<>());
        ServiceResult<Boolean> actual = service.insert(form);
        assertEquals("BAD_REQUEST", actual.getStatus().name());
        assertEquals("Thiếu thông tin lớp học", actual.getMessage());
        assertEquals(true, actual.getData());
    }

    @Test()
    void updateStudentWithEmptyCode()
    {
        StudentForm form = new StudentForm();
        form.setCode("");

        ServiceResult<Boolean> actual = service.update(form);
        assertEquals("BAD_REQUEST", actual.getStatus().name());
        assertEquals("Thiếu thông tin mã học sinh", actual.getMessage());
        assertEquals(true, actual.getData());
    }

    

    @Test()
    void updateStudentWithEmptyFullName()
    {
        StudentForm form = new StudentForm();
        form.setCode("hailt");
        form.setFullName("");

        ServiceResult<Boolean> actual = service.update(form);
        assertEquals("BAD_REQUEST", actual.getStatus().name());
        assertEquals("Thiếu thông tin tên học sinh", actual.getMessage());
        assertEquals(true, actual.getData());
    }

    @Test()
    void updateStudentWithNullStatus()
    {
        StudentForm form = new StudentForm();
        form.setCode("hailt");
        form.setFullName("Loeng Hoi");
        form.setStatus(null);

        ServiceResult<Boolean> actual = service.update(form);
        assertEquals("BAD_REQUEST", actual.getStatus().name());
        assertEquals("Thiếu thông tin trạng thái", actual.getMessage());
        assertEquals(true, actual.getData());
    }
    

    @Test()
    void updateStudentWithNullTrainningSystem()
    {
        StudentForm form = new StudentForm();
        form.setCode("hailt");
        form.setFullName("Loeng Hoi");
        form.setStatus(2);
        form.setTrainingSystem(null);

        ServiceResult<Boolean> actual = service.update(form);
        assertEquals("BAD_REQUEST", actual.getStatus().name());
        assertEquals("Thiếu thông tin hệ đào tạo", actual.getMessage());
        assertEquals(true, actual.getData());
    }

    @Test()
    void updateStudentWithNullQuanHe()
    {
        StudentForm form = new StudentForm();
        form.setCode("hailt");
        form.setFullName("Loeng Hoi");
        form.setStatus(2);
        form.setTrainingSystem(1);
        form.setQuanHe(null);

        ServiceResult<Boolean> actual = service.update(form);
        assertEquals("BAD_REQUEST", actual.getStatus().name());
        assertEquals("Thiếu thông tin phụ huynh", actual.getMessage());
        assertEquals(true, actual.getData());
    }

    @Test()
    void updateStudentWithEmptyParentName()
    {
        StudentForm form = new StudentForm();
        form.setCode("hailt");
        form.setFullName("Loeng Hoi");
        form.setStatus(2);
        form.setTrainingSystem(1);
        form.setQuanHe(1);
        form.setHoTenPhuHuynh("");

        ServiceResult<Boolean> actual = service.update(form);
        assertEquals("BAD_REQUEST", actual.getStatus().name());
        assertEquals("Thiếu thông tin họ tên phụ huynh", actual.getMessage());
        assertEquals(true, actual.getData());
    }

    @Test()
    void updateStudentWithEmptyParentPhoneNumber()
    {
        StudentForm form = new StudentForm();
        form.setCode("hailt");
        form.setFullName("Loeng Hoi");
        form.setStatus(2);
        form.setTrainingSystem(1);
        form.setQuanHe(1);
        form.setHoTenPhuHuynh("Loeng Sei Hoi");
        form.setSoDienThoaiPhuHuynh("");

        ServiceResult<Boolean> actual = service.update(form);
        assertEquals("BAD_REQUEST", actual.getStatus().name());
        assertEquals("Thiếu thông tin số điện thoại phụ huynh", actual.getMessage());
        assertEquals(true, actual.getData());
    }

    @Test()
    void updateStudentWithEmptyClassCode()
    {
        StudentForm form = new StudentForm();
        form.setCode("hailt");
        form.setFullName("Loeng Hoi");
        form.setStatus(2);
        form.setTrainingSystem(1);
        form.setQuanHe(1);
        form.setHoTenPhuHuynh("Loeng Sei Hoi");
        form.setSoDienThoaiPhuHuynh("0123456789");
        form.setClassCode("");

        ServiceResult<Boolean> actual = service.update(form);
        assertEquals("BAD_REQUEST", actual.getStatus().name());
        assertEquals("Thiếu thông tin lớp học", actual.getMessage());
        assertEquals(true, actual.getData());
    }

}
