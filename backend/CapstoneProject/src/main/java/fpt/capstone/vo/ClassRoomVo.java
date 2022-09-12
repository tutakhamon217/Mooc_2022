package fpt.capstone.vo;

import java.time.LocalDateTime;

public class ClassRoomVo {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private LocalDateTime createdTime;
    private String createdName;
    private LocalDateTime updatedTime;
    private String updatedName;
    private String name;
    private String code;
    private DropDownVo department;
    private DropDownVo gradeLevel;
    private String years;
    private DropDownVo specialize;
    private DropDownVo teacher;
    private Integer status;

    public ClassRoomVo(Integer id, LocalDateTime createdTime, String createdName, LocalDateTime updatedTime, String updatedName, String name, String code, Integer departmentID, String departmentName, Integer gradeLevelID, String gradeLevelName, String years, Integer specializeID, String specializeName, Integer teacherID, String teacherName, Integer status) {
        this.id = id;
        this.createdTime = createdTime;
        this.createdName = createdName;
        this.updatedTime = updatedTime;
        this.updatedName = updatedName;
        this.name = name;
        this.code = code;
        this.department = new DropDownVo(departmentID, departmentName);
        this.gradeLevel = new DropDownVo(gradeLevelID, gradeLevelName);
        this.years = years;
        this.specialize = new DropDownVo(specializeID, specializeName);
        this.teacher = new DropDownVo(teacherID, teacherName);
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUpdatedName() {
        return updatedName;
    }

    public void setUpdatedName(String updatedName) {
        this.updatedName = updatedName;
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

    public DropDownVo getDepartment() {
        return department;
    }

    public void setDepartment(DropDownVo department) {
        this.department = department;
    }

    public DropDownVo getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(DropDownVo gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public DropDownVo getSpecialize() {
        return specialize;
    }

    public void setSpecialize(DropDownVo specialize) {
        this.specialize = specialize;
    }

    public DropDownVo getTeacher() {
        return teacher;
    }

    public void setTeacher(DropDownVo teacher) {
        this.teacher = teacher;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
