package fpt.capstone.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transfer_students")
@Getter
@Setter
public class TransferStudents {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code", columnDefinition = "VARCHAR(200)")
    private String code;

    @Column(name = "school_year", columnDefinition = "VARCHAR(20)")
    private String schoolYear;

    @Column(name = "grade_code", columnDefinition ="VARCHAR(20)")
    private String gradeCode;

    @Column(name = "class_code", columnDefinition = "VARCHAR(20)")
    private String classCode;

    public TransferStudents(String code, String schoolYear, String gradeCode, String classCode) {
        this.code = code;
        this.schoolYear = schoolYear;
        this.gradeCode = gradeCode;
        this.classCode = classCode;
    }
}
