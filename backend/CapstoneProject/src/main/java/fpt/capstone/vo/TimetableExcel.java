package fpt.capstone.vo;



public class TimetableExcel {
    String weekDay;
    String lesson;

    String type;

    TimetableExcelElement subject;
    TimetableExcelElement teacher;


    public TimetableExcel(String weekDay, String lesson, String type, String subjectCode, String subjectName, String teacherCode, String teacherName) {
        this.weekDay = weekDay;
        this.lesson = lesson;
        this.type = type;
        this.subject = new TimetableExcelElement(subjectCode, subjectName);
        this.teacher = new TimetableExcelElement(teacherCode, teacherName);
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

    public TimetableExcelElement getSubject() {
        return this.subject;
    }

    public void setSubject(TimetableExcelElement subject) {
        this.subject = subject;
    }

    public TimetableExcelElement getTeacher() {
        return this.teacher;
    }

    public void setTeacher(TimetableExcelElement teacher) {
        this.teacher = teacher;
    }
    
}
