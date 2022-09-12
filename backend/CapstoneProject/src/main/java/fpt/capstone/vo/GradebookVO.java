package fpt.capstone.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GradebookVO implements Serializable {
    private Long id;

    private String code;

    private String schoolYear;

    private String semester;

    private String classCode;

    private Long classId;

    private String subjectCode;

    private Long typeSubject;

    private String studentCode;

    private List<GradebookSubjectsDetailsVO> subjectsDetails;

    public List<GradebookSubjectsDetailsVO> getSubjectsDetails() {
        return subjectsDetails;
    }

    public void setSubjectsDetails(List<GradebookSubjectsDetailsVO> subjectsDetails) {
        this.subjectsDetails = subjectsDetails;
    }
//
//    public String getSubjectCode() {
//        return subjectCode;
//    }
//
//    public void setSubjectCode(String subjectCode) {
//        this.subjectCode = subjectCode;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public String getSchoolYear() {
//        return schoolYear;
//    }
//
//    public void setSchoolYear(String schoolYear) {
//        this.schoolYear = schoolYear;
//    }
//
//    public String getSemester() {
//        return semester;
//    }
//
//    public void setSemester(String semester) {
//        this.semester = semester;
//    }
//
//    public String getClassCode() {
//        return classCode;
//    }
//
//    public void setClassCode(String classCode) {
//        this.classCode = classCode;
//    }
//
//    public Long getClassId() {
//        return classId;
//    }
//
//    public void setClassId(Long classId) {
//        this.classId = classId;
//    }
//
//    public Long getTypeSubject() {
//        return typeSubject;
//    }
//
//    public void setTypeSubject(Long typeSubject) {
//        this.typeSubject = typeSubject;
//    }
//
//    public String getStudentCode() {
//        return studentCode;
//    }
//
//    public void setStudentCode(String studentCode) {
//        this.studentCode = studentCode;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GradebookVO)) {
            return false;
        }

        return id != null && id.equals(((GradebookVO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GradebookDTO{" +
                "id=" + getId() +
                ", code='" + getCode() + "'" +
                ", schoolYear='" + getSchoolYear() + "'" +
                ", semester='" + getSemester() + "'" +
                ", classCode='" + getClassCode() + "'" +
                "}";
    }
}
