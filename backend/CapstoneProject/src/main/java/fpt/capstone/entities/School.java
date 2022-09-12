package fpt.capstone.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schools")
public class School {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "district_id")
    private Integer districtId;

    @Column(name = "province_id")
    private Integer provinceId;

    @Column(name = "abbreviation_name")
    private String abbreviationName;

    @Column(name = "address")
    private String address;

    @Column(name = "level_school")
    private String levelSchool;

    @Column(name = "type_education")
    private String typeEducation;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @Column(name = "founded_date")
    private Date foundedDate;

    @Column(name = "principal")
    private String principal;

    @Column(name = "hot_line")
    private String hotLine;

    @Column(name = "phone_principal")
    private String phonePrincipal;

    @Column(name = "email")
    private String email;

    @Column(name = "website")
    private String website;

    @Column(name = "logo")
    private String logo;

    public School(String name, String code, Integer districtId, Integer provinceId, String abbreviationName, String address, String levelSchool, String typeEducation, Date foundedDate, String principal, String hotLine, String phonePrincipal, String email, String website, String logo) {
        this.name = name;
        this.code = code;
        this.districtId = districtId;
        this.provinceId = provinceId;
        this.abbreviationName = abbreviationName;
        this.address = address;
        this.levelSchool = levelSchool;
        this.typeEducation = typeEducation;
        this.foundedDate = foundedDate;
        this.principal = principal;
        this.hotLine = hotLine;
        this.phonePrincipal = phonePrincipal;
        this.email = email;
        this.website = website;
        this.logo = logo;
    }
}
