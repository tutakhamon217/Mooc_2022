package fpt.capstone.form_data;

import fpt.capstone.vo.GradebookSubjectsDetailsVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GradeBookForm {
    //String subjectCode,String classCode,String schoolYear,Integer semester ,Integer pageIndex, Integer pageSize
    private Integer id;

    private String code;

    private String schoolYear;

    private String semester;

    private String classCode;

    private Integer classId;

    private String subjectCode;

    private String studentCode;

    private String teacherCode;

    private List<GradebookSubjectsDetailsVO> subjectsDetails;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GradeBookForm)) {
            return false;
        }

        return id != null && id.equals(((GradeBookForm) o).id);
    }

    public GradeBookForm(String schoolYear, String semester, String classCode, String subjectCode) {
        this.schoolYear = schoolYear;
        this.semester = semester;
        this.classCode = classCode;
        this.subjectCode = subjectCode;
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GradeBookForm{" +
                "id=" + getId() +
                ", code='" + getCode() + "'" +
                ", schoolYear='" + getSchoolYear() + "'" +
                ", semester='" + getSemester() + "'" +
                ", classCode='" + getClassCode() + "'" +
                "}";
    }

}
