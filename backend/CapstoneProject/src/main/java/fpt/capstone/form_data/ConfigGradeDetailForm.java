package fpt.capstone.form_data;

public class ConfigGradeDetailForm {
    private Integer id;
    private String name;
    private Integer type_choose;
    private String selected_value;
    private String code;
    private String parent_code;

    public ConfigGradeDetailForm(Integer id, String name, Integer type_choose, String selected_value, String code, String parent_code) {
        this.id = id;
        this.name = name;
        this.type_choose = type_choose;
        this.selected_value = selected_value;
        this.code = code;
        this.parent_code = parent_code;
    }

    public ConfigGradeDetailForm() {
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

    public Integer getType_choose() {
        return type_choose;
    }

    public void setType_choose(Integer type_choose) {
        this.type_choose = type_choose;
    }

    public String getSelected_value() {
        return selected_value;
    }

    public void setSelected_value(String selected_value) {
        this.selected_value = selected_value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParent_code() {
        return parent_code;
    }

    public void setParent_code(String parent_code) {
        this.parent_code = parent_code;
    }
}
