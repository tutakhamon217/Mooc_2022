package fpt.capstone.form_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransferStudentsForm {
    private Integer id;

    private String code;

    private String schoolYear;

    private Integer gradeLevel;

    private String gradeCode;

    private String classCode;

    private String className;

    private String competitionTitle;

    private TransferStudentsDetailsForm details;

    private AssessStudentConductForm assess;

    private AssessStudentConductDetailForm assessDetails;
}
