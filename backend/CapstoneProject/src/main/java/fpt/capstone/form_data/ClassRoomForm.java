package fpt.capstone.form_data;

public class ClassRoomForm {
    private String years;
    private String code;
    private String name;

    private Integer gradeLevel;

    private Integer deptID;

    private Integer specialize;

    private Integer teacher;

    public ClassRoomForm(String years, String code, String name, Integer gradeLevel, Integer deptID, Integer specialize, Integer teacher) {
        this.years = years;
        this.code = code;
        this.name = name;
        this.gradeLevel = gradeLevel;
        this.deptID = deptID;
        this.specialize = specialize;
        this.teacher = teacher;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(Integer gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public Integer getDeptID() {
        return deptID;
    }

    public void setDeptID(Integer deptID) {
        this.deptID = deptID;
    }

    public Integer getSpecialize() {
        return specialize;
    }

    public void setSpecialize(Integer specialize) {
        this.specialize = specialize;
    }

    public Integer getTeacher() {
        return teacher;
    }

    public void setTeacher(Integer teacher) {
        this.teacher = teacher;
    }
}
