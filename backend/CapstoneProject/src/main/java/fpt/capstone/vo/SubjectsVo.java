package fpt.capstone.vo;

public class SubjectsVo {
    private Integer id;
    private String code;
    private String name;
    private Integer gradeLevel;
    private String deptName;
    private Integer type;
    private Integer subType;
    private String description;

    public SubjectsVo() {
    }

    public SubjectsVo(Integer id, String code, String name, Integer gradeLevel, String deptName, Integer type, Integer subType, String description) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.gradeLevel = gradeLevel;
        this.deptName = deptName;
        this.type = type;
        this.subType = subType;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(Integer gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
