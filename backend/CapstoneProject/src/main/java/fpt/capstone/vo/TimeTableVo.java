package fpt.capstone.vo;

public class TimeTableVo {
    String weekDay;
    String lesson;
    String type;
    DropDownVo subject;
    DropDownVo teacher;

    public TimeTableVo(String weekDay, String lesson, String type, Integer subjectID, String subjectName, Integer teacherID, String teacherName) {
        this.weekDay = weekDay;
        this.lesson = lesson;
        this.type = type;
        this.subject = new DropDownVo(subjectID, subjectName);
        this.teacher = new DropDownVo(teacherID, teacherName);
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getLesson() {
        return lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
