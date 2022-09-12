package fpt.capstone.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "week_details")
public class WeekDetails {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code", columnDefinition = "VARCHAR(250)")
    private String code;

    @Column(name = "type", columnDefinition = "VARCHAR(100)")
    private String type;

    @Column(name = "day", columnDefinition = "VARCHAR(250)")
    private String day;

    @Column(name = "parent_code", columnDefinition = "VARCHAR(100)")
    private String parentCode;
}
