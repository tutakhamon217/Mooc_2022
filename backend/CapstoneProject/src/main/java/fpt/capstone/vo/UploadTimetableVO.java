package fpt.capstone.vo;

public class UploadTimetableVO {
    String weekDay;
    String lesson;
    String type;
    Integer subject;
    Integer teacher;


    public UploadTimetableVO(String weekDay, String lesson, String type, Integer subject, Integer teacher) {
        this.weekDay = weekDay;
        this.lesson = lesson;
        this.type = type;
        this.subject = subject;
        this.teacher = teacher;
    }


    public String getWeekDay() {
        return this.weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getLesson() {
        return this.lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSubject() {
        return this.subject;
    }

    public void setSubject(Integer subject) {
        this.subject = subject;
    }


    public Integer getTeacher() {
        return this.teacher;
    }

    public void setTeacher(Integer teacher) {
        this.teacher = teacher;
    }
    

}
