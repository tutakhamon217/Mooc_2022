package fpt.capstone.vo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StudentDetailInformationVo {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String avatar;
    private String code;
    private String full_name;
    private Integer status;
    private Integer grade_level;
    private String department_name;
    private Integer department_id;
    private String class_name;
    private String class_code;
    private Integer training_system;
    private String phone;
    private String email;
    private String birth_day;
    private String religion;
    private String nation;
    private String home_town;
    private String permanent_address;
    private String temporary_address;
    private String social_insurance_number;
    private String identity_card;
    private String issued_address;
    private String issued_date;
    private Integer sex;
    private String start_date;
    private Integer elect_format;
    private Integer graduation_type;
    private Integer relationship;
    private String parent_name;
    private String parent_phone;
    private Integer relationship_second;
    private String parent_name_second;
    private String parent_phone_second;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public StudentDetailInformationVo(Integer id, String avatar, String code, String full_name, Integer status, Integer grade_level, Integer department_id, String department_name, String class_code, String class_name, Integer training_system, String phone, String email, LocalDateTime birth_day, String religion, String nation, String home_town, String permanent_address, String temporary_address, String social_insurance_number, String identity_card, String issued_address, LocalDateTime issued_date, Integer sex, LocalDateTime start_date, Integer elect_format, Integer graduation_type, Integer relationship, String parent_name, String parent_phone, Integer relationship_second, String parent_name_second, String parent_phone_second) {
        this.id = id;
        this.avatar = avatar;
        this.code = code;
        this.full_name = full_name;
        this.status = status;
        this.grade_level = grade_level;
        this.department_name = department_name;
        this.class_name = class_name;
        this.training_system = training_system;
        this.phone = phone;
        this.email = email;
        
        this.setBirth_day(birth_day);
        this.religion = religion;
        this.nation = nation;
        this.home_town = home_town;
        this.permanent_address = permanent_address;
        this.temporary_address = temporary_address;
        this.social_insurance_number = social_insurance_number;
        this.identity_card = identity_card;
        this.issued_address = issued_address;
        this.setIssued_date(issued_date);
        this.sex = sex;
        this.setStart_date(start_date);
        this.elect_format = elect_format;
        this.graduation_type = graduation_type;
        this.relationship = relationship;
        this.parent_name = parent_name;
        this.parent_phone = parent_phone;
        this.relationship_second = relationship_second;
        this.parent_name_second = parent_name_second;
        this.parent_phone_second = parent_phone_second;
        this.department_id = department_id;
        this.class_code  = class_code;
    }



    public Integer getRelationship_second() {
        return this.relationship_second;
    }

    public void setRelationship_second(Integer relationship_second) {
        this.relationship_second = relationship_second;
    }

    public String getParent_name_second() {
        return this.parent_name_second;
    }

    public void setParent_name_second(String parent_name_second) {
        this.parent_name_second = parent_name_second;
    }

    public String getParent_phone_second() {
        return this.parent_phone_second;
    }

    public void setParent_phone_second(String parent_phone_second) {
        this.parent_phone_second = parent_phone_second;
    }


    public Integer getDepartment_id() {
        return this.department_id;
    }

    public void setDepartment_id(Integer department_id) {
        this.department_id = department_id;
    }


    public String getClass_code() {
        return this.class_code;
    }

    public void setClass_code(String class_code) {
        this.class_code = class_code;
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return this.dateTimeFormatter;
    }

    public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFull_name() {
        return this.full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getGrade_level() {
        return this.grade_level;
    }

    public void setGrade_level(Integer grade_level) {
        this.grade_level = grade_level;
    }

    public String getDepartment_name() {
        return this.department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getClass_name() {
        return this.class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public Integer getTraining_system() {
        return this.training_system;
    }

    public void setTraining_system(Integer training_system) {
        this.training_system = training_system;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirth_day() {
        return this.birth_day;
    }

    public void setBirth_day(LocalDateTime birth_day) {
        if (birth_day == null) {
            this.birth_day = "";
        } else {
            this.birth_day = birth_day.format(dateTimeFormatter);
        }
    }

    public String getReligion() {
        return this.religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getNation() {
        return this.nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getHome_town() {
        return this.home_town;
    }

    public void setHome_town(String home_town) {
        this.home_town = home_town;
    }

    public String getPermanent_address() {
        return this.permanent_address;
    }

    public void setPermanent_address(String permanent_address) {
        this.permanent_address = permanent_address;
    }

    public String getTemporary_address() {
        return this.temporary_address;
    }

    public void setTemporary_address(String temporary_address) {
        this.temporary_address = temporary_address;
    }

    public String getSocial_insurance_number() {
        return this.social_insurance_number;
    }

    public void setSocial_insurance_number(String social_insurance_number) {
        this.social_insurance_number = social_insurance_number;
    }

    public String getIdentity_card() {
        return this.identity_card;
    }

    public void setIdentity_card(String identity_card) {
        this.identity_card = identity_card;
    }

    public String getIssued_address() {
        return this.issued_address;
    }

    public void setIssued_address(String issued_address) {
        this.issued_address = issued_address;
    }

    public String getIssued_date() {
        return this.issued_date;
    }

    public void setIssued_date(LocalDateTime issued_date) {
        if (issued_date == null) {
            this.issued_date = "";
        } else {
            this.issued_date = issued_date.format(dateTimeFormatter);
        }
    }

    public Integer getSex() {
        return this.sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getStart_date() {
        return this.start_date;
    }

    public void setStart_date(LocalDateTime start_date) {
        if (start_date == null) {
            this.start_date = "";
        } else {
            this.start_date = start_date.format(dateTimeFormatter);
        }
    }

    public Integer getElect_format() {
        return this.elect_format;
    }

    public void setElect_format(Integer elect_format) {
        this.elect_format = elect_format;
    }

    public Integer getGraduation_type() {
        return this.graduation_type;
    }

    public void setGraduation_type(Integer graduation_type) {
        this.graduation_type = graduation_type;
    }

    public Integer getRelationship() {
        return this.relationship;
    }

    public void setRelationship(Integer relationship) {
        this.relationship = relationship;
    }

    public String getParent_name() {
        return this.parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    public String getParent_phone() {
        return this.parent_phone;
    }

    public void setParent_phone(String parent_phone) {
        this.parent_phone = parent_phone;
    }


}
