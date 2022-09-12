package fpt.capstone.entities;

import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teachers")
@Getter
@Setter
public class Teacher {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "created_name", columnDefinition = "VARCHAR(250)")
    private String createdName;

    @Column(name = "avatar", columnDefinition = "TEXT")
    private String avatar;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "update_name", columnDefinition = "VARCHAR(250)")
    private String updateName;

    @Column(name = "full_name", columnDefinition = "VARCHAR(250)")
    private String fullName;

    @Column(name = "code", columnDefinition = "VARCHAR(50)")
    private String code;

    @Column(name = "dept_id", columnDefinition = "INT")
    private Integer deptID;

    @Column(name = "start_date", columnDefinition = "DATETIME")
    private LocalDateTime startDate;

    @Column(name = "contract_type", columnDefinition = "INT")
    private Integer contractType;

    @Column(name = "phone", columnDefinition = "VARCHAR(250)")
    private String phone;

    @Email(message = "{teachers.email.invalid}")
    @Column(name = "email", columnDefinition = "VARCHAR(250)")
    private String email;

    @Column(name = "birth_day", columnDefinition = "DATETIME")
    private LocalDate birthDay;

    @Column(name = "home_town", columnDefinition = "VARCHAR(250)")
    private String homeTown;

    @Column(name = "nation", columnDefinition = "VARCHAR(250)")
    private String nation;

    @Column(name = "permanent_address", columnDefinition = "VARCHAR(250)")
    private String permanentAddress;

    @Column(name = "social_insurance_number", columnDefinition = "VARCHAR(250)")
    private String socialInsuranceNumber;

    @Column(name = "identity_card", columnDefinition = "VARCHAR(250)")
    private String identityCard;

    @Column(name = "issued_address", columnDefinition = "VARCHAR(250)")
    private String issuedAddress;

    @Column(name = "issued_date", columnDefinition = "DATETIME")
    private LocalDateTime issuedDate;

    @Column(name = "marriage_status", columnDefinition = "INT")
    private Integer marriageStatus;

    @Column(name = "sex", columnDefinition = "INT")
    private Integer sex;

    @Column(name = "status", columnDefinition = "INT")
    private Integer status;

    @Column(name = "religion", columnDefinition = "VARCHAR(250)")
    private String religion;

    @Column(name = "temporary_address", columnDefinition = "VARCHAR(250)")
    private String temporaryAddress;

    public Teacher(String avatar, LocalDateTime createdTime, String createdName, String fullName, String code, Integer deptID, LocalDateTime startDate, Integer contractType, String phone, String email, LocalDate birthDay, String homeTown, String nation, String permanentAddress, String socialInsuranceNumber, String identityCard, String issuedAddress, LocalDateTime issuedDate, Integer marriageStatus, Integer sex, Integer status, String religion, String temporaryAddress) {
        this.createdTime = createdTime;
        this.createdName = createdName;
        this.avatar = avatar;
        this.fullName = fullName;
        this.code = code;
        this.deptID = deptID;
        this.startDate = startDate;
        this.contractType = contractType;
        this.phone = phone;
        this.email = email;
        this.birthDay = birthDay;
        this.homeTown = homeTown;
        this.nation = nation;
        this.permanentAddress = permanentAddress;
        this.socialInsuranceNumber = socialInsuranceNumber;
        this.identityCard = identityCard;
        this.issuedAddress = issuedAddress;
        this.issuedDate = issuedDate;
        this.marriageStatus = marriageStatus;
        this.sex = sex;
        this.status = status;
        this.religion = religion;
        this.temporaryAddress = temporaryAddress;
    }
}
