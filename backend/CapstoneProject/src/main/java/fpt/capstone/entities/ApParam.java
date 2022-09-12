package fpt.capstone.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ap_param")
public class ApParam {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", columnDefinition = "VARCHAR(1000)")
    private String name;

    @Column(name = "code", columnDefinition = "VARCHAR(1000)")
    private String code;

    @Column(name = "value", columnDefinition = "VARCHAR(1000)")
    private String value;

    @Column(name = "parent_code", columnDefinition = "VARCHAR(100)")
    private String parentCode;

    @Column(name = "type", columnDefinition = "VARCHAR(250)")
    private String type;
}
