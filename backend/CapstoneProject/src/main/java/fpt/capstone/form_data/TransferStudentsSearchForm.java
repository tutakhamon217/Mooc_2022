package fpt.capstone.form_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransferStudentsSearchForm {
    private String schoolYear;

    private Integer gradeLevel;

    private String classCode;

    private Integer transferStatus;

    private String studentSearch;
}
