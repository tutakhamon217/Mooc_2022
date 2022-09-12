package fpt.capstone.service;

import fpt.capstone.entities.ConfigGradeDetail;
import fpt.capstone.entities.ConfigScoreDetail;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.ConfigGradeDetailForm;
import fpt.capstone.repository.ConfigGradeDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ConfigGradeDetailServiceImpl implements ConfigGradeDetailService {

    @Autowired
    private ConfigGradeDetailRepository configGradeDetailRepository;

    @Override
    public ServiceResult<List<ConfigGradeDetailForm>> saveAll(List<ConfigGradeDetailForm> list) {

        try{
            if(list.size() > 0){
                for (int i = 0; i < list.size(); i++){
                    if(list.get(i).getName() == null || list.get(i).getName().trim().equals("") || list.get(i).getType_choose() == null){
                        return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Hãy nhập đầy đủ thông tin", null);
                    }
                    if(list.get(i).getType_choose() < 0 || list.get(i).getType_choose() > 1){
                        return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Kiểu chọn phải có giá trị 0 hoặc 1", null);
                    }
                    if(list.get(i).getType_choose() == 0 && list.get(i).getSelected_value() != null && list.get(i).getSelected_value().length() > 0){
                        return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Chưa chọn kiểu chọn nhưng có giá trị lựa chọn", null);
                    }
                    if(list.get(i).getType_choose() == 1 && (list.get(i).getSelected_value() == null || list.get(i).getSelected_value().trim().equals(""))){
                        return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Đã chọn kiểu chọn nhưng chưa có giá trị lựa chọn", null);
                    }
                    if(list.get(i).getParent_code() == null || list.get(i).getParent_code().trim().equals("")){
                        return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Danh sách cập nhật chứa thành phần chưa thuộc đầu điểm cho môn học nào", null);
                    }
                }

                List<ConfigGradeDetail> configGradeDetails = configGradeDetailRepository.getAllConfGradeDetailByParentCode(list.get(0).getParent_code());
                for (int i = 0; i < configGradeDetails.size(); i++){
                    if(configGradeDetailRepository.isScored(configGradeDetails.get(i).getCode()).equals(1)){
                        return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Đầu điểm này đã được nhập điểm", null);
                    }
                }

                List<ConfigGradeDetail> listDetails = new ArrayList<>();

                Calendar calendar = Calendar.getInstance();
                String timeCurrent = calendar.get(Calendar.YEAR)
                        + (calendar.get(Calendar.MONTH) + 1)
                        + calendar.get(Calendar.DATE)
                        + "_"
                        + calendar.get(Calendar.HOUR)
                        + calendar.get(Calendar.MINUTE)
                        + calendar.get(Calendar.MILLISECOND);
                for(ConfigGradeDetailForm cs : list){
                    ConfigGradeDetail csd = new ConfigGradeDetail(
                            cs.getName(),
                            cs.getType_choose(),
                            cs.getSelected_value(),
                            "conf_grading_details" + timeCurrent + "_" + UUID.randomUUID(),
                            cs.getParent_code()
                    );
                    listDetails.add(csd);
                }
                listDetails = configGradeDetailRepository.saveAll(listDetails);
                ServiceResult sr = new ServiceResult();
                sr.setMessage("success");
                sr.setStatus(HttpStatus.OK);
                sr.setData(listDetails);
                return sr;
            }else{
                return new ServiceResult<>(HttpStatus.OK, "empty", null);
            }
        }catch (Exception e){
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thêm mới thất bại", null);
        }
    }

    @Override
    public ServiceResult<Boolean> deleteAll(Integer[] list) {
        try {
                for (int i = 0; i < list.length; i++){
                    ConfigGradeDetail csd = configGradeDetailRepository.findById(list[i]).get();
                    if(configGradeDetailRepository.isScored(csd.getCode()).equals(1)){
                        return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Đầu điểm này đã được nhập điểm", null);
                    }
                }
            if(list.length > 0){
                List<ConfigGradeDetail> listCSD = new ArrayList<>();
                for (int i = 0; i < list.length; i++) {
                    ConfigGradeDetail csd = configGradeDetailRepository.findById(list[i]).get();
                    listCSD.add(csd);
                }
                configGradeDetailRepository.deleteAll(listCSD);
                return new ServiceResult<>(HttpStatus.OK, "Xóa thành công", true);
            }else{
                return new ServiceResult<>(HttpStatus.OK, "empty", true);
            }
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Xoá thất bại", false);
        }
    }

    @Override
    public ServiceResult<Boolean> updateChange(List<ConfigGradeDetailForm> list) {
        try {
            for (int i = 0; i < list.size(); i++){
                if(list.get(i).getName() == null || list.get(i).getName().trim().equals("") || list.get(i).getType_choose() == null){
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Hãy nhập đầy đủ thông tin", null);
                }
                if(list.get(i).getId() == null){
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Đầu điểm '"+ list.get(i).getName() +"' chưa từng tồn tại", null);
                }
                if(list.get(i).getType_choose() < 0 || list.get(i).getType_choose() > 1){
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Kiểu chọn phải có giá trị 0 hoặc 1", null);
                }
                if(list.get(i).getType_choose() == 0 && list.get(i).getSelected_value() != null && list.get(i).getSelected_value().length() > 0){
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Chưa chọn kiểu chọn nhưng có giá trị lựa chọn", null);
                }
                if(list.get(i).getType_choose() == 1 && (list.get(i).getSelected_value() == null || list.get(i).getSelected_value().trim().equals(""))){
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Đã chọn kiểu chọn nhưng chưa có giá trị lựa chọn", null);
                }
                if(list.get(i).getCode() == null || list.get(i).getCode().trim().equals("")){
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Đầu điểm '"+ list.get(i).getName() +"' chưa từng tồn tại", null);
                }
                if(list.get(i).getParent_code() == null || list.get(i).getParent_code().trim().equals("")){
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Danh sách cập nhật chứa thành phần chưa thuộc đầu điểm cho môn học nào", null);
                }
            }

            for (int i = 0; i < list.size(); i++){
                if(configGradeDetailRepository.isScored(list.get(i).getCode()).equals(1)){
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Đầu điểm này đã được nhập điểm", null);
                }
            }
            if(list.size() > 0){
                List<ConfigGradeDetail> listCSD = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    ConfigGradeDetail csd = configGradeDetailRepository.findById(list.get(i).getId()).get();
                    csd.setName(list.get(i).getName());

                    csd.setTypeChoose(list.get(i).getType_choose());
                    csd.setSelectedValue(list.get(i).getSelected_value());
                    csd.setCode(list.get(i).getCode());
                    csd.setParentCode(list.get(i).getParent_code());

                    listCSD.add(csd);
                }
                configGradeDetailRepository.saveAll(listCSD);
                return new ServiceResult<>(HttpStatus.OK, "Cập nhật thành công", true);
            }else{
                return new ServiceResult<>(HttpStatus.OK, "empty", true);
            }
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Cập nhật thất bại", false);
        }
    }
}
