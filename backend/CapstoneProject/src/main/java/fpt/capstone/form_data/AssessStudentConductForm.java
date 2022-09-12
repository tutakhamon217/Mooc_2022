package fpt.capstone.form_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AssessStudentConductForm {
    private Integer id;
    private String class_code;
    private String semester;
    private String school_year;
    private String creator;
    private String create_date;
    private String update_date;
    private String updater;
    private String code;

}
