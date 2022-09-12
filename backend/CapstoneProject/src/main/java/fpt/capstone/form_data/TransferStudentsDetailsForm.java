package fpt.capstone.form_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransferStudentsDetailsForm {
    private Integer id;

    private String code;

    private String currentSchoolYear;

    private String currentClassCode;

    private String studentCode;

    private String studentName;

    private String academicAbility;

    private String conduct;

    private Integer status;

    private String newClassCode;

    private String newClassName;

    private Integer newGradeLevel;

    private String newSchoolYear;

    private String parentCode;

}
