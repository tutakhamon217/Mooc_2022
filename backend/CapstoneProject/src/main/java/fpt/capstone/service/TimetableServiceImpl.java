package fpt.capstone.service;

import fpt.capstone.entities.ClassRoom;
import fpt.capstone.entities.GradeLevel;
import fpt.capstone.entities.ScheduleGradeClass;
import fpt.capstone.entities.ScheduleSubjectDetails;
import fpt.capstone.entities.ScheduleTimetable;
import fpt.capstone.entities.SchoolYear;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.entities.Subjects;
import fpt.capstone.entities.Teacher;
import fpt.capstone.entities.WeekDetails;
import fpt.capstone.form_data.TimetableForm;
import fpt.capstone.helper.TimetableExcelHelper;
import fpt.capstone.payroll.SubjectNotFoundException;
import fpt.capstone.payroll.ValidationException;
import fpt.capstone.repository.*;
import fpt.capstone.utility.TimetableUtility;
import fpt.capstone.vo.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.Null;

@Service
public class TimetableServiceImpl implements TimetableService {

    @Autowired
    private ScheduleTimetableRepository scheduleTimetableRepository;

    @Autowired
    private ScheduleGradeClassRepository scheduleGradeClassRepository;

    @Autowired
    private WeekDetailsRepository weekDetailsRepository;

    @Autowired
    private ScheduleSubjectDetailsRepository scheduleSubjectDetailsRepository;

    @Autowired
    private TeachingAssignmentRepository teachingAssignmentRepository;

    @Autowired
    private SubjectsRepository subjectsRepository;

    @Autowired
    private GradeLevelRepositpry gradeLevelRepositpry;

    @Autowired
    private ClassRoomRepository classroomRespository;

    @Autowired
    private SubjectClassRepository subjectClassRepository;

    @Autowired
    private SchoolYearRespository schoolYearRepository;

    @Override
    public ServiceResult<List<TimeTableVoV2>> getScheduleTimetableByYearAndSemester(String year, String semester,
            String gradeLevel, String className, LocalDate applyDate) {

        Optional<String> code = scheduleGradeClassRepository.getLatestScheduleGradeClassCode(applyDate, gradeLevel,
                className, semester);
        if (!code.isPresent()) {
            return new ServiceResult<List<TimeTableVoV2>>(HttpStatus.OK, "Không tìm thấy thời khóa biểu",
                    TimetableUtility.generateEmptyTimetable());
        }
        List<TimeTableVoV2> values = new ArrayList<>();
        List<Map<String, Object>> results = teachingAssignmentRepository.getLatestTimetable(code.get());

        for (Map<String, Object> result : results) {
            java.sql.Date date = (java.sql.Date) (result.get("applyDate"));
            String weekDay = (String) (result.get("weekDay"));
            String lession = (String) (result.get("lesson"));
            String type = (String) (result.get("type"));
            Integer subjectId = (Integer) (result.get("subjectID"));
            String subjectName = (String) (result.get("subjectName"));
            String subjectCode = (String) (result.get("subjectCode"));
            Integer teacherID = (Integer) (result.get("teacherID"));
            String teacherName = (String) (result.get("teacherName"));
            String teacherCode = (String) (result.get("teacherCode"));

            values.add(new TimeTableVoV2(date.toLocalDate(), weekDay, lession, type, subjectId, subjectName,
                    subjectCode, teacherID, teacherName, teacherCode));
        }

        return new ServiceResult<>(HttpStatus.OK, "Schedule timetable", values);// teachingAssignmentRepository.getLatestTimetable(code.get()));
    }

    @Override
    public ServiceResult<List<TimeTableDropDown>> getTimeTableDropdown(String year, String semester, String className) {
        return new ServiceResult<>(HttpStatus.OK, "Timetable dropdown",
                subjectClassRepository.getTimeTableDropdown(year, semester, className));
    }

    @Override
    public ServiceResult<Boolean> updateTimeTable(TimetableForm form) {
        // Validate
        for (UploadTimetableVO element : form.getTimetable()) {
            if (element.getSubject() == null) {
                continue;
            }
            Subjects assignedSubject = subjectsRepository.findById(element.getSubject())
                    .orElseThrow(() -> new SubjectNotFoundException(element.getSubject().toString()));

            if (!subjectClassRepository.isValidForTimetable(form.getYear(), form.getSemester(), form.getClassName(),
                    assignedSubject.getCode())) {
                throw new ValidationException("SubjectCode", "Không thể thêm môn học có code là "
                        + assignedSubject.getCode() + " cho lớp " + form.getClassName());
            }
        }

        LocalDate applyDate = LocalDate.parse(form.getApplyDate());

        if (applyDate.getDayOfWeek() != DayOfWeek.MONDAY) {
            throw new ValidationException("dayOfWeek", "Ngày áp dụng phải là thứ Hai");
        }

        if (!scheduleTimetableRepository.isExisted(form.getYear(), form.getSemester())) {
            ScheduleTimetable st = new ScheduleTimetable(
                    1000,
                    "timetable_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMd_Hms")),
                    form.getSemester(),
                    LocalDate.now(),
                    form.getYear(),
                    LocalDate.now(),
                    "",
                    null);

