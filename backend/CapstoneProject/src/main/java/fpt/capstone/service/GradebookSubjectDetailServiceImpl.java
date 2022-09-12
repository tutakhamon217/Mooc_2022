package fpt.capstone.service;

import fpt.capstone.entities.Gradebook;
import fpt.capstone.entities.GradebookSubjectsDetails;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.repository.GradebookRepository;
import fpt.capstone.repository.GradebookSubjectsDetailsRepository;
import fpt.capstone.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class GradebookSubjectDetailServiceImpl implements GradebookSubjectDetailService{
    @Autowired
    private GradebookSubjectsDetailsRepository gradebookSubjectsDetailsRepository;
    @Autowired
    private GradebookRepository gradebookRepository;


    @Override
    public ServiceResult<Boolean> saveEvaluate(String year, String classCode, Integer semester, List<String> listStudentCode, String evaluate, String subjectCode) {
        ServiceResult serviceResult = new ServiceResult();
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
            String updater = userDetails.getUsername();
            //kiểm tra xem tồn tại trong bảng gradebook chưa
            if(gradebookRepository.checkExistGradeBook(year,semester,classCode)){
                //đẫ tồn tại trong gradeobook
                //lấy và kiểm tra xem học sinh này với môn này đã tồn tại trong gradebooksubjectdetail chưa
                Map<String, Object> getCodeByClassYearSemester = gradebookRepository.getCodeByClassYearSemester(classCode,year,semester).get(0);
                for (int i = 0; i < listStudentCode.size(); i++){
                    List<GradebookSubjectsDetails> list = gradebookSubjectsDetailsRepository.getGSDbyParentCodeAndStudentCodeAndSubjectCode(getCodeByClassYearSemester.get("code").toString(), listStudentCode.get(i), subjectCode);
                    if(list.size() != 0){
                        //đã tồn tại trong gradebook subject detail
                        GradebookSubjectsDetails gsd = list.get(0);
                        gsd.setEvaluate(evaluate);
                        gsd.setEvaluateStatus(1);
                        gsd.setUpdater(updater);
                        gradebookSubjectsDetailsRepository.save(gsd);
                    }else{
                        //chưa tồn tại trong gradebook subject detail
                        GradebookSubjectsDetails gsd = new GradebookSubjectsDetails();
                        gsd.setUpdater(updater);
                        gsd.setEvaluate(evaluate);
                        gsd.setSubjectCode(subjectCode);
                        gsd.setParentCode(getCodeByClassYearSemester.get("code").toString());
                        gsd.setEvaluateStatus(1);
                        gsd.setStudentCode(listStudentCode.get(i));
                        gsd.setCode(UUID.randomUUID().toString().replace("-", ""));
                        gradebookSubjectsDetailsRepository.save(gsd);
                    }
                }
            }else{
                //chưa tồn tại trong gradebook
                Gradebook gradebook = new Gradebook();
                String codeGradeBook = UUID.randomUUID().toString().replace("-", "");
                gradebook.setCode(codeGradeBook);
                gradebook.setSemester(semester.toString());
                gradebook.setSchoolYear(year);
                gradebook.setClassCode(classCode);
                gradebookRepository.save(gradebook);
                for(int i = 0; i < listStudentCode.size(); i++){
                    GradebookSubjectsDetails gsd = new GradebookSubjectsDetails();
                    gsd.setUpdater(updater);
                    gsd.setEvaluate(evaluate);
                    gsd.setSubjectCode(subjectCode);
                    gsd.setParentCode(codeGradeBook);
                    gsd.setEvaluateStatus(1);
                    gsd.setStudentCode(listStudentCode.get(i));
                    gsd.setCode(UUID.randomUUID().toString().replace("-", ""));
                    gsd.setAvgScore(null);
                    gradebookSubjectsDetailsRepository.save(gsd);
                }
            }
            serviceResult.setData(true);
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setMessage("Cập nhật đánh giá hạnh kiểm thành công");
            return serviceResult;
        }catch (Exception e){
            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
            serviceResult.setMessage("Cập nhật thất bại");
            return serviceResult;
        }
    }

  
}
