package fpt.capstone.service;

import java.util.List;

import fpt.capstone.entities.AttendanceStudentDetail;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.UpdateAttendanceDTO;
import fpt.capstone.vo.StudentAttendanceVo;

public interface AttendanceService {
    ServiceResult<List<AttendanceStudentDetail>> getAttendance(String month, String year, String classCode);
    ServiceResult<Boolean> updateAttendace(UpdateAttendanceDTO form);
    ServiceResult<List<StudentAttendanceVo>> getStudentAttendance(String year, String month, String studentCode);
}
