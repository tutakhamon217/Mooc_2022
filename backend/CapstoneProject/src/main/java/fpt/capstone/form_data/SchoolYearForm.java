package fpt.capstone.form_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SchoolYearForm {
    private String years;
    private String semesterAmount;
    private String fromDate1;
    private String toDate1;
    private String fromDate2;
    private String toDate2;

    public SchoolYearForm(String years, String semesterAmount, String fromDate1, String toDate1) {
        this.years = years;
        this.semesterAmount = semesterAmount;
        this.fromDate1 = fromDate1;
        this.toDate1 = toDate1;
    }
}
