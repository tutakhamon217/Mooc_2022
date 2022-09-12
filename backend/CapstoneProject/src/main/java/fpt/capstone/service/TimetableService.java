package fpt.capstone.service;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.TimetableForm;
import fpt.capstone.vo.TeacherTimeTableVo;
import fpt.capstone.vo.TimeTableDropDown;
import fpt.capstone.vo.TimeTableVo;
import fpt.capstone.vo.TimeTableVoV2;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface TimetableService {
    ServiceResult<List<TimeTableVoV2>> getScheduleTimetableByYearAndSemester(String year, String semester, String gradeLevel, String className, LocalDate applyDate);
    ServiceResult<List<TimeTableDropDown>> getTimeTableDropdown(String year, String semester, String className);
    ServiceResult<Boolean> updateTimeTable(TimetableForm form);
    ByteArrayInputStream exportToExcel(String year, String semester, String gradeLevel, String className, LocalDate applyDate);
    ByteArrayInputStream downloadTemplateFile(String years, String semester, String gradeLevel);
    ServiceResult<Boolean> upload(String year, String semester, String gradeLevel, String applyDate, MultipartFile file);
    ServiceResult<List<List<TeacherTimeTableVo>>> getTimetableForTeacher(String applyDate, String teacherCode);
}

