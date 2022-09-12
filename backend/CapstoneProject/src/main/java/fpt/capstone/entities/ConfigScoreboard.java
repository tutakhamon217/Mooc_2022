package fpt.capstone.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scoreboard_conf")
@Getter
@Setter
public class ConfigScoreboard {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "year")
    private String year;

    @Column(name = "grade_id")
    private Integer gradeId;

    @Column(name = "code")
    private String code;

    public ConfigScoreboard(String year, Integer gradeId, String code) {
        this.year = year;
        this.gradeId = gradeId;
        this.code = code;
    }
}
