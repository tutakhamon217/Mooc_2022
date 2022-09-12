package fpt.capstone.entities;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subjects")
@Getter
@Setter
public class Subjects {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "created_name", columnDefinition = "VARCHAR(200)")
    private String createdName;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "update_name", columnDefinition = "VARCHAR(200)")
    private String updateName;

    @Column(name = "name", columnDefinition = "VARCHAR(200)")
    private String name;

    @Column(name = "code")
    private String code;

    @Range(min = 1, max = 12, message = "1<gradeLevel<12")
    @Column(name = "grade_level")
    private Integer gradeLevel;

    @Range(min = 0, max = 1, message = "{subjects.type.invalid}")
    @Column(name = "type")
    private Integer type;

    @Range(min = 0, max = 1, message = "{sujects.subType.invalid}")
    @Column(name = "sub_type")
    private Integer subType;

    @Column(name = "description")
    private String description;

    public Subjects(LocalDateTime createdTime, String createdName, LocalDateTime updateTime, String updateName, String name, String code, @Range(min = 1, max = 12, message = "{subjects.gradeLevel.invalid}") Integer gradeLevel, @Range(min = 0, max = 11, message = "{subjects.type.invalid}") Integer type, @Range(min = 0, max = 1, message = "{sujects.subType.invalid}") Integer subType, String description) {
        this.createdTime = createdTime;
        this.createdName = createdName;
        this.updateTime = updateTime;
        this.updateName = updateName;
        this.name = name;
        this.code = code;
        this.gradeLevel = gradeLevel;
        this.type = type;
        this.subType = subType;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Subjects{" +
                "id=" + id +
                ", createdTime=" + createdTime +
                ", createdName='" + createdName + '\'' +
                ", updateTime=" + updateTime +
                ", updateName='" + updateName + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", gradeLevel=" + gradeLevel +
                ", type=" + type +
                ", subType=" + subType +
                ", description='" + description + '\'' +
                '}';
    }
}
