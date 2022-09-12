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
public class ScoreVO {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private Float coefficient;
    private Integer quantity;
    private Integer minimumScore;
    private List<ScoreDetailVO> scoreDetailVOList;
}
