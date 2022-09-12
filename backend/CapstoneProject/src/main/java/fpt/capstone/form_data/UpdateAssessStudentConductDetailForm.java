package fpt.capstone.form_data;

import java.util.ArrayList;
import java.util.List;

public class UpdateAssessStudentConductDetailForm {
    private List<AssessStudentConductDetailForm> list = new ArrayList<>();
    private String classCode;
    private Integer semester;
    private String schoolYear;

    public UpdateAssessStudentConductDetailForm() {
    }

    public UpdateAssessStudentConductDetailForm(List<AssessStudentConductDetailForm> list, String classCode, Integer semester, String schoolYear) {
        this.list = list;
        this.classCode = classCode;
        this.semester = semester;
        this.schoolYear = schoolYear;
    }

    public List<AssessStudentConductDetailForm> getList() {
        return list;
    }

    public void setList(List<AssessStudentConductDetailForm> list) {
        this.list = list;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }
}
