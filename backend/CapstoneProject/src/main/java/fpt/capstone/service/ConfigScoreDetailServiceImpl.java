package fpt.capstone.service;

import fpt.capstone.entities.ConfigScoreDetail;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.ConfigScoreDetailForm;
import fpt.capstone.repository.ConfigScoreDetailRepository;
import fpt.capstone.vo.ScoreDetailVO;
import fpt.capstone.vo.ScoreStudentVO;
import fpt.capstone.vo.ScoreVO;
import fpt.capstone.vo.StudentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class ConfigScoreDetailServiceImpl implements ConfigScoreDetailService {
    @Autowired
    private ConfigScoreDetailRepository configScoreDetailRepository;

    @Override
    public ServiceResult<List<ConfigScoreDetailForm>> saveAll(List<ConfigScoreDetailForm> list) {

        try{
            if(list.size() > 0){
                for (int i = 0; i < list.size(); i++){
                    if(list.get(i).getName() == null || list.get(i).getName().trim().equals("") || list.get(i).getCoefficient() == null || list.get(i).getMinimum_score() == null || list.get(i).getQuantity() == null){
                        return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Hãy nhập đầy đủ thông tin", null);
                    }
                    if(list.get(i).getCoefficient() < 1 || list.get(i).getCoefficient() > 10){
                        return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Hệ số phải lớn hơn 0 và nhỏ hơn hoặc bằng 10", null);
                    }
                    if(list.get(i).getQuantity() < 1 || list.get(i).getQuantity() > 10){
                        return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Số lượng bài kiểm tra phải lớn hơn 0 và nhỏ hơn 10", null);
                    }
                    if(list.get(i).getMinimum_score() < 1 || list.get(i).getMinimum_score() > 10){
                        return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Số lượng bài kiểm tra tối thiểu phải lớn hơn 0 và nhỏ hơn 10", null);
                    }
                    if(list.get(i).getMinimum_score() > list.get(i).getQuantity()){
                        return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Số lượng bài kiểm tra tối thiểu phải nhỏ hơn tổng số lượng bài kiểm tra", null);
                    }
                    if(list.get(i).getParent_code() == null || list.get(i).getParent_code().trim().equals("")){
                        return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Danh sách cập nhật chứa thành phần chưa thuộc đầu điểm cho môn học nào", null);
                    }
                }

                List<ConfigScoreDetail> configScoreDetails = configScoreDetailRepository.getAllSubjectScoreByParentCode(list.get(0).getParent_code());
                for (int i = 0; i < configScoreDetails.size(); i++){
                    if(configScoreDetailRepository.isScored(configScoreDetails.get(i).getCode()).equals(1)){
                        return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Đầu điểm này đã được nhập điểm", null);
                    }
                }
                List<ConfigScoreDetail> listDetails = new ArrayList<>();

                Calendar calendar = Calendar.getInstance();
                String timeCurrent = calendar.get(Calendar.YEAR)
                        + (calendar.get(Calendar.MONTH) + 1)
                        + calendar.get(Calendar.DATE)
                        + "_"
                        + calendar.get(Calendar.HOUR)
                        + calendar.get(Calendar.MINUTE)
                        + calendar.get(Calendar.MILLISECOND);
                for(ConfigScoreDetailForm cs : list){

                    ConfigScoreDetail csd = new ConfigScoreDetail(
                            cs.getName(),
                            cs.getCoefficient(),
                            cs.getQuantity(),
                            cs.getMinimum_score(),
                            "conf_score_details" + timeCurrent + "_" + UUID.randomUUID(),
                            cs.getParent_code()
                    );
                    listDetails.add(csd);
                }
                listDetails = configScoreDetailRepository.saveAll(listDetails);
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
                    ConfigScoreDetail csd = configScoreDetailRepository.findById(list[i]).get();
                    if(configScoreDetailRepository.isScored(csd.getCode()).equals(1)){
                        return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Đầu điểm này đã được nhập điểm", null);
                    }
                }
            if(list.length > 0){
                List<ConfigScoreDetail> listCSD = new ArrayList<>();
                for (int i = 0; i < list.length; i++) {
                    ConfigScoreDetail csd = configScoreDetailRepository.findById(list[i]).get();
                    listCSD.add(csd);
                }
                configScoreDetailRepository.deleteAll(listCSD);
                return new ServiceResult<>(HttpStatus.OK, "Xóa thành công", true);
            }else{
                return new ServiceResult<>(HttpStatus.OK, "empty", true);
            }
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Xóa thất bại", false);
        }
    }

    @Override
    public ServiceResult<Boolean> updateChange(List<ConfigScoreDetailForm> list) {
        try {
            for (int i = 0; i < list.size(); i++){
                if(list.get(i).getName() == null || list.get(i).getName().trim().equals("") || list.get(i).getCoefficient() == null || list.get(i).getMinimum_score() == null || list.get(i).getQuantity() == null){
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Hãy nhập đầy đủ thông tin", null);
                }
                if(list.get(i).getId() == null){
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Đầu điểm " + list.get(i).getName() + " chưa tồn tại trước đó trong môn học", null);
                }
                if(list.get(i).getCoefficient() < 1 || list.get(i).getCoefficient() > 10){
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Hệ số phải lớn hơn 0 và nhỏ hơn hoặc bằng 10", null);
                }
                if(list.get(i).getQuantity() < 1 || list.get(i).getQuantity() > 10){
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Số lượng bài kiểm tra phải lớn hơn 0 và nhỏ hơn 10", null);
                }
                if(list.get(i).getMinimum_score() < 1 || list.get(i).getMinimum_score() > 10){
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Số lượng bài kiểm tra tối thiểu phải lớn hơn 0 và nhỏ hơn 10", null);
                }
                if(list.get(i).getMinimum_score() > list.get(i).getQuantity()){
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Số lượng bài kiểm tra tối thiểu phải nhỏ hơn tổng số lượng bài kiểm tra", null);
                }
                if(list.get(i).getCode() == null || list.get(i).getCode().trim().equals("")){
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Đầu điểm '"+ list.get(i).getName() +"' chưa từng tồn tại", null);
                }
                if(list.get(i).getParent_code() == null || list.get(i).getParent_code().trim().equals("")){
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Danh sách cập nhật chứa thành phần chưa thuộc đầu điểm cho môn học nào", null);
                }
            }

            for (int i = 0; i < list.size(); i++){
                if(configScoreDetailRepository.isScored(list.get(i).getCode()).equals(1)){
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Đầu điểm này đã được nhập điểm", null);
                }
            }
            if(list.size() > 0){
                List<ConfigScoreDetail> listCSD = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    ConfigScoreDetail csd = configScoreDetailRepository.findById(list.get(i).getId()).get();
                    csd.setName(list.get(i).getName());
                    csd.setCoefficient(list.get(i).getCoefficient());
                    csd.setQuantity(list.get(i).getQuantity());
                    csd.setMinimumScore(list.get(i).getMinimum_score());
                    listCSD.add(csd);
                }
                configScoreDetailRepository.saveAll(listCSD);
                return new ServiceResult<>(HttpStatus.OK, "Cập nhật thành công", true);
            }else{
                return new ServiceResult<>(HttpStatus.OK, "empty", true);
            }
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Cập nhật thất bại", false);
        }
    }

