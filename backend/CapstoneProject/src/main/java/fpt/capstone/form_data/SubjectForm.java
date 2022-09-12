package fpt.capstone.form_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubjectForm {

    private String code;
    private String name;
    @Range(min = 1, max = 12, message = "1<gradeLevel<12")
    private int gradeLevel;
    private List<Integer> deptId;
    private int type;
    private int subType;
    private String description;

}
