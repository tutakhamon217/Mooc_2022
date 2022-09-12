package fpt.capstone.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "departments")
public class Departments {
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

    @Column(name = "parent_id", columnDefinition = "INT")
    private Integer parentID;

    @Column(name = "position", columnDefinition = "INT")
    private Integer position;

    @Column(name = "employee_id", columnDefinition = "INT")
    private Integer employeeID;

    @Column(name = "description", columnDefinition = "TEXT")
    private String descption;

    @Column(name = "type", columnDefinition = "VARCHAR(50)")
    private String type;


    public Departments(LocalDateTime createdTime, String createdName, LocalDateTime updateTime, String updateName, String name, String code, Integer parentID, Integer position, Integer employeeID, String descption, String type) {
        this.createdTime = createdTime;
        this.createdName = createdName;
        this.updateTime = updateTime;
        this.updateName = updateName;
        this.name = name;
        this.code = code;
        this.parentID = parentID;
        this.position = position;
        this.employeeID = employeeID;
        this.descption = descption;
        this.type = type;
    }
}
