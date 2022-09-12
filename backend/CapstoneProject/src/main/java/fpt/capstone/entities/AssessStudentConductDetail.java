package fpt.capstone.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assess_student_conduct_details")
@Getter
@Setter
public class AssessStudentConductDetail {
    @Id
    @Column(name = "Id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotNull
    private String student_code;

    @Column
    private Integer number_off;

    @Column
    private Integer number_off_allowed;

    @Column
    private String academic_ability;

    @Column
    private String conduct;

    @Column
    private String competition_title;

    @Column
    @NotNull
    private String parent_code;

    public AssessStudentConductDetail(String student_code, Integer number_off, Integer number_off_allowed, String academic_ability, String conduct, String competition_title, String parent_code) {
        this.student_code = student_code;
        this.number_off = number_off;
        this.number_off_allowed = number_off_allowed;
        this.academic_ability = academic_ability;
        this.conduct = conduct;
        this.competition_title = competition_title;
        this.parent_code = parent_code;
    }
}
