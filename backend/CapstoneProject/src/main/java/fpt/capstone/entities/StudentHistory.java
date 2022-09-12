package fpt.capstone.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student_history")
public class StudentHistory {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "student_code", columnDefinition = "VARCHAR(250)")
    private String studentCode;

    @Column(name = "current_class_code", columnDefinition = "VARCHAR(50)")
    private String currentClassCode;

    @Column(name = "year", columnDefinition = "VARCHAR(100)")
    private String year;


    public StudentHistory(String studentCode, String currentClassCode, String year) {
        this.studentCode = studentCode;
        this.currentClassCode = currentClassCode;
        this.year = year;
    }
}
