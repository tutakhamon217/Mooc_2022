package fpt.capstone.form_data;

public class UpdateDepartmentForm {
    private String id;
    private String code;
    private String name;
    private Integer parentID;
    private String type;
    private String description;
    private Integer employeeID;
    private Integer position;

    public UpdateDepartmentForm(String code, String name, Integer parentID, String type, String description, Integer employeeID, Integer position, String id) {
        this.code = code;
        this.name = name;
        this.parentID = parentID;
        this.type = type;
        this.description = description;
        this.employeeID = employeeID;
        this.position = position;
        this.id = id;
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }


    @Override
    public String toString() {
        return "{" +
                " code='" + getCode() + "'" +
                ", name='" + getName() + "'" +
                ", parentID='" + getParentID() + "'" +
                ", type='" + getType() + "'" +
                ", description='" + getDescription() + "'" +
                ", employeeID='" + getEmployeeID() + "'" +
                ", position='" + getPosition() + "'" +
                "}";
    }

}
