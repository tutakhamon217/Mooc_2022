package fpt.capstone.service;

import fpt.capstone.form_data.SchoolYearForm;
import fpt.capstone.entities.SchoolYear;
import fpt.capstone.entities.ServiceResult;

import java.util.List;
import java.util.Map;

public interface SchoolYearService {
    <S extends SchoolYear> List<S> saveAll(Iterable<S> entities);

    List<SchoolYear> getSchoolYearByYears(String years);

    public void deleteSchoolYear(SchoolYear schoolYear);

    ServiceResult<Map<String, Object>> getAllSchoolYearPaging();

    ServiceResult<Map<String, Object>> saveSchoolYear(SchoolYearForm schoolYearForm);

    public ServiceResult<List<String>> getAllSchoolYears();

    Map<String, Object> getSemesterAmount(String years);

    ServiceResult<Boolean> checkStartEndSemester(String years, Integer semester);

    ServiceResult<Map<String, Object>> getCurrentSchoolyear();

    ServiceResult<SchoolYear> getSchoolYear(String year, String semester);

    ServiceResult<SchoolYear> getSchoolYearOf(String date);
    
    ServiceResult<List<String>> getSchoolYearOfHistoryStudent(String studentCode);

    ServiceResult<List<String>> getCurrentAndNextSchoolYear();

    ServiceResult<SchoolYear> getObjCurrentYear();

    ServiceResult<List<Map<String, Object>>> getRangeOfSemester(String year, Integer semester);
}
