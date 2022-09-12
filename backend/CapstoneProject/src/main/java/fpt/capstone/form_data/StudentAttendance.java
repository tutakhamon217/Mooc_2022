package fpt.capstone.form_data;

public class StudentAttendance {
    private String code;
    private String checkDate;
    private String date;


    public StudentAttendance(String code, String checkDate, String date) {
        this.code = code;
        this.checkDate = checkDate;
        this.date = date;
    }


    public StudentAttendance() {
    }


    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCheckDate() {
        return this.checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
