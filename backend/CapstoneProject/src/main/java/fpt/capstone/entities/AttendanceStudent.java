package fpt.capstone.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attendance_student")
public class AttendanceStudent {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "code", columnDefinition = "VARCHAR(250)")
    private String code;

    @Column(name = "shool_year", columnDefinition = "VARCHAR(50)")
    private String schoolYear;

    @Column(name = "semester", columnDefinition = "VARCHAR(250)")
    private String semester;

    @Column(name = "month", columnDefinition = "VARCHAR(50)")
    private String month;
}
