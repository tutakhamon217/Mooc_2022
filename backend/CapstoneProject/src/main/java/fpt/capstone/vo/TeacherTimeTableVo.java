package fpt.capstone.vo;

import java.util.ArrayList;


public class TeacherTimeTableVo {
    String dayOfWeek;
    String type;
    String lesson;
    ArrayList<TeacherTimetable> subjects;


    public TeacherTimeTableVo(String dayOfWeek, String type, String lesson) {
        this.dayOfWeek = dayOfWeek;
        this.type = type;
        this.lesson = lesson;
        this.subjects = new ArrayList<>();
    }


    public String getDayOfWeek() {
        return this.dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLesson() {
        return this.lesson;
    }

    public void setLesson(String lesson) {
        this.lesson = lesson;
    }


    public ArrayList<TeacherTimetable> getSubjects() {
        return this.subjects;
    }

    public void setSubjects(ArrayList<TeacherTimetable> subjects) {
        this.subjects = subjects;
    }

}
