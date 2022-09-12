package fpt.capstone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import fpt.capstone.entities.SchoolYear;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.SchoolYearForm;
import fpt.capstone.payroll.ValidationException;
import fpt.capstone.repository.ScheduleTimetableRepository;
import fpt.capstone.repository.SchoolYearRespository;
import fpt.capstone.service.SchoolYearServiceImpl;

@ExtendWith(MockitoExtension.class)
public class SchoolYearServiceImplTest {

    @InjectMocks
    SchoolYearServiceImpl service;

    @Mock
    SchoolYearRespository respository;

    @Mock
    ScheduleTimetableRepository scheduleTimetableRepository;

    @Test()
    void getSemesterAmountTestWithNull() {
        Map<String, Object> result = service.getSemesterAmount(null);

        Map<String, Object> expected = new HashMap<>();
        expected.put("semesterAmount", 0);

        assertEquals(expected, result);
    }

    @Test()
    void getSemesterAmountTest() {
        when(respository.getSemesterAmount("2022-2023")).thenReturn(2);
        Map<String, Object> result = service.getSemesterAmount("2022-2023");

        Map<String, Object> expected = new HashMap<>();
        expected.put("semesterAmount", 2);

        assertEquals(expected, result);
    }

    @Test()
    void getAllSchoolYear() {
        List<String> result = new ArrayList<>();
        result.add("2021-2022");
        result.add("2022-2023");
        result.add("2023-2024");
        when(respository.getAllSchoolYear()).thenReturn(result);
        ServiceResult<List<String>> actual = service.getAllSchoolYears();
        assertEquals("OK", actual.getStatus().name());
        assertEquals("All school years", actual.getMessage());
        assertEquals(result, actual.getData());

    }

    @Test()
    void getAllSchoolYearButNull() {
        
        when(respository.getAllSchoolYear()).thenReturn(null);
        ServiceResult<List<String>> actual = service.getAllSchoolYears();
        assertEquals("OK", actual.getStatus().name());
        assertEquals("All school years", actual.getMessage());
        assertEquals(null, actual.getData());

    }

    @Test()
    void getNullSchoolYear() {
        ServiceResult<SchoolYear> actual = service.getSchoolYear(null, null);
        assertEquals("OK", actual.getStatus().name());
        assertEquals("school year", actual.getMessage());
        assertEquals(null, actual.getData());
    }

    @Test()
    void getSchoolYear() {
        SchoolYear expected = new SchoolYear(
                "2021-2022",
                2,
                "2",
                LocalDateTime.parse("2022-05-06T00:00:00", DateTimeFormatter.ISO_DATE_TIME),
                LocalDateTime.parse("2022-12-28T00:00:00", DateTimeFormatter.ISO_DATE_TIME),
                LocalDateTime.parse("2022-08-02T20:49:13", DateTimeFormatter.ISO_DATE_TIME),
                "admin",
                null,
                null);
        when(respository.getSchoolYearOptional("2021-2022", "2")).thenReturn(Optional.of(expected));
        ServiceResult<SchoolYear> actual = service.getSchoolYear("2021-2022", "2");
        assertEquals("OK", actual.getStatus().name());
        assertEquals("school year", actual.getMessage());
        assertEquals(expected, actual.getData());
    }

    @Test()
    void getSchoolYearReturnNull() {
        when(respository.getSchoolYearOptional("2021-2022", "2")).thenReturn(Optional.empty());
        ServiceResult<SchoolYear> actual = service.getSchoolYear("2021-2022", "2");
        assertEquals("OK", actual.getStatus().name());
        assertEquals("school year", actual.getMessage());
        assertEquals(null, actual.getData());
    }

    @Test()
    void getNullSemesterOf() {
        ValidationException thrown = assertThrows(ValidationException.class, () -> service.getSchoolYearOf(null));
        assertTrue(thrown.getName().equals("date"));
        assertTrue(thrown.getValidateMessage().equals("ngày điểm danh không hợp lệ"));
    }

    @Test()
    void getWrongDateFormatSemesterOf() {
        ValidationException thrown = assertThrows(ValidationException.class,
                () -> service.getSchoolYearOf("capstone2022"));
        assertTrue(thrown.getName().equals("date"));
        assertTrue(thrown.getValidateMessage().equals("ngày điểm danh không hợp lệ"));
    }

    @Test()
    void getSemesterOfReturnNull() {
        when(respository.getSemesterOf(any())).thenReturn(Optional.empty());
        ServiceResult<SchoolYear> actual = service.getSchoolYearOf("2022-08-05");
        assertEquals("OK", actual.getStatus().name());
        assertEquals("school year", actual.getMessage());
        assertEquals(null, actual.getData());
    }

    @Test()
    void getSemesterOf() {
        SchoolYear expected = new SchoolYear(
                "2021-2022",
                2,
                "2",
                LocalDateTime.parse("2022-05-06T00:00:00", DateTimeFormatter.ISO_DATE_TIME),
                LocalDateTime.parse("2022-12-28T00:00:00", DateTimeFormatter.ISO_DATE_TIME),
                LocalDateTime.parse("2022-08-02T20:49:13", DateTimeFormatter.ISO_DATE_TIME),
                "admin",
                null,
                null);
                when(respository.getSemesterOf(any())).thenReturn(Optional.of(expected));
        ServiceResult<SchoolYear> actual = service.getSchoolYearOf("2022-08-05");
        assertEquals("OK", actual.getStatus().name());
        assertEquals("school year", actual.getMessage());
        assertEquals(expected, actual.getData());
    }

}
