package fpt.capstone.service;

import fpt.capstone.entities.ConfigScoreboard;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.ConfigGradeDetailForm;
import fpt.capstone.form_data.ConfigScoreDetailForm;

import java.util.List;
import java.util.Map;

public interface ConfigScoreboardService {
    List<Map<String, Object>> getDropDownListGradeByYear(String year);

    ConfigScoreboard configFromBeginningScoreboardGrading(List<ConfigGradeDetailForm> configGradeDetailForms, Integer grade_id, String year, String subjectCode, Integer semester);

    ConfigScoreboard configFromBeginningScoreboardScore(List<ConfigScoreDetailForm> configScoreDetails, Integer grade_id, String year, String subjectCode, Integer semester);
}
