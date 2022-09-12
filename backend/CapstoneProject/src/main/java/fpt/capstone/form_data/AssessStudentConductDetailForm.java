package fpt.capstone.form_data;

public class AssessStudentConductDetailForm {
    private Integer id;
    private String student_code;
    private Integer number_off;
    private Integer number_off_allowed;
    private String academic_ability;
    private String conduct;
    private String competition_title;
    private String parent_code;

    public AssessStudentConductDetailForm() {
    }

    public AssessStudentConductDetailForm(Integer id, String student_code, Integer number_off, Integer number_off_allowed, String academic_ability, String conduct, String competition_title, String parent_code) {
        this.id = id;
        this.student_code = student_code;
        this.number_off = number_off;
        this.number_off_allowed = number_off_allowed;
        this.academic_ability = academic_ability;
        this.conduct = conduct;
        this.competition_title = competition_title;
        this.parent_code = parent_code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudent_code() {
        return student_code;
    }

    public void setStudent_code(String student_code) {
        this.student_code = student_code;
    }

    public Integer getNumber_off() {
        return number_off;
    }

    public void setNumber_off(Integer number_off) {
        this.number_off = number_off;
    }

    public Integer getNumber_off_allowed() {
        return number_off_allowed;
    }

    public void setNumber_off_allowed(Integer number_off_allowed) {
        this.number_off_allowed = number_off_allowed;
    }

    public String getAcademic_ability() {
        return academic_ability;
    }

    public void setAcademic_ability(String academic_ability) {
        this.academic_ability = academic_ability;
    }

    public String getConduct() {
        return conduct;
    }

    public void setConduct(String conduct) {
        this.conduct = conduct;
    }

    public String getCompetition_title() {
        return competition_title;
    }

    public void setCompetition_title(String competition_title) {
        this.competition_title = competition_title;
    }

    public String getParent_code() {
        return parent_code;
    }

    public void setParent_code(String parent_code) {
        this.parent_code = parent_code;
    }
}
