package fpt.capstone.form_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeacherForm {
    private String image;
    private String code;
    private String fullName;
    private Integer status;
    private Integer deptId;
    private Set<String> authorities;
    private String startDate;
    private Integer contractType;
    private String phone;
    private String email;
    private String birthDay;
    private String religion;
    private String nation;
    private String homeTown;
    private String permanentAddress;
    private String temporaryAddress;
    private String socialInsuranceNumber;//10 so
    private String identityCard;// 12 so
    private String issuedAddress;
    private String issuedDate;
    private Integer marriageStatus;
    private Integer sex;


}
