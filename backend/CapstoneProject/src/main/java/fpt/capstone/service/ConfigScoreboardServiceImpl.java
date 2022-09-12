package fpt.capstone.service;

import fpt.capstone.entities.*;
import fpt.capstone.form_data.ConfigGradeDetailForm;
import fpt.capstone.form_data.ConfigScoreDetailForm;
import fpt.capstone.repository.ConfigGradeDetailRepository;
import fpt.capstone.repository.ConfigScoreDetailRepository;
import fpt.capstone.repository.ConfigScoreSubjectRepository;
import fpt.capstone.repository.ConfigScoreboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class ConfigScoreboardServiceImpl implements ConfigScoreboardService {

    @Autowired
    private ConfigScoreboardRepository configScoreboardRepository;

    @Autowired
    private ConfigScoreSubjectRepository configScoreSubjectRepository;

    @Autowired
    private ConfigScoreDetailRepository configScoreDetailRepository;

    @Autowired
    private ConfigGradeDetailRepository configGradeDetailRepository;

    @Override
    public List<Map<String, Object>> getDropDownListGradeByYear(String year) {
        return configScoreboardRepository.getDropDownListGradeByYear(year);
    }

    @Override
    public ConfigScoreboard configFromBeginningScoreboardGrading(List<ConfigGradeDetailForm> configGradeDetailForms,
                                                                 Integer grade_id,
                                                                 String year,
                                                                 String subjectCode,
                                                                 Integer semester) {
        ConfigScoreboard create = new ConfigScoreboard();
        create.setYear(year);
        create.setGradeId(grade_id);
        Calendar calendar = Calendar.getInstance();
        String code;
        code = "scoreboard_"
                + calendar.get(Calendar.YEAR)
                + (calendar.get(Calendar.MONTH) + 1)
                + calendar.get(Calendar.DATE)
                + "_"
                + calendar.get(Calendar.HOUR)
                + calendar.get(Calendar.MINUTE)
                + calendar.get(Calendar.MILLISECOND);
        create.setCode(code);
        ConfigScoreboard csb = configScoreboardRepository.save(create);


        ConfigScoreSubject css = new ConfigScoreSubject();
        String codeCSS = "conf_score_subject_"
                + subjectCode
                + "_"
                + calendar.get(Calendar.YEAR)
                + (calendar.get(Calendar.MONTH) + 1)
                + calendar.get(Calendar.DATE)
                + "_"
                + calendar.get(Calendar.HOUR)
                + calendar.get(Calendar.MINUTE)
                + calendar.get(Calendar.MILLISECOND);
        css.setCode(codeCSS);
        css.setSubjectCode(subjectCode);
        css.setParentCode(code);
        css.setSemester(semester);
        configScoreSubjectRepository.save(css);

        String timeCurrent = calendar.get(Calendar.YEAR)
                + (calendar.get(Calendar.MONTH) + 1)
                + calendar.get(Calendar.DATE)
                + "_"
                + calendar.get(Calendar.HOUR)
                + calendar.get(Calendar.MINUTE)
                + calendar.get(Calendar.MILLISECOND);
        for (ConfigGradeDetailForm obj : configGradeDetailForms) {
            ConfigGradeDetail csd = new ConfigGradeDetail();
            csd.setName(obj.getName());
            csd.setSelectedValue(obj.getSelected_value());
            csd.setTypeChoose(obj.getType_choose());
            String codeCSD = "conf_grading_details" + timeCurrent + "_" + UUID.randomUUID();
            csd.setCode(codeCSD);
            csd.setParentCode(codeCSS);
            configGradeDetailRepository.save(csd);
        }

        return csb;
    }

    @Override
    public ConfigScoreboard configFromBeginningScoreboardScore(List<ConfigScoreDetailForm> configScoreDetails,
                                                               Integer grade_id,
                                                               String year,
                                                               String subjectCode,
                                                               Integer semester) {
        ConfigScoreboard create = new ConfigScoreboard();
        create.setYear(year);
        create.setGradeId(grade_id);
        Calendar calendar = Calendar.getInstance();
        String code;
        code = "scoreboard_"
                + calendar.get(Calendar.YEAR)
                + (calendar.get(Calendar.MONTH) + 1)
                + calendar.get(Calendar.DATE)
                + "_"
                + calendar.get(Calendar.HOUR)
                + calendar.get(Calendar.MINUTE)
                + calendar.get(Calendar.MILLISECOND);
        create.setCode(code);
        ConfigScoreboard csb = configScoreboardRepository.save(create);


        ConfigScoreSubject css = new ConfigScoreSubject();
        String codeCSS = "conf_score_subject_"
                + subjectCode
                + "_"
                + calendar.get(Calendar.YEAR)
                + (calendar.get(Calendar.MONTH) + 1)
                + calendar.get(Calendar.DATE)
                + "_"
                + calendar.get(Calendar.HOUR)
                + calendar.get(Calendar.MINUTE)
                + calendar.get(Calendar.MILLISECOND);
        css.setCode(codeCSS);
        css.setSubjectCode(subjectCode);
        css.setParentCode(code);
        css.setSemester(semester);
        configScoreSubjectRepository.save(css);

        String timeCurrent = calendar.get(Calendar.YEAR)
                + (calendar.get(Calendar.MONTH) + 1)
                + calendar.get(Calendar.DATE)
                + "_"
                + calendar.get(Calendar.HOUR)
                + calendar.get(Calendar.MINUTE)
                + calendar.get(Calendar.MILLISECOND);
        for (ConfigScoreDetailForm obj : configScoreDetails) {
            ConfigScoreDetail csd = new ConfigScoreDetail();
            csd.setName(obj.getName());
            csd.setCoefficient(obj.getCoefficient());
            csd.setQuantity(obj.getQuantity());
            csd.setMinimumScore(obj.getMinimum_score());
            String codeCSD = "conf_score_details" + timeCurrent + "_" + UUID.randomUUID();
            csd.setCode(codeCSD);
            csd.setParentCode(codeCSS);
            configScoreDetailRepository.save(csd);
        }

        return csb;
    }
}
