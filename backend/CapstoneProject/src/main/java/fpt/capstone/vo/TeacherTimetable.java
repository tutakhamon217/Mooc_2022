package fpt.capstone.vo;

public class TeacherTimetable {
    String subjectCode;
    String subjectName;
    String classCode;
    String className;


    public TeacherTimetable(String subjectCode, String subjectName, String classCode, String className) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.classCode = classCode;
        this.className = className;
    }


    public String getSubjectCode() {
        return this.subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return this.subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getClassCode() {
        return this.classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
