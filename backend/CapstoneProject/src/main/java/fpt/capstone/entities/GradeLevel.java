package fpt.capstone.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grade_level")
@Getter
@Setter
public class GradeLevel {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_time")

    private LocalDateTime createdTime;


    @Column(name = "created_name", columnDefinition = "VARCHAR(200)")
    private String createdName;

    @Column(name = "update_time")

    private LocalDateTime updateTime;


    @Column(name = "update_name", columnDefinition = "VARCHAR(200)")
    private String updateName;

    @Column(name = "name", columnDefinition = "VARCHAR(500)")
    private String name;

    @Column(name = "code", columnDefinition = "VARCHAR(200)")
    private String code;
    @Column(name = "level_school")
    private int levelSchool;
}
