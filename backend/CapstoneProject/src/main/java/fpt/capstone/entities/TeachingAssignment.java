package fpt.capstone.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teaching_assignment")
@Getter
@Setter
public class TeachingAssignment {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code", columnDefinition = "VARCHAR(200)")
    private String code;

    @Column(name = "teacher_code", columnDefinition = "VARCHAR(200)")
    private String teacherCode;

    @Column(name = "subject_code", columnDefinition = "VARCHAR(200)")
    private String subjectCode;

    @Column(name = "class_code", columnDefinition = "VARCHAR(200)")
    private String classCode;

    @Column(name = "apply_all_semester")
    private int applyAllSemester;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "creator", columnDefinition = "VARCHAR(200)")
    private String creator;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @Column(name = "updater", columnDefinition = "VARCHAR(200)")
    private String updater;

    @Column(name = "year", columnDefinition = "VARCHAR(20)")
    private String year;

    @Column(name = "semester_1")
    private Integer semester1;

    @Column(name = "semester_2")
    private Integer semester2;

    public TeachingAssignment(String code, String teacherCode, String subjectCode, String classCode, int applyAllSemester, LocalDate createDate, String creator, String year, Integer semester1, Integer semester2) {
        this.code = code;
        this.teacherCode = teacherCode;
        this.subjectCode = subjectCode;
        this.classCode = classCode;
        this.applyAllSemester = applyAllSemester;
        this.createDate = createDate;
        this.creator = creator;
        this.year = year;
        this.semester1 = semester1;
        this.semester2 = semester2;
    }
}
