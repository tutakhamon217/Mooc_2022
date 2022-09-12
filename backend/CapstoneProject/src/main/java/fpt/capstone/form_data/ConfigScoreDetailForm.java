package fpt.capstone.form_data;

public class ConfigScoreDetailForm {
    private Integer id;
    private String name;
    private Integer coefficient;
    private Integer quantity;
    private Integer minimum_score;
    private String code;
    private String parent_code;

    public ConfigScoreDetailForm(Integer id, String name, Integer coefficient, Integer quantity, Integer minimum_score, String code, String parent_code) {
        this.id = id;
        this.name = name;
        this.coefficient = coefficient;
        this.quantity = quantity;
        this.minimum_score = minimum_score;
        this.code = code;
        this.parent_code = parent_code;
    }

    public ConfigScoreDetailForm() {
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

    public Integer getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Integer coefficient) {
        this.coefficient = coefficient;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getMinimum_score() {
        return minimum_score;
    }

    public void setMinimum_score(Integer minimum_score) {
        this.minimum_score = minimum_score;
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

    @Override
    public String toString() {
        return "ConfigScoreDetailForm{" +
                "name='" + name + '\'' +
                ", coefficient=" + coefficient +
                ", quantity=" + quantity +
                ", minimum_score=" + minimum_score +
                ", code='" + code + '\'' +
                ", parent_code='" + parent_code + '\'' +
                '}';
    }
}
