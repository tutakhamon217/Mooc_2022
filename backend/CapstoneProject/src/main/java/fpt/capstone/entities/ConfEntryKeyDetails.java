package fpt.capstone.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "conf_entry_key_details")
@Getter
@Setter
public class ConfEntryKeyDetails {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type_subjects", columnDefinition = "INT")
    private Integer typeSubjects;

    @Column(name = "score_name", columnDefinition = "VARCHAR(200)")
    private String scoreName;

    @Column(name = "entry_lock_date", columnDefinition = "DATETIME")
    private LocalDate entryLockDate;

    @Column(name = "created_date", columnDefinition = "DATETIME")
    private LocalDate createdDate;

    @Column(name = "updated_date", columnDefinition = "DATETIME")
    private LocalDate updatedDate;

    @Column(name = "updater_code", columnDefinition = "VARCHAR(200)")
    private String updaterCode;

    @Column(name = "status", columnDefinition = "INT")
    private Integer status;

    @Column(name = "code", columnDefinition = "VARCHAR(200)")
    private String code;

    @Column(name = "parent_code", columnDefinition = "VARCHAR(200)")
    private String parentCode;
}
