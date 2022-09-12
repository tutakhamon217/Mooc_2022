package fpt.capstone.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transfer_students_details")
@Getter
@Setter
public class TransferStudentsDetails {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code", columnDefinition = "VARCHAR(200)")
    private String code;

    @Column(name = "current_school_year", columnDefinition = "VARCHAR(20)")
    private String currentSchoolYear;

    @Column(name = "current_class_code", columnDefinition ="VARCHAR(20)")
    private String currentClassCode;

    @Column(name = "student_code", columnDefinition = "VARCHAR(20)")
    private String studentCode;

    @Column(name = "academic_ability", columnDefinition = "VARCHAR(20)")
    private String academicAbility;

    @Column(name = "conduct", columnDefinition = "VARCHAR(20)")
    private String conduct;

    @Column(name = "status", columnDefinition = "INT")
    private Integer status;

    @Column(name = "new_class_code", columnDefinition ="VARCHAR(20)")
    private String newClassCode;

    @Column(name = "new_shool_year", columnDefinition ="VARCHAR(20)")
    private String newSchoolYear;

    @Column(name = "parent_code", columnDefinition ="VARCHAR(200)")
    private String parentCode;

    public TransferStudentsDetails(String code, String currentSchoolYear, String currentClassCode, String studentCode, String academicAbility, String conduct, Integer status, String newClassCode, String newSchoolYear, String parentCode) {
        this.code = code;
        this.currentSchoolYear = currentSchoolYear;
        this.currentClassCode = currentClassCode;
        this.studentCode = studentCode;
        this.academicAbility = academicAbility;
        this.conduct = conduct;
        this.status = status;
        this.newClassCode = newClassCode;
        this.newSchoolYear = newSchoolYear;
        this.parentCode = parentCode;
    }
}
