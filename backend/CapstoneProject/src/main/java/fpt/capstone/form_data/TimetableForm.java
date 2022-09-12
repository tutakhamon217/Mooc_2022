package fpt.capstone.form_data;

import java.util.ArrayList;
import fpt.capstone.vo.TimeTableVo;
import fpt.capstone.vo.UploadTimetableVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TimetableForm {
    private String year;
    private String semester;
    private String className;
    private String gradeLevel;
    private String applyDate;
    private List<UploadTimetableVO> timetable;

}
