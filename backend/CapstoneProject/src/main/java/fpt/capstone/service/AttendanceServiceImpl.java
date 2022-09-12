package fpt.capstone.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fpt.capstone.entities.AttendanceStudent;
import fpt.capstone.entities.AttendanceStudentDetail;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.entities.Student;
import fpt.capstone.form_data.UpdateAttendanceDTO;
import fpt.capstone.payroll.ValidationException;
import fpt.capstone.repository.ApParamRepository;
import fpt.capstone.repository.AttendanceStudentDetailRepository;
import fpt.capstone.repository.AttendanceStudentRepository;
import fpt.capstone.repository.SchoolYearRespository;
import fpt.capstone.repository.StudentRepository;
import fpt.capstone.vo.StudentAttendanceVo;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    AttendanceStudentDetailRepository detailRepository;

    @Autowired
    AttendanceStudentRepository attendanceStudentRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    SchoolYearRespository schoolYearRespository;

    @Autowired
    ApParamRepository apParamRepository;

    @Override
    public ServiceResult<List<AttendanceStudentDetail>> getAttendance(String month, String year, String classCode) {
        return new ServiceResult<>(HttpStatus.OK, "attendance", detailRepository.getAttendance(month, year, classCode));
    }

    @Override
    public ServiceResult<Boolean> updateAttendace(UpdateAttendanceDTO form) {
        form.getAttendances().stream().forEach((a) -> {

            Optional<AttendanceStudentDetail> detail = detailRepository.getAttendanceByDate(a.getCode(),
                    LocalDate.parse(a.getDate()));
            if (detail.isPresent()) {
                if (!detail.get().getCheckDate().equals(a.getCheckDate())) {
                    detailRepository.updateAttendace(detail.get().getId(), a.getCheckDate());
                }

                return;
            }
            Student student = studentRepository.getByCode(a.getCode())
                    .orElseThrow(() -> new ValidationException("code", "Code không tồn tại"));

            Date attendanceDate = null;
            try {
                attendanceDate = (new SimpleDateFormat("yyyy-MM-dd")).parse(a.getDate());
            } catch (ParseException e) {
                throw new ValidationException("date", "ngày điểm danh không hợp lệ");
            }
            Integer month = attendanceDate.getMonth() + 1;

            String semester = schoolYearRespository
                    .getSemester(attendanceDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                    .orElseThrow(() -> new ValidationException("semester", "Kì học không tồn tại"));
            Optional<String> parentCodeOptional = attendanceStudentRepository.getCode(form.getYear(), semester,
                    Integer.toString(month));
            String parentCode = null;
            if (parentCodeOptional.isPresent()) {
                parentCode = parentCodeOptional.get();
            } else {
                parentCode = "AS_" + Long.toString((new Date()).getTime());
                List<AttendanceStudent> temp = new ArrayList<>();
                temp.add(new AttendanceStudent(1000, parentCode, form.getYear(), semester, String.valueOf(month)));
                attendanceStudentRepository.saveAll(temp);
            }
            String holiday = apParamRepository.getHoliday().orElse("");
            if (holiday.indexOf(a.getDate()) != -1) {
                throw new ValidationException("date", "Không được điểm danh ngày nghỉ");
            }
            Calendar c = Calendar.getInstance();
            c.setTime(attendanceDate);
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                throw new ValidationException("date", "Không được điểm danh ngày nghỉ");
            }

            List<AttendanceStudentDetail> temp = new ArrayList<>();
            temp.add(new AttendanceStudentDetail(100, a.getCode(), student.getFullName(), a.getCheckDate(), parentCode,
                    attendanceDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
            detailRepository.saveAll(temp);
        });
        return new ServiceResult<>(HttpStatus.OK, "updated", true);
    }

    @Override
    public ServiceResult<List<StudentAttendanceVo>> getStudentAttendance(String year, String month,
            String studentCode) {

        List<StudentAttendanceVo> results = new ArrayList();

        for(java.util.Map<String, Object> result: detailRepository.getStudentAttendance(year, month, studentCode))
        {
            java.sql.Date date = (java.sql.Date) (result.get("AttendanceDate"));
            String checkDate = (String) (result.get("checkDate"));
            results.add(new StudentAttendanceVo(checkDate, date.toLocalDate()));
        }

        return new ServiceResult<>(HttpStatus.OK, "Success", results);
    }
}
