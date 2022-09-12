package fpt.capstone.entities;

import antlr.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id", columnDefinition = "INT")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "login", columnDefinition = "VARCHAR(50)", unique = true)
    private String login;

    @Column(name = "password_hash", columnDefinition = "VARCHAR(60)")
    private String passwordHash;

    @Column(name = "full_name", columnDefinition = "VARCHAR(250)")
    private String fullName;

    @Column(name = "phone_number", columnDefinition = "VARCHAR(20)")
    private String phoneNumber;

    @Column(name = "image_url", columnDefinition = "VARCHAR(250)")
    private String imageUrl;

    @Column(name = "reset_key", columnDefinition = "VARCHAR(20)")
    private String resetKey;

    @Column(name = "created_by", columnDefinition = "VARCHAR(50)")
    private String createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "reset_date")
    private LocalDateTime resetDate;

    @Column(name = "last_modified_by", columnDefinition = "VARCHAR(50)")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column(name = "email", columnDefinition = "VARCHAR(255)")
    private String email;

    @Column(name = "expired_date")
    private Date expiredDate;
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "User_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_code", referencedColumnName = "code")}
    )
    private Set<Authority> authorities = new HashSet<>();

    public User(String login, String passwordHash, Set<Authority> authorities) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.authorities = authorities;
    }

    public User(String login, String passwordHash, String fullName, String phoneNumber, String imageUrl, String createdBy, LocalDateTime createdDate, String email, Set<Authority> authorities) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.email = email;
        this.authorities = authorities;
    }

    public User(int id, String login, String fullName, String phoneNumber, String imageUrl, String lastModifiedBy, LocalDateTime lastModifiedDate, String email, Set<Authority> authorities) {
        this.id = id;
        this.login = login;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.email = email;
        this.authorities = authorities;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", resetKey='" + resetKey + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdDate=" + createdDate +
                ", resetDate=" + resetDate +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", lastModifiedDate=" + lastModifiedDate +
                ", email='" + email + '\'' +
                ", expiredDate=" + expiredDate +
                ", authorities=" + authorities +
                '}';
    }
}
