package fpt.capstone.entities;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "conf_score_details")
@Getter
@Setter
public class ConfigScoreDetail {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", columnDefinition = "VARCHAR(200)")
    private String name;

    @Range(min = 1, max = 10, message = "{csd.coefficient.invalid}")
    @Column(name = "coefficient")
    private Integer coefficient;

    @Range(min = 1, max = 10, message = "{csd.quantity.invalid}")
    @Column(name = "quantity")
    private Integer quantity;

    @Range(min = 1, max = 10, message = "{csd.minimum_score.invalid}")
    @Column(name = "minimum_score")
    private Integer minimumScore;

    @Column(name = "code")
    private String code;

    @Column(name = "parent_code")
    private String parentCode;

    public ConfigScoreDetail(String name, Integer coefficient, Integer quantity, Integer minimumScore, String code, String parentCode) {
        this.name = name;
        this.coefficient = coefficient;
        this.quantity = quantity;
        this.minimumScore = minimumScore;
        this.code = code;
        this.parentCode = parentCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigScoreDetail that = (ConfigScoreDetail) o;
        return Objects.equals(name, that.name) && Objects.equals(coefficient, that.coefficient) && Objects.equals(quantity, that.quantity) && Objects.equals(minimumScore, that.minimumScore) && Objects.equals(parentCode, that.parentCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, coefficient, quantity, minimumScore, parentCode);
    }


}
