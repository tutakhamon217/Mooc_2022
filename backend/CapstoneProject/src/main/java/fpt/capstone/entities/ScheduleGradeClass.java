package fpt.capstone.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedule_grade_class")
@Getter
@Setter
public class ScheduleGradeClass {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", columnDefinition = "VARCHAR(250)")
    private String code;

    @Column(name = "grade_level", columnDefinition = "VARCHAR(250)")
    private String gradeLevel;

    @Column(name = "parent_code", columnDefinition = "VARCHAR(250)")
    private String parentCode;

    @Column(name = "end_date", columnDefinition = "DATE")
    private LocalDate endDate;

    @Column(name = "apply_date", columnDefinition = "DATE")
    private LocalDate applyDate;

    @Column(name = "class_code", columnDefinition = "VARCHAR(250)")
    private String classCode;

    @Column(name = "crate_date", columnDefinition = "DATE")
    private LocalDate createdDate;

    @Column(name = "creator", columnDefinition = "VARCHAR(250)")
    private String creator;
}
