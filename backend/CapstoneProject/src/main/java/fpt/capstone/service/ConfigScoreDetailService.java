package fpt.capstone.service;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.ConfigScoreDetailForm;
import fpt.capstone.vo.ScoreStudentVO;

import java.util.List;

public interface ConfigScoreDetailService {
    ServiceResult<List<ConfigScoreDetailForm>> saveAll(List<ConfigScoreDetailForm> list);

    ServiceResult<Boolean> deleteAll(Integer[] list);

    ServiceResult<Boolean> updateChange(List<ConfigScoreDetailForm> list);
//    List<ScoreStudentVO> getScoreBySubjectCodeAndClassCodeAndSemester(String subjectCode, Integer semester, String classCode);
}
