package fpt.capstone.service;

import fpt.capstone.entities.School;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.SchoolForm;
import fpt.capstone.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SchoolServiceImp implements SchoolService{

    @Autowired
    private SchoolRepository schoolRepository;

    @Override
    public ServiceResult<List<Map<String, Object>>> getAllDistrict(Integer province) {
        try{
            return new ServiceResult<>(HttpStatus.OK, "success", schoolRepository.getAllDistrict(province));
        }catch (Exception e){
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Lấy quận huyện lỗi", null);
        }
    }

    @Override
    public ServiceResult<List<Map<String, Object>>> getAllProvince() {
        try{
            return new ServiceResult<>(HttpStatus.OK, "success", schoolRepository.getAllProvince());
        }catch (Exception e){
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Lấy tỉnh/thành phố lỗi", null);
        }
    }

    @Override
    public ServiceResult<List<Map<String, Object>>> getSchoolInfo() {
        try{
            return new ServiceResult<>(HttpStatus.OK, "success", schoolRepository.getSchoolInfo());
        }catch (Exception e){
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Lấy thông tin trường học lỗi", null);
        }
    }

    @Override
    public ServiceResult<Boolean> save(SchoolForm schoolForm) {
        try{
            School school = new School();
            school.setId(schoolForm.getId());
            school.setName(schoolForm.getName());
            school.setCode(schoolForm.getCode());
            school.setDistrictId(schoolForm.getDistrict_id());
            school.setProvinceId(schoolForm.getProvince_id());
            school.setAbbreviationName(schoolForm.getAbbreviation_name());
            school.setAddress(schoolForm.getAddress());
            school.setLevelSchool(schoolForm.getLevel_school());
            school.setTypeEducation(schoolForm.getType_education());
            school.setFoundedDate(schoolForm.getFounded_date());
            school.setPrincipal(schoolForm.getPrincipal());
            school.setHotLine(schoolForm.getHot_line());
            school.setPhonePrincipal(schoolForm.getPhone_principal());
            school.setEmail(schoolForm.getEmail());
            school.setWebsite(schoolForm.getWebsite());
            school.setLogo(schoolForm.getLogo());
            schoolRepository.save(school);
            return new ServiceResult<>(HttpStatus.OK, "Cập nhật thông tin thành công", true);
        }catch (Exception e){
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Cập nhật thông tin thất bại", false);
        }
    }
}
