package fpt.capstone.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gradebook")
@Getter
@Setter
public class Gradebook {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", columnDefinition = "VARCHAR(200)")
    private String code;

    @Column(name = "school_year", columnDefinition = "VARCHAR(200)")
    private String schoolYear;

    @Column(name = "semester")
    private String semester;

    @Column(name = "class_code", columnDefinition = "VARCHAR(200)")
    private String classCode;

    public Gradebook(String code, String schoolYear, String semester, String classCode) {
        this.code = code;
        this.schoolYear = schoolYear;
        this.semester = semester;
        this.classCode = classCode;
    }
}
