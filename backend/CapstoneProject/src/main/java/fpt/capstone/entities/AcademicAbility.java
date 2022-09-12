package fpt.capstone.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "academic_ability")
public class AcademicAbility {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "student_code")
    private String studentCode;

    @Column(name = "parent_code")
    private String parentCode;

    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "creator")
    private String creator;

    @Column(name = "academic_ability")
    private String academic_Ability;


    public AcademicAbility(String code, String studentCode, String parentCode, LocalDate createDate, String creator, String academic_Ability) {
        this.code = code;
        this.studentCode = studentCode;
        this.parentCode = parentCode;
        this.createDate = createDate;
        this.creator = creator;
        this.academic_Ability = academic_Ability;
    }

}
