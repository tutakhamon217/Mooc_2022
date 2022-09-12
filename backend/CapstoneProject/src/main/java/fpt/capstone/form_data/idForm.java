package fpt.capstone.form_data;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



public class idForm {
    private String id = "";
    private String year= "";



    public idForm() {
    }



    public idForm(String id, String year) {
        this.id = id;
        this.year = year;
    }


    public String getYear() {
        return this.year;
    }

    public void setYear(String year) {
        this.year = year;
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
