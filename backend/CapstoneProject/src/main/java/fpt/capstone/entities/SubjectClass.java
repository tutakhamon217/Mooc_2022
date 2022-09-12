package fpt.capstone.entities;

import com.sun.istack.Nullable;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subject_class")
@Getter
@Setter
public class SubjectClass {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "class_id")
    private int classId;

    @Column(name = "subject_id")
    private int subjectId;

    @Range(min = 0, max = 10, message = "{subjects.coefficient.invalid}")
    @Column(name = "coefficient")
    @Nullable
    private Integer coefficient;

    @Column(name = "flg_semester1")
    @Nullable
    private Integer flgSemester1;

    @Column(name = "flg_semester2")
    private Integer flgSemester2;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "created_name", columnDefinition = "VARCHAR(200)")
    private String createdName;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "update_name", columnDefinition = "VARCHAR(200)")
    private String updateName;

    public SubjectClass(int classId, int subjectId, @Range(min = 0, max = 10, message = "{subjects.coefficient.invalid}") Integer coefficient, Integer flgSemester1, Integer flgSemester2, LocalDateTime createdTime, String createdName, LocalDateTime updateTime, String updateName) {
        this.classId = classId;
        this.subjectId = subjectId;
        this.coefficient = coefficient;
        this.flgSemester1 = flgSemester1;
        this.flgSemester2 = flgSemester2;
        this.createdTime = createdTime;
        this.createdName = createdName;
        this.updateTime = updateTime;
        this.updateName = updateName;
    }
}
