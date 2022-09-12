package fpt.capstone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mysql.cj.jdbc.exceptions.MySQLTimeoutException;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.SchoolForm;
import fpt.capstone.repository.SchoolRepository;
import fpt.capstone.service.SchoolServiceImp;

@ExtendWith(MockitoExtension.class)
public class SchoolServiceimplTest {
    @InjectMocks
    SchoolServiceImp service;

    @Mock
    SchoolRepository repository;

    @Test()
    void getAllDistricts()
    {
        List<Map<String, Object>> listResult = new ArrayList<>();
        String[] sampleDistricts = {"Huyện Mai Sơn", "Thành phố Sơn La", "Huyện Mộc Châu", "Huyện Yêu Châu", "Huyện Sông Mã",
            "Huyện Bắc Yên", "Huyện Mường La", "Huyện Phú Yên", "Huyện Quỳnh Nhai", "Huyện Sông Mã", "Huyện Sốp Cộp",
            "Huyện Thuật Châu", "Huyện Vân Hồ"
        };

        for(int i = 0; i < sampleDistricts.length; ++i)
        {
            Map<String, Object> district = new HashMap<>();
            district.put("id", i + 1);
            district.put("name", sampleDistricts[i]);
            district.put("province_id", 26);
        }

        when(repository.getAllDistrict(26)).thenReturn(listResult);

        ServiceResult<List<Map<String, Object>>> result = service.getAllDistrict(26);
        assertEquals(result.getStatus().name(), "OK");
        assertEquals(result.getMessage(), "success");
        assertEquals(result.getData(), listResult);
    }

    @Test()
    void getAllDistrictsWhenNull()
    {
        List<Map<String, Object>> listResult = new ArrayList<>();

        when(repository.getAllDistrict(null)).thenReturn(listResult);

        ServiceResult<List<Map<String, Object>>> result = service.getAllDistrict(null);
        assertEquals(result.getStatus().name(), "OK");
        assertEquals(result.getMessage(), "success");
        assertEquals(result.getData(), listResult);
    }

    @Test()
    void getAllDistrictsExceptionOccurs()
    {
        when(repository.getAllDistrict(26)).thenAnswer(invocation -> { throw new Exception();});
        ServiceResult<List<Map<String, Object>>> result = service.getAllDistrict(26);
        assertEquals(result.getStatus().name(), "BAD_REQUEST");
        assertEquals(result.getMessage(), "Lấy quận huyện lỗi");
        assertEquals(result.getData(), null);
        
    }

    @Test()
    void getAllDistrictsExceptionOccursWithNull()
    {
        when(repository.getAllDistrict(null)).thenAnswer(invocation -> { throw new Exception();});
        ServiceResult<List<Map<String, Object>>> result = service.getAllDistrict(null);
        assertEquals(result.getStatus().name(), "BAD_REQUEST");
        assertEquals(result.getMessage(), "Lấy quận huyện lỗi");
        assertEquals(result.getData(), null);
        
    }

    @Test()
    void getAllProvinces()
    {
        List<Map<String, Object>> listResult = new ArrayList<>();
        String[] sampleProvinces = {
            "Thành phố Hà Nội",
            "Thành phố Hồ Chí Minh",
            "Tỉnh Lào Cai",
            "Tỉnh Sơn La"
        };
        for(int i = 0; i < sampleProvinces.length; ++i)
        {
            Map<String, Object> district = new HashMap<>();
            district.put("id", i + 1);
            district.put("name", sampleProvinces[i]);
            district.put("path", null);
        }
        when(repository.getAllProvince()).thenReturn(listResult);

        ServiceResult<List<Map<String, Object>>> result = service.getAllProvince();
        assertEquals(result.getStatus().name(), "OK");
        assertEquals(result.getMessage(), "success");
        assertEquals(result.getData(), listResult);
    }

    @Test()
    void getAllProvincesReturnNull()
    {
        
        when(repository.getAllProvince()).thenReturn(null);

        ServiceResult<List<Map<String, Object>>> result = service.getAllProvince();
        assertEquals(result.getStatus().name(), "OK");
        assertEquals(result.getMessage(), "success");
        assertEquals(result.getData(), null);
    }

    @Test()
    void getAllProvincesException()
    {
        
        when(repository.getAllProvince()).thenAnswer(invocation -> { throw new Exception();});

        ServiceResult<List<Map<String, Object>>> result = service.getAllProvince();
        assertEquals(result.getStatus().name(), "BAD_REQUEST");
        assertEquals(result.getMessage(), "Lấy tỉnh/thành phố lỗi");
        assertEquals(result.getData(), null);
    }

    @Test()
    void updateNullSchool()
    {
        ServiceResult<Boolean> result = service.save(null);
        assertEquals(result.getStatus().name(), "BAD_REQUEST");
        assertEquals(result.getMessage(), "Cập nhật thông tin thất bại");
        assertEquals(result.getData(), false);
    }

