package fpt.capstone.form_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EvaluateSubjectForm {
    private String year;
    private String classCode;
    private Integer semester;
    private List<String> listStudentCode = new ArrayList<>();
    private String evaluate;
    private String subjectCode;
}
