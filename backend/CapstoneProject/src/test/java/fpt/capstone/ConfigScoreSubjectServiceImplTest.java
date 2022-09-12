package fpt.capstone;

import fpt.capstone.entities.ConfigScoreSubject;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.ConfigGradeDetailForm;
import fpt.capstone.repository.ConfigScoreSubjectRepository;
import fpt.capstone.service.ConfigScoreSubjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConfigScoreSubjectServiceImplTest {
    @InjectMocks
    ConfigScoreSubjectServiceImpl configScoreSubjectService;

    @Mock
    ConfigScoreSubjectRepository configScoreSubjectRepository;

    ServiceResult<String> result;

    //case subject_code null
    @Test
    void getCodeOfCSS_1(){
        String subject_code = null;
        List<ConfigScoreSubject> list = new ArrayList<>();
        when(configScoreSubjectRepository.getCodeOfCSS(subject_code)).thenReturn(list);
        result = configScoreSubjectService.getCodeOfCSS(subject_code);
        assertNotEquals(result.getStatus().name(), "OK");
    }

    //case subject_code empty
    @Test
    void getCodeOfCSS_2(){
        String subject_code = "";
        List<ConfigScoreSubject> list = new ArrayList<>();
        when(configScoreSubjectRepository.getCodeOfCSS(subject_code)).thenReturn(list);
        result = configScoreSubjectService.getCodeOfCSS(subject_code);
        assertNotEquals(result.getStatus().name(), "OK");
    }

    //case ok
    @Test
    void getCodeOfCSS_3(){
        String subject_code = "1";
        List<ConfigScoreSubject> list = new ArrayList<>();
        list.add(new ConfigScoreSubject(1, "1","1","1",1));
        when(configScoreSubjectRepository.getCodeOfCSS(subject_code)).thenReturn(list);
        result = configScoreSubjectService.getCodeOfCSS(subject_code);
        assertEquals(result.getStatus().name(), "OK");
    }

    //case subject_code does not exist in db
    @Test
    void getCodeOfCSS_4(){
        String subject_code = "1";
        List<ConfigScoreSubject> list = new ArrayList<>();
        when(configScoreSubjectRepository.getCodeOfCSS(subject_code)).thenReturn(list);
        result = configScoreSubjectService.getCodeOfCSS(subject_code);
        assertNotEquals(result.getStatus().name(), "OK");
    }
}
