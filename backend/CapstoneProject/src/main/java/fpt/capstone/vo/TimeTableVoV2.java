package fpt.capstone.vo;
import java.time.LocalDate;


public class TimeTableVoV2 {
    private LocalDate applyDate;
    String weekDay;
    String lesson;
    

    String type;
    DropDownVo subject;
    DropDownVo teacher;

    public TimeTableVoV2(LocalDate applyDate,String weekDay, String lesson, String type, Integer subjectID, String subjectName, String subjectCode, Integer teacherID, String teacherName, String teacherCode) {
        this.applyDate = applyDate;
        this.weekDay = weekDay;
        this.lesson = lesson;
        this.type = type;
        this.subject = new DropDownVo(subjectID, subjectName, subjectCode);
        this.teacher = new DropDownVo(teacherID, teacherName, teacherCode);
    }


    public LocalDate getApplyDate() {
        return this.applyDate;
    }

    public void setApplyDate(LocalDate applyDate) {
        this.applyDate = applyDate;
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
