package fpt.capstone.form_data;

import java.util.List;

public class CreateScoreDetailFromScoreboardForm {
    private Integer grade_id;
    private String year;
    private String subjectCode;
    private Integer semester;
    private List<ConfigScoreDetailForm> configScoreDetails;

    public CreateScoreDetailFromScoreboardForm(Integer grade_id, String year, String subjectCode, Integer semester, List<ConfigScoreDetailForm> configScoreDetails) {
        this.grade_id = grade_id;
        this.year = year;
        this.subjectCode = subjectCode;
        this.semester = semester;
        this.configScoreDetails = configScoreDetails;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Integer getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(Integer grade_id) {
        this.grade_id = grade_id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public List<ConfigScoreDetailForm> getConfigScoreDetails() {
        return configScoreDetails;
    }

    public void setConfigScoreDetails(List<ConfigScoreDetailForm> configScoreDetails) {
        this.configScoreDetails = configScoreDetails;
    }
}
