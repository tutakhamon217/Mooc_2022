package fpt.capstone.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedule_timetable")
@Getter
@Setter
public class ScheduleTimetable {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", columnDefinition = "VARCHAR(250)")
    private String code;

    @Column(name = "semester", columnDefinition = "VARCHAR(100)")
    private String semester;

    @Column(name = "create_date", columnDefinition = "DATE")
    private LocalDate createdDate;

    @Column(name = "school_year", columnDefinition = "VARCHAR(50)")
    private String schoolYear;

    @Column(name = "update_date", columnDefinition = "DATE")
    private LocalDate updateDate;

    @Column(name = "creator", columnDefinition = "VARCHAR(250)")
    private String creator;

    @Column(name = "updater", columnDefinition = "VARCHAR(250)")
    private String updater;
}