//    @Override
//    public List<ScoreStudentVO> getScoreBySubjectCodeAndClassCodeAndSemester(String subjectCode, Integer semester, String classCode) {
//        List<Map<String, Object>> scoreStudentList=configScoreDetailRepository.getScoreStudent(subjectCode,semester,classCode);
//        List<ScoreStudentVO> scoreStudentVOList=new ArrayList<>();
//        List<ScoreVO>scoreVOList=new ArrayList<>();
//
//
//        for (Map<String, Object> sc :scoreStudentList) {
//            ScoreStudentVO scoreStudentVO=new ScoreStudentVO();
//            scoreStudentVO.setStudentCode(sc.get("student_code").toString());
//            scoreStudentVO.setStudentName(sc.get("full_name").toString());
//            scoreStudentVO.setAvgScore((Double)sc.get("avg_score"));
//            scoreStudentVOList.add(scoreStudentVO);
//
//            ScoreVO scoreVO=new ScoreVO();
//            scoreVO.setId((Integer) sc.get("confScoreDetailsId"));
//            scoreVO.setName(sc.get("confScoreDetailsName").toString());
//            scoreVO.setCoefficient((Float)sc.get("coefficient"));
//            scoreVO.setQuantity((Integer) sc.get("quantity"));
//            scoreVO.setMinimumScore((Integer) sc.get("miniumScore"));
//            scoreVOList.add(scoreVO);
//            scoreStudentVO.setScoreVOList(scoreVOList);
//
//
//        }
//        System.out.println(scoreStudentVOList.size());
//        return scoreStudentVOList;
//
//    }

}
