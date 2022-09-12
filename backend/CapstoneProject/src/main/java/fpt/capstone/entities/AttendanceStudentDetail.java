package fpt.capstone.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attendance_student_details")
public class AttendanceStudentDetail {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "student_code", columnDefinition = "VARCHAR(250)")
    private String studentCode;

    @Column(name = "student_name", columnDefinition = "VARCHAR(250)")
    private String studentName;

    @Column(name = "check_date", columnDefinition = "VARCHAR(50)")
    private String checkDate;

    @Column(name = "parent_code", columnDefinition = "VARCHAR(250)")
    private String parentCode;

    @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date; 
}
