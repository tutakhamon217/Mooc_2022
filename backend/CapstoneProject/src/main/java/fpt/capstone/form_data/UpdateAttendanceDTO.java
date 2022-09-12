package fpt.capstone.form_data;

import java.util.List;

public class UpdateAttendanceDTO {
    private String classCode;
    private String year;
    private List<StudentAttendance> attendances;


    public UpdateAttendanceDTO() {
    }


    public UpdateAttendanceDTO(String classCode, String year, List<StudentAttendance> attendances) {
        this.classCode = classCode;
        this.year = year;
        this.attendances = attendances;
    }



    public String getClassCode() {
        return this.classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getYear() {
        return this.year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<StudentAttendance> getAttendances() {
        return this.attendances;
    }

    public void setAttendances(List<StudentAttendance> attendances) {
        this.attendances = attendances;
    }
}
