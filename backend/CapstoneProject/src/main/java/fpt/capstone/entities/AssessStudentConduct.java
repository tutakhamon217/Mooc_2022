package fpt.capstone.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "assess_student_conduct")
@Getter
@Setter
public class AssessStudentConduct {
    @Id
    @Column(name = "Id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotNull
    private String class_code;

    @Column
    @NotNull
    private Integer semester;

    @Column
    @NotNull
    private String school_year;

    @Column
    private LocalDateTime create_date;

    @Column
    private String creator;

    @Column
    private LocalDateTime update_date;

    @Column
    private String updater;

    @Column
    @NotNull
    private String code;

    public AssessStudentConduct(String class_code, Integer semester, String school_year, LocalDateTime create_date, String creator, LocalDateTime update_date, String updater, String code) {
        this.class_code = class_code;
        this.semester = semester;
        this.school_year = school_year;
        this.create_date = create_date;
        this.creator = creator;
        this.update_date = update_date;
        this.updater = updater;
        this.code = code;
    }
}