            scheduleTimetableRepository.save(st);
        }

        String schduleTimetableCode = scheduleTimetableRepository.getCodeByYearAndSemester(form.getYear(),
                form.getSemester());

        GradeLevel gl = null;
        try {
            gl = gradeLevelRepositpry.getById(Integer.parseInt(form.getGradeLevel()));
        } catch (Exception ex) {
            throw new ValidationException("gradeLevel", "Khối không tồn tại");
        }

        if (!scheduleGradeClassRepository.isExist(schduleTimetableCode, gl.getCode(), form.getClassName(), applyDate)) {
            ScheduleGradeClass sgc = new ScheduleGradeClass(1000,
                    "code_grade_class_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMd_Hms")),
                    gl.getCode(),
                    schduleTimetableCode,
                    null,
                    applyDate,
                    form.getClassName(),
                    LocalDate.now(),
                    "");

            scheduleGradeClassRepository.save(sgc);
        }

        String scheduelGradeClassCode = scheduleGradeClassRepository.getCode(schduleTimetableCode, gl.getCode(),
                form.getClassName(), applyDate);

        // Generate null timetable
        for (int weekDay = 2; weekDay <= 7; ++weekDay) {
            String weekDayString = "t" + String.valueOf(weekDay);
            for (int period = 0; period <= 1; ++period) {
                String type = period == 0 ? "morning" : "afternoon";
                if (!weekDetailsRepository.isExist(scheduelGradeClassCode, String.valueOf(period), weekDayString)) {
                    WeekDetails wd = new WeekDetails(1000,
                            "week_detail_" + type
                                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMd_Hms")),
                            String.valueOf(period),
                            weekDayString,
                            scheduelGradeClassCode);

                    weekDetailsRepository.save(wd);
                }

                String weekdetailCode = weekDetailsRepository.getCode(scheduelGradeClassCode, String.valueOf(period),
                        weekDayString);

                for (int lession = 1; lession <= 5; ++lession) {
                    if (!scheduleSubjectDetailsRepository.isExist(weekdetailCode, String.valueOf(lession))) {
                        ScheduleSubjectDetails ssd = new ScheduleSubjectDetails(
                                1000,
                                "subject_details_"
                                        + type
                                        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMd_Hms")),
                                "",
                                weekdetailCode,
                                String.valueOf(lession));

                        scheduleSubjectDetailsRepository.save(ssd);
                    } else {
                        ScheduleSubjectDetails entity = scheduleSubjectDetailsRepository
                                .getEntity(weekdetailCode, String.valueOf(lession)).orElse(null);

                        entity.setSubjectCode("");

                        scheduleSubjectDetailsRepository.save(entity);
                    }
                }
            }
        }

        // Update timetable
        for (UploadTimetableVO timetable : form.getTimetable()) {

            String weekdetailCode = weekDetailsRepository.getCode(scheduelGradeClassCode,
                    timetable.getType(), timetable.getWeekDay());
            String subjectCode = timetable.getSubject() == null ? ""
                    : subjectsRepository.findById(timetable.getSubject()).orElse(null).getCode();

            ScheduleSubjectDetails entity = scheduleSubjectDetailsRepository
                    .getEntity(weekdetailCode, timetable.getLesson()).orElse(null);

            entity.setSubjectCode(subjectCode);

            scheduleSubjectDetailsRepository.save(entity);
        }

        return new ServiceResult<>(HttpStatus.OK, "updated", true);
    }

    @Override
    public ByteArrayInputStream exportToExcel(String year, String semester, String gradeLevel, String className,
            LocalDate applyDate) {
        ByteArrayInputStream in = TimetableExcelHelper.export(year, semester, className,
                applyDate.format(DateTimeFormatter.ISO_DATE),
                this.getScheduleTimetableByYearAndSemester(year, semester, gradeLevel, className, applyDate).getData());
        return in;
    }

    public ByteArrayInputStream downloadTemplateFile(String years, String semester, String gradeLevel) {
        return TimetableExcelHelper.downloadTemplateFile(years, semester, gradeLevel,
                classroomRespository.getAllClassRoomByYearsAndGradeAllDept(years, Integer.parseInt(gradeLevel)));
    }

    @Override
    public ServiceResult<Boolean> upload(String year, String semester, String gradeLevel,
            String applyDate, MultipartFile file) {
        if (TimetableExcelHelper.isExcelFormat(file)) {
            try {
                HashMap<String, TimetableForm> timetableExcels = TimetableExcelHelper.parseFromExcel(
                        file.getInputStream(), year, semester, Integer.parseInt(gradeLevel), applyDate,
                        classroomRespository, subjectClassRepository, subjectsRepository);

                for (Map.Entry<String, TimetableForm> entry : timetableExcels.entrySet()) {
                    this.updateTimeTable(entry.getValue());
                }

                return new ServiceResult<>(HttpStatus.OK, "updated", true);
            } catch (IOException e) {
                return new ServiceResult<Boolean>(HttpStatus.BAD_REQUEST, "Không thể đọc file", false);
            }
        }

        return new ServiceResult<Boolean>(HttpStatus.BAD_REQUEST, "Sai định dạng file", false);
    }

    @Override
    public ServiceResult<List<List<TeacherTimeTableVo>>> getTimetableForTeacher(String applyDate, String teacherCode) {
        List<List<TeacherTimeTableVo>> timetable = new ArrayList();
        for (int dayOfWeek = 2; dayOfWeek <= 7; ++dayOfWeek) {
            timetable.add(new ArrayList<>());
            for (int lession = 0; lession < 10; ++lession) {
                timetable.get(dayOfWeek - 2).add(new TeacherTimeTableVo("t" + String.valueOf(dayOfWeek),
                        lession < 5 ? "0" : "1", String.valueOf(lession % 5 + 1)));
            }
        }

        SchoolYear schoolYear = schoolYearRepository
                .getSemesterOf(LocalDate.parse(applyDate, DateTimeFormatter.ISO_DATE).atStartOfDay()).orElse(null);

        if (schoolYear == null) {
            return new ServiceResult<>(HttpStatus.OK, "Success", timetable);
        }

        List<Map<String, Object>> assignedClasses = teachingAssignmentRepository
                .getTeachingClassCodes(schoolYear.getYears(), schoolYear.getSemester(), teacherCode);

        for (Map<String, Object> assignedClass : assignedClasses) {
            // System.out.printf("%s %s\n", assignedClass.get("class_code"),
            // assignedClass.get("grade_level"));
            ClassRoom classroom = classroomRespository.getClassRoomByCode(assignedClass.get("class_code").toString())
                    .orElse(null);

            Optional<String> code = scheduleGradeClassRepository.getLatestScheduleGradeClassCode(
                    LocalDate.parse(applyDate, DateTimeFormatter.ISO_DATE), assignedClass.get("grade_level").toString(),
                    assignedClass.get("class_code").toString(), schoolYear.getSemester());

            if (!code.isPresent())
                continue;

            List<Map<String, Object>> results = teachingAssignmentRepository.getLatestTimetable(code.get());

            for (Map<String, Object> result : results) {
                java.sql.Date date = (java.sql.Date) (result.get("applyDate"));
                String weekDay = (String) (result.get("weekDay"));
                String lession = (String) (result.get("lesson"));
                String type = (String) (result.get("type"));
                Integer subjectId = (Integer) (result.get("subjectID"));
                String subjectName = (String) (result.get("subjectName"));
                String subjectCode = (String) (result.get("subjectCode"));
                Integer teacherID = (Integer) (result.get("teacherID"));
                String teacherName = (String) (result.get("teacherName"));
                String teacherCodeTimtable = (String) (result.get("teacherCode"));

                if (teacherCodeTimtable == null) {
                    continue;
                }

                if (teacherCodeTimtable.equals(teacherCode)) {
                    // System.out.printf("%s %s %s %s %s\n", weekDay, type, lession, subjectCode,
                    // teacherCodeTimtable);
                    Integer weekDayIdx = Integer.parseInt(weekDay.substring(1)) - 2;
                    Integer lessionIdx = Integer.parseInt(lession) - 1 + Integer.parseInt(type) * 5;
                    System.out.printf("%s %s %s %s %s %s\n", weekDayIdx, lessionIdx, subjectCode,
                    teacherCodeTimtable, classroom.getCode(), classroom.getName());
                    // timetable.get(weekDayIdx).get(lessionIdx).setSubjectCode(subjectCode);
                    // timetable.get(weekDayIdx).get(lessionIdx).setSubjectName(subjectName);
                    // timetable.get(weekDayIdx).get(lessionIdx).setClassCode(classroom.getCode());
                    // timetable.get(weekDayIdx).get(lessionIdx).setClassName(classroom.getName());
                    timetable.get(weekDayIdx).get(lessionIdx).getSubjects().add(new TeacherTimetable(subjectCode, subjectName, classroom.getCode(), classroom.getName()));
                }

            }
            // System.out.print(code.get());

        }

        return new ServiceResult<>(HttpStatus.OK, "Success", timetable);
    }
}
