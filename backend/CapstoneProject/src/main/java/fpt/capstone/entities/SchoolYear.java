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
@Table(name = "school_year")
public class SchoolYear {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "years", columnDefinition = "VARCHAR(100)")
    private String years;

    @Column(name = "semester_amount", columnDefinition = "INT")
    private int semesterAmount;

    @Column(name = "semester", columnDefinition = "VARCHAR(255)")
    private String semester;

    @Column(name = "from_date")
    private LocalDateTime fromDate;

    @Column(name = "to_date")
    private LocalDateTime toDate;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "created_name", columnDefinition = "VARCHAR(200)")
    private String createdName;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "update_name", columnDefinition = "VARCHAR(200)")
    private String updateName;


    public SchoolYear(String years, int semesterAmount, String semester, LocalDateTime fromDate, LocalDateTime toDate, LocalDateTime createdTime, String createdName, LocalDateTime updateTime, String updateName) {
        this.years = years;
        this.semesterAmount = semesterAmount;
        this.semester = semester;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.createdTime = createdTime;
        this.createdName = createdName;
        this.updateTime = updateTime;
        this.updateName = updateName;
    }

}
