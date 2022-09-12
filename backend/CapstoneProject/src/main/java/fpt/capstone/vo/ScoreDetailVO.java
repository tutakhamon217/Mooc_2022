package fpt.capstone.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ScoreDetailVO {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer times;
    private String value;
//    private String scoreCode;// link sang 1 dau diem ben  conf_score_details hoặc bảng conf_grading_details


}
