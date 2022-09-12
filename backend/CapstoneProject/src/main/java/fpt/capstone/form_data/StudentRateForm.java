package fpt.capstone.form_data;
import fpt.capstone.vo.academicAbilityVo;


import java.util.List;


public class StudentRateForm {
    private String classCode;
    private String year;
    private Integer semester;
    private List<academicAbilityVo> rateList;


    public StudentRateForm() {
    }

    public StudentRateForm(String classCode, String year, Integer semester, List<academicAbilityVo> rateList) {
        this.classCode = classCode;
        this.year = year;
        this.semester = semester;
        this.rateList = rateList;
    }


    public String getClassCode() {
        return this.classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getYear() {
        return this.year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getSemester() {
        return this.semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public List<academicAbilityVo> getRateList() {
        return this.rateList;
    }

    public void setRateList(List<academicAbilityVo> rateList) {
        this.rateList = rateList;
    }
}