package fpt.capstone.form_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeachingAssignmentUpdate {
    private Integer teachingAssignmentId;
    private Integer teacherId;
    private Integer semester1;
    private Integer semester2;
    private Integer applyAllSemester;
    private String subjectCode;
    private String classCode;

}
