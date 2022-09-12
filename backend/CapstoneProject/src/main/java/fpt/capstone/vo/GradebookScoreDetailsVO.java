package fpt.capstone.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GradebookScoreDetailsVO {
    private Integer id;

    private String code;

    private String parentCode;

    private Integer scoreId;
    private String scoreCode;

    private String scoreName;

    private Integer times;

    private String value;

    private Integer type;

    private Integer coeficient;

    private Integer quantity;

    //Mon tinh diem => tim kiem
    private Integer idStudent;
    private String subjectCode;
    private String years;
    private String semester;
    private Integer minimumScore;
//    private String studentName;
//    private String studentCode;

    //Mon xep loai
    private Integer typeChoose;
    private List<String> selectedValue;

    public GradebookScoreDetailsVO(String scoreCode, Integer times, String value, Integer type, Integer coeficient) {
        this.scoreCode = scoreCode;
        this.times = times;
        this.value = value;
        this.type = type;
        this.coeficient = coeficient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GradebookScoreDetailsVO)) {
            return false;
        }

        return id != null && id.equals(((GradebookScoreDetailsVO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GradebookScoreDetailsDTO{" +
                "id=" + getId() +
                ", code='" + getCode() + "'" +
                ", parentCode='" + getParentCode() + "'" +
                ", scoreCode='" + getScoreCode() + "'" +
                ", times=" + getTimes() +
                ", value='" + getValue() + "'" +
                ", type=" + getType() +
                ", coeficient=" + getCoeficient() +
                "}";
    }
}
