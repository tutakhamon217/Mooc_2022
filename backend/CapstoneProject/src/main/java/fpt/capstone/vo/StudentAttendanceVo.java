package fpt.capstone.vo;

import java.time.LocalDate;

public class StudentAttendanceVo {
    private String checkDate;
    private LocalDate date;


    public StudentAttendanceVo(String checkDate, LocalDate date) {
        this.checkDate = checkDate;
        this.date = date;
    }

    public String getCheckDate() {
        return this.checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
