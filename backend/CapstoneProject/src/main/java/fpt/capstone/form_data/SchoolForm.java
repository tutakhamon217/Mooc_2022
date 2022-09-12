package fpt.capstone.form_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SchoolForm {
    private Integer id;

    private String name;

    private String code;

    private Integer district_id;

    private Integer province_id;

    private String abbreviation_name;

    private String address;

    private String level_school;

    private String type_education;

    private Date founded_date;

    private String principal;

    private String hot_line;

    private String phone_principal;

    private String email;

    private String website;

    private String logo;

    public SchoolForm(String name, String code, Integer district_id, Integer province_id, String abbreviation_name, String address, String level_school, String type_education, Date founded_date, String principal, String hot_line, String phone_principal, String email, String website, String logo) {
        this.name = name;
        this.code = code;
        this.district_id = district_id;
        this.province_id = province_id;
        this.abbreviation_name = abbreviation_name;
        this.address = address;
        this.level_school = level_school;
        this.type_education = type_education;
        this.founded_date = founded_date;
        this.principal = principal;
        this.hot_line = hot_line;
        this.phone_principal = phone_principal;
        this.email = email;
        this.website = website;
        this.logo = logo;
    }
}
