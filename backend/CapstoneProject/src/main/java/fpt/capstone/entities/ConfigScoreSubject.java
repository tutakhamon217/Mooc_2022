package fpt.capstone.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "conf_score_subjects")
@Getter
@Setter
public class ConfigScoreSubject {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "parent_code")
    private String parentCode;

    @Column(name = "subject_code")
    private String subjectCode;


    @Column(name = "semester")
    private Integer semester;

    public ConfigScoreSubject(String code, String parentCode, String subjectCode, Integer semester) {
        this.code = code;
        this.parentCode = parentCode;
        this.subjectCode = subjectCode;
        this.semester = semester;
    }
}
