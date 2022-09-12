package fpt.capstone.form_data;

public class ClassRoomSearchForm {
    private String years;
    private String code;
    private String name;
    private Integer gradeLevel;
    private Integer deptID;

    private Integer pageIndex;

    private Integer pageSize;

    public ClassRoomSearchForm(String years, String code, String name, Integer gradeLevel, Integer deptID, Integer pageIndex, Integer pageSize) {
        this.years = years;
        this.code = code;
        this.name = name;
        this.gradeLevel = gradeLevel;
        this.deptID = deptID;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
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

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
