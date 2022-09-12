package fpt.capstone.form_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubjectClassUpdate {
    private Integer coefficient;
    private Integer flgSemester1;
    private Integer flgSemester2;
    private Integer classId;
    private Integer subjectClassId;
    private Integer subjectId;
}
