package fpt.capstone.utility;

import java.time.LocalDate;
import java.util.*;
import fpt.capstone.vo.TimeTableVoV2;

public class TimetableUtility {
    public static List<TimeTableVoV2> generateEmptyTimetable()
    {
        List<TimeTableVoV2> timetable = new ArrayList<>();

        for(int day = 2; day <= 7; ++day)
        {
            for(int period = 0; period <= 1; ++period)
            {
                for(int lession = 1; lession <= 5; ++lession)
                {
                    TimeTableVoV2 t = new TimeTableVoV2(LocalDate.now(), "t" + String.valueOf(day), String.valueOf(lession), String.valueOf(period), null, null, null, null, null, null);
                    timetable.add(t);
                }
            }
        }

        return timetable;
    }
}
