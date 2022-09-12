package fpt.capstone.vo;

public class DropDownVo {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String code;

    public DropDownVo(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.code = null;
    }


    public DropDownVo(Integer id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
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
