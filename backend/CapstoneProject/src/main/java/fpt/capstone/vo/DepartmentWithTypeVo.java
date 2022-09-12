
package fpt.capstone.vo;

public class DepartmentWithTypeVo {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String code;
    private String type;

    public DepartmentWithTypeVo(Integer id, String name, String type) {
        this.id = id;
        this.name = name;
        this.code = null;
        this.type = type;
    }


    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
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
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
