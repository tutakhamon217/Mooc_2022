package fpt.capstone.entities;

import fpt.capstone.utility.DataUtil;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gradebook_subjects_details")
@Getter
@Setter
public class GradebookSubjectsDetails {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", columnDefinition = "VARCHAR(200)")
    private String code;

    @Column(name = "student_code", columnDefinition = "VARCHAR(200)")
    private String studentCode;

    @Column(name = "parent_code", columnDefinition = "VARCHAR(200)")
    private String parentCode;

    @Column(name = "avg_score", columnDefinition = "DOUBLE")
    private Double avgScore;

    @Column(name = "subject_code", columnDefinition = "VARCHAR(200)")
    private String subjectCode;

    @Column(name = "evaluate", columnDefinition = "VARCHAR(200)")
    private String evaluate;

    @Column(name = "updater", columnDefinition = "VARCHAR(200)")
    private String updater;

    @Column(name = "evaluate_status", columnDefinition = "INT")
    private Integer evaluateStatus;

    public GradebookSubjectsDetails(Double avgScore, String updater) {
        this.avgScore = avgScore;
        this.updater = updater;
    }

    public GradebookSubjectsDetails(String code, String studentCode, String parentCode, Double avgScore, String subjectCode, String updater) {
        this.code = code;
        this.studentCode = studentCode;
        this.parentCode = parentCode;
        this.avgScore = avgScore;
        this.subjectCode = subjectCode;
        this.updater = updater;
    }

}