    @Test()
    void updateNullNameSchool()
    {
        SchoolForm form = new SchoolForm();
        form.setId(1);
        form.setName(null);
        form.setCode("THPT_LTV_ND");
        form.setProvince_id(1);

        ServiceResult<Boolean> result = service.save(null);
        assertEquals(result.getStatus().name(), "BAD_REQUEST");
        assertEquals(result.getMessage(), "Cập nhật thông tin thất bại");
        assertEquals(result.getData(), false);
    }

    @Test()
    void updateNullCodeSchool()
    {
        SchoolForm form = new SchoolForm();
        form.setId(1);
        form.setName("Trường THPT Lương Thế Vinh");
        form.setCode(null);
        form.setProvince_id(1);

        ServiceResult<Boolean> result = service.save(null);
        assertEquals(result.getStatus().name(), "BAD_REQUEST");
        assertEquals(result.getMessage(), "Cập nhật thông tin thất bại");
        assertEquals(result.getData(), false);
    }

    @Test()
    void updateNullProvinceSchool()
    {
        SchoolForm form = new SchoolForm();
        form.setId(1);
        form.setName("Trường THPT Lương Thế Vinh");
        form.setCode("THPT_LTV_ND");
        form.setProvince_id(null);

        ServiceResult<Boolean> result = service.save(null);
        assertEquals(result.getStatus().name(), "BAD_REQUEST");
        assertEquals(result.getMessage(), "Cập nhật thông tin thất bại");
        assertEquals(result.getData(), false);
    }

    @Test()
    void updateNullNameAndProvinceSchool()
    {
        SchoolForm form = new SchoolForm();
        form.setId(1);
        form.setName(null);
        form.setCode("THPT_LTV_ND");
        form.setProvince_id(null);

        ServiceResult<Boolean> result = service.save(null);
        assertEquals(result.getStatus().name(), "BAD_REQUEST");
        assertEquals(result.getMessage(), "Cập nhật thông tin thất bại");
        assertEquals(result.getData(), false);
    }

    @Test()
    void updateNullNameAndCodeSchool()
    {
        SchoolForm form = new SchoolForm();
        form.setId(1);
        form.setName(null);
        form.setCode(null);
        form.setProvince_id(1);

        ServiceResult<Boolean> result = service.save(null);
        assertEquals(result.getStatus().name(), "BAD_REQUEST");
        assertEquals(result.getMessage(), "Cập nhật thông tin thất bại");
        assertEquals(result.getData(), false);
    }

    @Test()
    void getSchoolInfo() {
        List<Map<String, Object>> listResult = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();

        result.put("id", 1);
        result.put("update_time", null);
        result.put("update_name", null);
        result.put("name", "Trường THPT Lương Thế Vinh");
        result.put("code", "THPT_LTV_ND");
        result.put("district_id", 7);
        result.put("province_id", 1);
        result.put("abbreviation_name", "THPTLTV");
        result.put("address", "Thị Trấn Gôi, Huyện Vụ Bản, Tỉnh Nam Định");
        result.put("level_school", "HIGH");
        result.put("type_education", "PRIVATE");
        result.put("founded_date", LocalDate.parse("1973-08-01"));
        result.put("principal", "Trần Văn Hùng");
        result.put("hot_line", "0948621328");
        result.put("phone_principal", "0948621328");
        result.put("email", "thptltvnd@gmail.com");
        result.put("website", "thptltv.edu.vn");
        result.put("provinceName", "Thành phố Hà Nội");
        result.put("districtName", "Quận Hai Bà Trưng");

        listResult.add(result);

        when(repository.getSchoolInfo()).thenReturn(listResult);

        ServiceResult<List<Map<String, Object>>> expected = service.getSchoolInfo();
        assertEquals(expected.getStatus().name(), "OK");
        assertEquals(expected.getMessage(), "success");
        assertEquals(expected.getData(), listResult);
    }

    @Test()
    void getSchoolInfoReturnNull() {
        when(repository.getSchoolInfo()).thenReturn(null);

        ServiceResult<List<Map<String, Object>>> expected = service.getSchoolInfo();
        assertEquals(expected.getStatus().name(), "OK");
        assertEquals(expected.getMessage(), "success");
        assertEquals(expected.getData(), null);
    }

    @Test()
    void getSchoolInfoException() {
        when(repository.getSchoolInfo()).thenAnswer(invocation -> { throw new Exception();});

        ServiceResult<List<Map<String, Object>>> expected = service.getSchoolInfo();
        assertEquals(expected.getStatus().name(), "BAD_REQUEST");
        assertEquals(expected.getMessage(), "Lấy thông tin trường học lỗi");
        assertEquals(expected.getData(), null);
    }
}
