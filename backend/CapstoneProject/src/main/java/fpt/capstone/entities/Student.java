package fpt.capstone.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.time.LocalDateTime;

import com.sun.istack.Nullable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "students")
public class Student {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "created_name", columnDefinition = "VARCHAR(250)")
    private String createdName;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "update_name", columnDefinition = "VARCHAR(250)")
    private String updateName;

    @Column(name = "full_name", columnDefinition = "VARCHAR(250)")
    private String fullName;

    @Column(name = "code", columnDefinition = "VARCHAR(50) default 0")
    private String code;

    @Column(name = "dept_id")
    private Integer deptId;

    @Column(name = "start_date")
    @Nullable
    private LocalDateTime startDate;

    @Column(name = "phone", columnDefinition = "VARCHAR(250)")
    private String phone;

    @Column(name = "email", columnDefinition = "VARCHAR(250)")
    private String email;

    @Column(name = "birth_day")
    @Nullable
    private LocalDateTime birthDay;

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

    @Column(name = "issued_date")
    @Nullable
    private LocalDateTime issuedDate;

    @Column(name = "sex")
    private Integer sex;

    @Column(name = "avatar", columnDefinition = "VARCHAR(250)")
    private String avatar;

    @Column(name = "elect_format")
    private Integer electFormat;

    @Column(name = "graduation_type")
    private Integer graduationType;

    @Column(name = "status")
    private Integer status;

    @Column(name = "training_system")
    private Integer trainingSystem;

    @Column(name = "religion", columnDefinition = "VARCHAR(250)")
    private String religion;

    @Column(name = "temporary_address", columnDefinition = "VARCHAR(250)")
    private String temporaryAddress;


    public Student(LocalDateTime createdTime, String createdName, LocalDateTime updateTime, String updateName, String fullName, String code, Integer deptId, LocalDateTime startDate, String phone, String email, LocalDateTime birthDay, String homeTown, String nation, String permanentAddress, String socialInsuranceNumber, String identityCard, String issuedAddress, LocalDateTime issuedDate, Integer sex, String avatar, Integer electFormat, Integer graduationType, Integer status, Integer trainingSystem, String religion, String temporaryAddress) {
        this.createdTime = createdTime;
        this.createdName = createdName;
        this.updateTime = updateTime;
        this.updateName = updateName;
        this.fullName = fullName;
        this.code = code;
        this.deptId = deptId;
        this.startDate = startDate;
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
        this.sex = sex;
        this.avatar = avatar;
        this.electFormat = electFormat;
        this.graduationType = graduationType;
        this.status = status;
        this.trainingSystem = trainingSystem;
        this.religion = religion;
        this.temporaryAddress = temporaryAddress;
    }


    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", createdTime='" + getCreatedTime() + "'" +
                ", createdName='" + getCreatedName() + "'" +
                ", updateTime='" + getUpdateTime() + "'" +
                ", updateName='" + getUpdateName() + "'" +
                ", fullName='" + getFullName() + "'" +
                ", code='" + getCode() + "'" +
                ", deptId='" + getDeptId() + "'" +
                ", startDate='" + getStartDate() + "'" +
                ", phone='" + getPhone() + "'" +
                ", email='" + getEmail() + "'" +
                ", birthDay='" + getBirthDay() + "'" +
                ", homeTown='" + getHomeTown() + "'" +
                ", nation='" + getNation() + "'" +
                ", permanentAddress='" + getPermanentAddress() + "'" +
                ", socialInsuranceNumber='" + getSocialInsuranceNumber() + "'" +
                ", identityCard='" + getIdentityCard() + "'" +
                ", issuedAddress='" + getIssuedAddress() + "'" +
                ", issuedDate='" + getIssuedDate() + "'" +
                ", sex='" + getSex() + "'" +
                ", avatar='" + getAvatar() + "'" +
                ", electFormat='" + getElectFormat() + "'" +
                ", graduationType='" + getGraduationType() + "'" +
                ", status='" + getStatus() + "'" +
                ", trainingSystem='" + getTrainingSystem() + "'" +
                ", religion='" + getReligion() + "'" +
                ", temporaryAddress='" + getTemporaryAddress() + "'" +
                "}";
    }

}
