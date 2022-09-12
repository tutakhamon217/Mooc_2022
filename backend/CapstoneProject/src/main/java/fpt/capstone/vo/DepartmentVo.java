package fpt.capstone.vo;

import java.time.LocalDateTime;

public class DepartmentVo {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private LocalDateTime createdTime;
    private String createdName;
    private LocalDateTime updateTime;

    private String updateName;
    private String name;
    private String code;
    private Integer parentID;
    private Integer position;
    private Integer employeeID;
    private String descption;
    private String type;


    public DepartmentVo(Integer id, LocalDateTime createdTime, String createdName, LocalDateTime updateTime, String updateName, String name, String code, Integer parentID, Integer position, Integer employeeID, String descption, String type) {
        this.id = id;
        this.createdTime = createdTime;
        this.createdName = createdName;
        this.updateTime = updateTime;
        this.updateName = updateName;
        this.name = name;
        this.code = code;
        this.parentID = parentID;
        this.position = position;
        this.employeeID = employeeID;
        this.descption = descption;
        this.type = type;
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

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getDescption() {
        return descption;
    }

    public void setDescption(String descption) {
        this.descption = descption;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
