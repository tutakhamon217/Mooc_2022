package fpt.capstone.vo;

public class academicAbilityVo {
    String studentCode;
    String academicAbility;


    public academicAbilityVo() {
    }

    public academicAbilityVo(String studentCode, String academicAbility) {
        this.studentCode = studentCode;
        this.academicAbility = academicAbility;
    }

    public String getStudentCode() {
        return this.studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getAcademicAbility() {
        return this.academicAbility;
    }

    public void setAcademicAbility(String academicAbility) {
        this.academicAbility = academicAbility;
    }

}
