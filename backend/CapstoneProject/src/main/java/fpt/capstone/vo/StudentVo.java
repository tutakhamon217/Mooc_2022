package fpt.capstone.vo;

import java.time.LocalDateTime;

public class StudentVo {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String code;
    private String full_name;
    private Integer sex;
    private LocalDateTime birth_day;
    private String class_name;
    private String department_name;
    private Integer status;


    public StudentVo(Integer id, String code, String full_name, Integer sex, LocalDateTime birth_day, String class_name, String department_name, Integer status) {
        this.id = id;
        this.code = code;
        this.full_name = full_name;
        this.sex = sex;
        this.birth_day = birth_day;
        this.class_name = class_name;
        this.department_name = department_name;
        this.status = status;
    }


    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getSex() {
        return this.sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public LocalDateTime getBirth_day() {
        return this.birth_day;
    }

    public void setBirth_day(LocalDateTime birth_day) {
        this.birth_day = birth_day;
    }

    public String getClass_name() {
        return this.class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getDepartment_name() {
        return this.department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
