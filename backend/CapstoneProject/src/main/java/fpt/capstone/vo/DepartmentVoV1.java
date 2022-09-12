package fpt.capstone.vo;

import java.time.LocalDateTime;

public class DepartmentVoV1 {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private LocalDateTime createdTime;

    private String createdName;

    private LocalDateTime updateTime;

    private String updateName;

    private String name;

    private String code;

    private DropDownVo parent;

    private Integer position;

    private DropDownVo employee;

    private String description;

    private ApParamDropDownVo type;

    public DepartmentVoV1(Integer id, LocalDateTime createdTime, String createdName, LocalDateTime updateTime, String updateName, String name, String code, Integer parentID, String parentName, Integer position, Integer employeeID, String employeeName, String description, String typeCode, String typeName) {
        this.id = id;
        this.createdTime = createdTime;
        this.createdName = createdName;
        this.updateTime = updateTime;
        this.updateName = updateName;
        this.name = name;
        this.code = code;
        this.parent = new DropDownVo(parentID, parentName);
        this.position = position;
        this.employee = new DropDownVo(employeeID, employeeName);
        this.description = description;
        this.type = new ApParamDropDownVo(typeCode, typeName);
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

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
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

    public DropDownVo getParent() {
        return parent;
    }

    public void setParent(DropDownVo parent) {
        this.parent = parent;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public DropDownVo getEmployee() {
        return employee;
    }

    public void setEmployee(DropDownVo employee) {
        this.employee = employee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ApParamDropDownVo getType() {
        return type;
    }

    public void setType(ApParamDropDownVo type) {
        this.type = type;
    }


}
