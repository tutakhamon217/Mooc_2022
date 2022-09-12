package fpt.capstone.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contacts")
public class Contact {
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_name", columnDefinition = "VARCHAR(250)")
    private String createdName;

    @Column(name = "created_time", columnDefinition = "DATETIME")
    private LocalDateTime createdTime;

    @Column(name = "full_name", columnDefinition = "VARCHAR(250)")
    private String fullName;

    @Column(name = "phone", columnDefinition = "VARCHAR(20)")
    private String phone;

    @Column(name = "primary_contact", columnDefinition = "INT")
    private Integer primaryContact;

    @Column(name = "relationship", columnDefinition = "INT")
    private Integer relationship;

    @Column(name = "student_id", columnDefinition = "INT")
    private Integer studentId;


    public Contact(String createdName, LocalDateTime createdTime, String fullName, String phone, Integer primaryContact, Integer relationship, Integer studentId) {
        this.createdName = createdName;
        this.createdTime = createdTime;
        this.fullName = fullName;
        this.phone = phone;
        this.primaryContact = primaryContact;
        this.relationship = relationship;
        this.studentId = studentId;
    }


    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", createdName='" + getCreatedName() + "'" +
                ", createdTime='" + getCreatedTime() + "'" +
                ", fullName='" + getFullName() + "'" +
                ", phone='" + getPhone() + "'" +
                ", primaryContact='" + getPrimaryContact() + "'" +
                ", relationship='" + getRelationship() + "'" +
                ", studentId='" + getStudentId() + "'" +
                "}";
    }


}
