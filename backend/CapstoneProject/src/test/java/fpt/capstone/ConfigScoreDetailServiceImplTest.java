package fpt.capstone;

import fpt.capstone.entities.ConfigScoreDetail;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.ConfigScoreDetailForm;
import fpt.capstone.repository.ConfigScoreDetailRepository;
import fpt.capstone.service.ConfigScoreDetailServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConfigScoreDetailServiceImplTest {
    @InjectMocks
    ConfigScoreDetailServiceImpl configScoreDetailService;

    @Mock
    ConfigScoreDetailRepository configScoreDetailRepository;

    ServiceResult<List<ConfigScoreDetailForm>> result;
    List<ConfigScoreDetailForm> listTest = new ArrayList<>();
    ServiceResult<Boolean> resultDelete = new ServiceResult<>();

    //case minimum_score > quantity
    @Test()
    void saveAll_1(){
        ConfigScoreDetailForm configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "1",
                5,
                3,
                5,
                null,
                "1"
        );

        listTest.add(configScoreDetailForm);
        result = configScoreDetailService.saveAll(listTest);
        assertEquals(result.getStatus().name(), "BAD_REQUEST");
    }

    //case isScored
    @Test()
    void saveAll_2(){
        List<ConfigScoreDetail> listResult = new ArrayList<>();
        listResult.add(new ConfigScoreDetail(1,"Kiểm tra 15 phút", 1,1,1,"conf_score_details2045_645817_8d7e66af-fb80-4f1f-acab-fade5dc20655","conf_score_subject_MHH11_2022815_645817"));
        listResult.add(new ConfigScoreDetail(2,"Kiểm tra 30 phút", 1,1,1,"conf_score_details2045_645817_3b25f0cf-9d75-4c65-b849-b9f4cf96eec1","conf_score_subject_MHH11_2022815_645817"));

        ConfigScoreDetailForm configScoreDetailForm = new ConfigScoreDetailForm(
                null,
                "2",
                2,
                2,
                2,
                "",
                "conf_score_subject_MHH11_2022815_645817"
        );
        listTest.add(configScoreDetailForm);
        when(configScoreDetailRepository.getAllSubjectScoreByParentCode(any())).thenReturn(listResult);
        when(configScoreDetailRepository.isScored(any())).thenReturn(1);


        result = configScoreDetailService.saveAll(listTest);
        assertEquals(result.getStatus().name(), "BAD_REQUEST");

    }

    //case parent_code null or empty
    @Test()
    void saveAll_3(){
        ConfigScoreDetailForm configScoreDetailForm = new ConfigScoreDetailForm(
                13,
                "1",
                5,
                5,
                3,
                null,
                null
        );
        listTest.add(configScoreDetailForm);

        configScoreDetailForm = new ConfigScoreDetailForm(
                13,
                "1",
                5,
                5,
                3,
                "1",
                ""
        );
        listTest.add(configScoreDetailForm);

        result = configScoreDetailService.saveAll(listTest);
        assertEquals(result.getStatus().name(), "BAD_REQUEST");
    }

    //case quantity and minimum_score < 0
    @Test()
    void saveAll_4(){
        ConfigScoreDetailForm configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "1",
                1,
                0,
                1,
                null,
                "1"
        );

        listTest.add(configScoreDetailForm);
        configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "1",
                1,
                1,
                0,
                null,
                "1"
        );
        listTest.add(configScoreDetailForm);
        result = configScoreDetailService.saveAll(listTest);
        assertEquals(result.getStatus().name(), "BAD_REQUEST");
    }

    //case coefficient out of range [1, 10]
    @Test()
    void saveAll_5(){
        ConfigScoreDetailForm configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "1",
                15,
                1,
                1,
                null,
                "1"
        );
        listTest.add(configScoreDetailForm);
        result = configScoreDetailService.saveAll(listTest);
        assertEquals(result.getStatus().name(), "BAD_REQUEST");
    }

    //case coefficient null or quantity null or minimum_score null
    @Test()
    void saveAll_6(){
        ConfigScoreDetailForm configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "1",
                null,
                1,
                1,
                null,
                "1"
        );
        listTest.add(configScoreDetailForm);

        configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "1",
                1,
                null,
                1,
                "1",
                "1"
        );
        listTest.add(configScoreDetailForm);

        configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "1",
                1,
                1,
                null,
                "1",
                "1"
        );
        listTest.add(configScoreDetailForm);

        result = configScoreDetailService.saveAll(listTest);
        assertEquals(result.getStatus().name(), "BAD_REQUEST");
    }

    //case list empty
    @Test()
    void saveAll_7(){
        result = configScoreDetailService.saveAll(listTest);
        assertEquals(result.getMessage(), "empty");
    }

    //case ok
    @Test()
    void saveAll_8(){
        List<ConfigScoreDetail> listResult = new ArrayList<>();
        listResult.add(new ConfigScoreDetail(1,"Kiểm tra 15 phút", 1,1,1,"conf_score_details2045_645817_8d7e66af-fb80-4f1f-acab-fade5dc20655","conf_score_subject_MHH11_2022815_645817"));
        listResult.add(new ConfigScoreDetail(2,"Kiểm tra 30 phút", 1,1,1,"conf_score_details2045_645817_3b25f0cf-9d75-4c65-b849-b9f4cf96eec1","conf_score_subject_MHH11_2022815_645817"));

        ConfigScoreDetailForm configScoreDetailForm = new ConfigScoreDetailForm(
                null,
                "2",
                2,
                2,
                2,
                "",
                "conf_score_subject_MHH11_2022815_645817"
        );
        listTest.add(configScoreDetailForm);

        List<ConfigScoreDetail> listExpect = new ArrayList<>();
        ConfigScoreDetail configScoreDetail = new ConfigScoreDetail(
                1,
                "2",
                2,
                2,
                2,
                "1",
                "conf_score_subject_MHH11_2022815_645817"
        );
        listExpect.add(configScoreDetail);

        when(configScoreDetailRepository.getAllSubjectScoreByParentCode(any())).thenReturn(listResult);
        when(configScoreDetailRepository.isScored(any())).thenReturn(0);
        when(configScoreDetailRepository.saveAll(any())).thenReturn(listExpect);

        result = configScoreDetailService.saveAll(listTest);
        assertEquals(result.getData(), listExpect);

        ArgumentCaptor<List<ConfigScoreDetail>> roundArgumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(configScoreDetailRepository, times(1)).saveAll(roundArgumentCaptor.capture());
        ConfigScoreDetail temp = new ConfigScoreDetail(
                0,
                "2",
                2,
                2,
                2,
                "",
                "conf_score_subject_MHH11_2022815_645817"
        );
        List<ConfigScoreDetail> temp2 = new ArrayList<>();
        temp2.add(temp);
        assertEquals(temp2, roundArgumentCaptor.getValue());
    }

    //case quantity out of range [1, 10]
    @Test()
    void saveAll_9(){
        ConfigScoreDetailForm configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "1",
                1,
                11,
                1,
                null,
                "1"
        );
        listTest.add(configScoreDetailForm);
        result = configScoreDetailService.saveAll(listTest);
        assertEquals(result.getStatus().name(), "BAD_REQUEST");
    }

    //case minimum_score out of range [1, 10]
    @Test()
    void saveAll_10(){
        ConfigScoreDetailForm configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "1",
                1,
                1,
                15,
                null,
                "1"
        );
        listTest.add(configScoreDetailForm);
        result = configScoreDetailService.saveAll(listTest);
        assertEquals(result.getStatus().name(), "BAD_REQUEST");
    }

    //test exception
    @Test
    void saveAll_11(){
        ServiceResult resultChange;
        List<ConfigScoreDetailForm> list = new ArrayList<>();
        List<ConfigScoreDetail> listRS = new ArrayList<>();
        list.add(new ConfigScoreDetailForm(1,"1",1,1,1,"1","1"));
        when(configScoreDetailRepository.getAllSubjectScoreByParentCode(any())).thenReturn(listRS);
        when(configScoreDetailRepository.isScored(any())).thenReturn(0);
        when(configScoreDetailRepository.saveAll(null)).thenReturn(null);
        resultChange = configScoreDetailService.saveAll(list);
        assertEquals("Thêm mới thất bại", resultChange.getMessage());
    }

    //test delete
    //case empty
    @Test
    void deleteAll_1(){
        Integer[] listId = new Integer[0];
        assertEquals(configScoreDetailService.deleteAll(listId).getData(), true);
    }

    //case input is not list integer
    @Test
    void deleteAll_2(){
        assertEquals(configScoreDetailService.deleteAll(null).getData(), false);
    }

    //case isScored
    @Test
    void deleteAll_3(){
        Integer[] listInput = new Integer[2];
        listInput[0] = 1;
        listInput[1] = 2;
        ConfigScoreDetail data = new ConfigScoreDetail(
                1,
                "2",
                2,
                2,
                2,
                "",
                "conf_score_subject_MHH11_2022815_645817"
        );
        when(configScoreDetailRepository.findById(anyInt())).thenReturn(Optional.of(data));
        when(configScoreDetailRepository.isScored(any())).thenReturn(1);

        resultDelete = configScoreDetailService.deleteAll(listInput);
        assertEquals(resultDelete.getStatus().name(), "BAD_REQUEST");
    }

    //case not isScored, ok
    @Test
    void deleteAll_4(){
        Integer[] listInput = new Integer[2];
        listInput[0] = 1;
        listInput[1] = 2;
        ConfigScoreDetail data = new ConfigScoreDetail(
                1,
                "2",
                2,
                2,
                2,
                "",
                "conf_score_subject_MHH11_2022815_645817"
        );
        when(configScoreDetailRepository.findById(anyInt())).thenReturn(Optional.of(data));
        when(configScoreDetailRepository.isScored(any())).thenReturn(0);

        resultDelete = configScoreDetailService.deleteAll(listInput);
        assertEquals(resultDelete.getStatus().name(), "OK");

        ArgumentCaptor<List<ConfigScoreDetail>> roundArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(configScoreDetailRepository, times(1)).deleteAll(roundArgumentCaptor.capture());
        List<ConfigScoreDetail> listInputForDeleteAllRepository = new ArrayList<>();
        data = new ConfigScoreDetail(
                1,
                "2",
                2,
                2,
                2,
                "",
                "conf_score_subject_MHH11_2022815_645817"
        );
        listInputForDeleteAllRepository.add(data);
        listInputForDeleteAllRepository.add(data);
        assertEquals(listInputForDeleteAllRepository, roundArgumentCaptor.getValue());
    }

    //test updateChange
    //case minimum_score > quantity
    @Test()
    void updateChange_1(){
        ConfigScoreDetailForm configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "1",
                5,
                3,
                5,
                null,
                "1"
        );

        listTest.add(configScoreDetailForm);
        ServiceResult resultChange;
        resultChange = configScoreDetailService.updateChange(listTest);
        assertEquals(resultChange.getStatus().name(), "BAD_REQUEST");
    }

    //case isScored
    @Test()
    void updateChange_2(){
        List<ConfigScoreDetail> listResult = new ArrayList<>();
        listResult.add(new ConfigScoreDetail(1,"Kiểm tra 15 phút", 1,1,1,"2-fb80-4f1f-acab-fade5dc20655","conf_score_subject_MHH11_2022815_645817"));

        ConfigScoreDetailForm configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "1",
                1,
                1,
                1,
                "1",
                "2"
        );
        listTest.add(configScoreDetailForm);
        when(configScoreDetailRepository.isScored(any())).thenReturn(1);

        ServiceResult resultChange;
        resultChange = configScoreDetailService.updateChange(listTest);
        assertEquals(resultChange.getStatus().name(), "BAD_REQUEST");
    }

    //case parent_code null or empty
    @Test()
    void updateChange_3(){
        ConfigScoreDetailForm configScoreDetailForm = new ConfigScoreDetailForm(
                13,
                "1",
                5,
                5,
                3,
                null,
                null
        );
        listTest.add(configScoreDetailForm);
        ServiceResult resultChange;
        resultChange = configScoreDetailService.updateChange(listTest);
        assertEquals(resultChange.getStatus().name(), "BAD_REQUEST");

        listTest = new ArrayList<>();
        configScoreDetailForm = new ConfigScoreDetailForm(
                13,
                "1",
                5,
                5,
                3,
                "1",
                ""
        );
        listTest.add(configScoreDetailForm);
        resultChange = configScoreDetailService.updateChange(listTest);
        assertEquals(resultChange.getStatus().name(), "BAD_REQUEST");
    }

    //case quantity and minimum_score < 0
    @Test()
    void updateChange_4(){
        ConfigScoreDetailForm configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "1",
                1,
                0,
                1,
                null,
                "1"
        );

        listTest.add(configScoreDetailForm);
        configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "1",
                1,
                1,
                0,
                null,
                "1"
        );
        ServiceResult resultChange;
        resultChange = configScoreDetailService.updateChange(listTest);
        assertEquals(resultChange.getStatus().name(), "BAD_REQUEST");
    }

    //case coefficient out of range [1, 10]
    @Test()
    void updateChange_5(){
        ConfigScoreDetailForm configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "1",
                15,
                1,
                1,
                "",
                "1"
        );
        listTest.add(configScoreDetailForm);
        ServiceResult resultChange;
        resultChange = configScoreDetailService.updateChange(listTest);
        assertEquals(resultChange.getStatus().name(), "BAD_REQUEST");
    }

    //case coefficient null or quantity null or minimum_score null
    @Test()
    void updateChange_6(){
        ConfigScoreDetailForm configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "1",
                null,
                1,
                1,
                null,
                "1"
        );
        listTest.add(configScoreDetailForm);

        configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "1",
                1,
                null,
                1,
                "1",
                "1"
        );
        listTest.add(configScoreDetailForm);

        configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "1",
                1,
                1,
                null,
                "1",
                "1"
        );
        listTest.add(configScoreDetailForm);

        ServiceResult resultChange;
        resultChange = configScoreDetailService.updateChange(listTest);
        assertEquals(resultChange.getStatus().name(), "BAD_REQUEST");
    }

    //case list empty
    @Test()
    void updateChange_7(){
        ServiceResult resultChange;
        resultChange = configScoreDetailService.updateChange(listTest);
        assertEquals(resultChange.getMessage(), "empty");
    }

    //case ok
    @Test()
    void updateChange_8(){
        List<ConfigScoreDetail> listResult = new ArrayList<>();
        listResult.add(new ConfigScoreDetail(1,"Kiểm tra 15 phút", 1,1,1,"conf_score_details2045_645817_8d7e66af-fb80-4f1f-acab-fade5dc20655","conf_score_subject_MHH11_2022815_645817"));
        listResult.add(new ConfigScoreDetail(2,"Kiểm tra 30 phút", 1,1,1,"conf_score_details2045_645817_3b25f0cf-9d75-4c65-b849-b9f4cf96eec1","conf_score_subject_MHH11_2022815_645817"));

        ConfigScoreDetailForm configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "2",
                2,
                2,
                2,
                "1",
                "conf_score_subject_MHH11_2022815_645817"
        );
        listTest.add(configScoreDetailForm);

        List<ConfigScoreDetail> listExpect = new ArrayList<>();
        ConfigScoreDetail configScoreDetail = new ConfigScoreDetail(
                1,
                "2",
                2,
                2,
                2,
                "1",
                "conf_score_subject_MHH11_2022815_645817"
        );
        listExpect.add(configScoreDetail);

        ConfigScoreDetail data = new ConfigScoreDetail(
                1,
                "2",
                2,
                2,
                2,
                "1",
                "conf_score_subject_MHH11_2022815_645817"
        );
        when(configScoreDetailRepository.isScored(any())).thenReturn(0);
        when(configScoreDetailRepository.findById(anyInt())).thenReturn(Optional.of(data));
        when(configScoreDetailRepository.saveAll(any())).thenReturn(listExpect);

        ServiceResult resultUpdate;
        resultUpdate = configScoreDetailService.updateChange(listTest);
        assertEquals(resultUpdate.getData(), true);

        ArgumentCaptor<List<ConfigScoreDetail>> roundArgumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(configScoreDetailRepository, times(1)).saveAll(roundArgumentCaptor.capture());
        ConfigScoreDetail temp = new ConfigScoreDetail(
                1,
                "2",
                2,
                2,
                2,
                "",
                "conf_score_subject_MHH11_2022815_645817"
        );
        List<ConfigScoreDetail> temp2 = new ArrayList<>();
        temp2.add(temp);
        assertEquals(temp2, roundArgumentCaptor.getValue());
    }

    //case quantity out of range [1, 10]
    @Test()
    void updateChange_9(){
        ConfigScoreDetailForm configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "1",
                1,
                11,
                1,
                null,
                "1"
        );
        listTest.add(configScoreDetailForm);
        ServiceResult resultChange;
        resultChange = configScoreDetailService.updateChange(listTest);
        assertEquals(resultChange.getStatus().name(), "BAD_REQUEST");
    }

    //case minimum_score out of range [1, 10]
    @Test()
    void updateChange_10(){
        ConfigScoreDetailForm configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "1",
                1,
                1,
                15,
                null,
                "1"
        );
        listTest.add(configScoreDetailForm);
        ServiceResult resultChange;
        resultChange = configScoreDetailService.updateChange(listTest);
        assertEquals(resultChange.getStatus().name(), "BAD_REQUEST");
    }

    //case update but not exist id before
    @Test
    void updateChange_11(){
        ConfigScoreDetailForm configScoreDetailForm = new ConfigScoreDetailForm(
                null,
                "1",
                1,
                1,
                1,
                "1",
                "1"
        );
        listTest.add(configScoreDetailForm);
        ServiceResult resultChange;
        resultChange = configScoreDetailService.updateChange(listTest);
        assertEquals(resultChange.getStatus().name(), "BAD_REQUEST");
    }

    //case not exist code
    @Test
    void updateChange_12(){
        ConfigScoreDetailForm configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "1",
                1,
                1,
                1,
                null,
                "1"
        );
        listTest.add(configScoreDetailForm);
        ServiceResult resultChange;
        resultChange = configScoreDetailService.updateChange(listTest);
        assertEquals(resultChange.getStatus().name(), "BAD_REQUEST");

        configScoreDetailForm = new ConfigScoreDetailForm(
                1,
                "1",
                1,
                1,
                1,
                "",
                "1"
        );
        listTest = new ArrayList<>();
        listTest.add(configScoreDetailForm);
        resultChange = configScoreDetailService.updateChange(listTest);
        assertEquals(resultChange.getStatus().name(), "BAD_REQUEST");
    }

    //test exception
    @Test
    void updateChange_13(){
        ServiceResult resultChange;
        List<ConfigScoreDetailForm> list = new ArrayList<>();
        list.add(new ConfigScoreDetailForm(1,"1",1,1,1,"1","1"));
        when(configScoreDetailRepository.isScored(any())).thenReturn(0);
        when(configScoreDetailRepository.findById(anyInt())).thenReturn(Optional.of(new ConfigScoreDetail("1",1,1,1,"1","1")));
        when(configScoreDetailRepository.saveAll(null)).thenReturn(null);
        resultChange = configScoreDetailService.updateChange(list);
        assertEquals("Cập nhật thất bại", resultChange.getMessage());
    }
}
