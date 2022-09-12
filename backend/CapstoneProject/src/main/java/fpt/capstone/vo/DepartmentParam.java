package fpt.capstone.vo;

public class DepartmentParam {
    private static final long serialVersionUID = 1L;

    private String code;

    private String value;

    private String name;

    public DepartmentParam(String code, String value, String name) {
        this.code = code;
        this.value = value;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DepartmentParam{" +
                "code='" + code + '\'' +
                ", value='" + value + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
