package fpt.capstone.vo;

public class TimeTableDropDown {
    private DropDownVo subject;
    private DropDownVo teacher;

    public TimeTableDropDown(Integer subjectID, String subjectName, Integer teacherID, String teacherName)
    {
        this.subject = new DropDownVo(subjectID, subjectName);
        this.teacher = new DropDownVo(teacherID, teacherName);
    }

    public TimeTableDropDown(Integer subjectID, String subjectName, String subjectCode, Integer teacherID, String teacherName, String teacherCode)
    {
        this.subject = new DropDownVo(subjectID, subjectName, subjectCode);
        this.teacher = new DropDownVo(teacherID, teacherName, teacherCode);
    }

    public DropDownVo getSubject() {
        return subject;
    }

    public void setSubject(DropDownVo subject) {
        this.subject = subject;
    }

    public DropDownVo getTeacher() {
        return teacher;
    }

    public void setTeacher(DropDownVo teacher) {
        this.teacher = teacher;
    }
}
