package fpt.capstone.vo;

public class TimetableExcelElement {
    String code;
    String name;


    public TimetableExcelElement(String code, String name) {
        this.code = code;
        this.name = name;
    }


    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}