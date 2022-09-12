package fpt.capstone.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gradebook_score_details")
@Getter
@Setter
public class GradebookScoreDetails {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", columnDefinition = "VARCHAR(200)")
    private String code;

    @Column(name = "parent_code", columnDefinition = "VARCHAR(200)")
    private String parentCode;

    @Column(name = "score_code", columnDefinition = "VARCHAR(200)")
    private String scoreCode;

    @Column(name = "times", columnDefinition = "INT")
    private Integer times;

    @Column(name = "value", columnDefinition = "VARCHAR(5)")
    private String value;

    @Column(name = "type", columnDefinition = "INT")
    private Integer type;

    @Column(name = "coefficient", columnDefinition = "INT")
    private Integer coefficient;

    @Override
    public String toString() {
        return "GradebookScoreDetails{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", parentCode='" + parentCode + '\'' +
                ", scoreCode=" + scoreCode +
                ", times=" + times +
                ", value='" + value + '\'' +
                ", type=" + type +
                ", coefficient=" + coefficient +
                '}';
    }

    public GradebookScoreDetails(String code, String parentCode, String scoreCode, Integer times, String value, Integer type, Integer coefficient) {
        this.code = code;
        this.parentCode = parentCode;
        this.scoreCode = scoreCode;
        this.times = times;
        this.value = value;
        this.type = type;
        this.coefficient = coefficient;
    }
}
