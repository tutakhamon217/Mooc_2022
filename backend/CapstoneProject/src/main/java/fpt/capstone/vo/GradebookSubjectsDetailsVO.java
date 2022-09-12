package fpt.capstone.vo;

import fpt.capstone.entities.ConfigScoreDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GradebookSubjectsDetailsVO {
    private Integer id;

    private String code;

    private String studentCode;

    private String studentName;

    private String parentCode;

    private String avgScore;

    private String subjectCode;

    private String evaluate;

    private Integer studentId;

    private String classId;

    private String classCode;

//    private Integer subjectId;
//
//    private String subjectName;

    private String updater;

    private Integer status;

//    private String semester;

    //    private String schoolYear;
    private List<GradebookScoreDetailsVO> listScore;

//    private List<ConfigScoreDetail> confScoreDetailsList;

//    public String getSemester() {
//        return semester;
//    }
//
//    public void setSemester(String semester) {
//        this.semester = semester;
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
//    private List<GradebookSubjectsDetailsVO> listScore;
//
//    private List<ConfigScoreDetail> confScoreDetailsList;
//
//    public String getUpdater() {
//        return updater;
//    }
//
//    public void setUpdater(String updater) {
//        this.updater = updater;
//    }
//
//    public Integer getStatus() {
//        return status;
//    }
//
//    public void setStatus(Integer status) {
//        this.status = status;
//    }
//
//    public String getEvaluate() {
//        return evaluate;
//    }
//
//    public void setEvaluate(String evaluate) {
//        this.evaluate = evaluate;
//    }
//
//    public List<ConfigScoreDetail> getConfScoreDetailsList() {
//        return confScoreDetailsList;
//    }
//
//    public void setConfScoreDetailsList(List<ConfigScoreDetail> confScoreDetailsList) {
//        this.confScoreDetailsList = confScoreDetailsList;
//    }
//
//    public String getStudentName() {
//        return studentName;
//    }
//
//    public void setStudentName(String studentName) {
//        this.studentName = studentName;
//    }
//
//    public List<GradebookScoreDetailsDTO> getListScore() {
//        return listScore;
//    }
//
//    public void setListScore(List<GradebookScoreDetailsDTO> listScore) {
//        this.listScore = listScore;
//    }

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
//    public String getStudentCode() {
//        return studentCode;
//    }
//
//    public void setStudentCode(String studentCode) {
//        this.studentCode = studentCode;
//    }
//
//    public String getParentCode() {
//        return parentCode;
//    }
//
//    public void setParentCode(String parentCode) {
//        this.parentCode = parentCode;
//    }
//
//    public String getAvgScore() {
//        return avgScore;
//    }
//
//    public void setAvgScore(String avgScore) {
//        this.avgScore = avgScore;
//    }
//
//    public String getSubjectCode() {
//        return subjectCode;
//    }
//
//    public void setSubjectCode(String subjectCode) {
//        this.subjectCode = subjectCode;
//    }
//
//    public Long getStudentId() {
//        return studentId;
//    }
//
//    public void setStudentId(Long studentId) {
//        this.studentId = studentId;
//    }
//
//    public String getClassId() {
//        return classId;
//    }
//
//    public void setClassId(String classId) {
//        this.classId = classId;
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
//    public String getClassName() {
//        return className;
//    }
//
//    public void setClassName(String className) {
//        this.className = className;
//    }
//
//    public Long getSubjectId() {
//        return subjectId;
//    }
//
//    public void setSubjectId(Long subjectId) {
//        this.subjectId = subjectId;
//    }
//
//    public String getSubjectName() {
//        return subjectName;
//    }
//
//    public void setSubjectName(String subjectName) {
//        this.subjectName = subjectName;
//    }
//
//    public Boolean isExemption() {
//        return exemption;
//    }
//
//    public void setExemption(Boolean exemption) {
//        this.exemption = exemption;
//    }
//
//    public String getAcademicAbility() {
//        return academicAbility;
//    }
//
//    public void setAcademicAbility(String academicAbility) {
//        this.academicAbility = academicAbility;
//    }

    public GradebookSubjectsDetailsVO(Integer id, String studentCode, String studentName) {
        this.id = id;
        this.studentCode = studentCode;
        this.studentName = studentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GradebookSubjectsDetailsVO)) {
            return false;
        }

        return id != null && id.equals(((GradebookSubjectsDetailsVO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GradebookSubjectsDetailsDTO{" +
                "id=" + getId() +
                ", code='" + getCode() + "'" +
                ", studentCode='" + getStudentCode() + "'" +
                ", parentCode='" + getParentCode() + "'" +
                ", avgScore=" + getAvgScore() +
                ", subjectCode='" + getSubjectCode() + "'" +
                "}";
    }
}
