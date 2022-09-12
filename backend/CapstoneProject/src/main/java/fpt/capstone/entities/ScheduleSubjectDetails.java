package fpt.capstone.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "schedule_subjects_details")
public class ScheduleSubjectDetails {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code", columnDefinition = "VARCHAR(250)")
    private String code;

    @Column(name = "subject_code", columnDefinition = "VARCHAR(250)")
    private String subjectCode;

    @Column(name = "parent_code", columnDefinition = "VARCHAR(250)")
    private String parentCode;

    @Column(name = "lesson", columnDefinition = "VARCHAR(100)")
    private String lession;



}
