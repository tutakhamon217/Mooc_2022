package fpt.capstone.entities;

import fpt.capstone.utility.DataUtil;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "conf_grading_details")
@Getter
@Setter
public class ConfigGradeDetail {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", columnDefinition = "VARCHAR(200)")
    private String name;

    @Range(min = 0, max = 1, message = "{cgd.type_choose.invalid}")
    @Column(name = "type_choose")
    private Integer typeChoose;

    @Column(name = "selected_value")
    private String selectedValue;

    @Column(name = "code")
    private String code;

    @Column(name = "parent_code")
    private String parentCode;

    public ConfigGradeDetail(String name, Integer typeChoose, String selectedValue, String code, String parentCode) {
        this.name = name;
        this.typeChoose = typeChoose;
        this.selectedValue = selectedValue;
        this.code = code;
        this.parentCode = parentCode;
    }

    public ConfigGradeDetail(int id, String name, @Range(min = 0, max = 1, message = "{cgd.type_choose.invalid}") Integer typeChoose, String selectedValue, String code) {
        this.id = id;
        this.name = name;
        this.typeChoose = typeChoose;
        this.selectedValue = selectedValue;
        this.code = code;

}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigGradeDetail that = (ConfigGradeDetail) o;
        return Objects.equals(name, that.name) && Objects.equals(typeChoose, that.typeChoose) && Objects.equals(selectedValue, that.selectedValue) && Objects.equals(parentCode, that.parentCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, typeChoose, selectedValue, parentCode);
    }
}
