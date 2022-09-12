package fpt.capstone.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Class_room")
@Getter
@Setter
public class ClassRoom {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_time", columnDefinition = "DATETIME")
    private LocalDateTime createdTime;

    @Column(name = "created_name", columnDefinition = "VARCHAR(200)")
    private String createdName;

    @Column(name = "update_time", columnDefinition = "DATETIME")
    private LocalDateTime updatedTime;

    @Column(name = "update_name", columnDefinition = "VARCHAR(200)")
    private String updatedName;

    @Column(name = "name", columnDefinition = "VARCHAR(500)")
    private String name;

    @Column(name = "code", columnDefinition = "VARCHAR(200)")
    private String code;

    @Column(name = "dept_id", columnDefinition = "INT")
    private Integer deptID;

    @Column(name = "grade_level", columnDefinition = "INT")
    private Integer gradeLevel;

    @Column(name = "years", columnDefinition = "VARCHAR(100)")
    private String years;

    @Column(name = "specialize", columnDefinition = "INT")
    private Integer specialize;

    @Column(name = "teacher_id", columnDefinition = "INT")
    private Integer teacherID;

    @Column(name = "status", columnDefinition = "INT")
    private Integer status;
}
