package fpt.capstone;

import fpt.capstone.entities.ConfigGradeDetail;
import fpt.capstone.entities.ConfigScoreDetail;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.ConfigGradeDetailForm;
import fpt.capstone.form_data.ConfigScoreDetailForm;
import fpt.capstone.repository.ConfigGradeDetailRepository;
import fpt.capstone.service.ConfigGradeDetailServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConfigGradeDetailServiceImplTest {
    @InjectMocks
    ConfigGradeDetailServiceImpl configGradeDetailService;

    @Mock
    ConfigGradeDetailRepository configGradeDetailRepository;

    ServiceResult<List<ConfigGradeDetailForm>> result;
    List<ConfigGradeDetailForm> listTest = new ArrayList<>();
    ServiceResult<Boolean> resultDelete = new ServiceResult<>();
    ServiceResult<Boolean> resultUpdateChange = new ServiceResult<>();

    //case name null or empty
    @Test()
    void saveAll_1(){
        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                null,
                null,
                0,
                "",
                "",
                "1"
        );
        listTest.add(configGradeDetailForm);
        result = configGradeDetailService.saveAll(listTest);
        assertEquals("BAD_REQUEST", result.getStatus().name());
        listTest = new ArrayList<>();
        configGradeDetailForm = new ConfigGradeDetailForm(
                null,
                "",
                0,
                "",
                "",
                "1"
        );
        listTest.add(configGradeDetailForm);
        result = configGradeDetailService.saveAll(listTest);
        assertEquals("BAD_REQUEST", result.getStatus().name());
    }

    //case isScored
    @Test()
    void saveAll_2(){
        List<ConfigGradeDetail> listResult = new ArrayList<>();
        listResult.add(new ConfigGradeDetail(1,"Kiểm tra 15 phút", 0, null, "", "anyString"));

        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                null,
                "1",
                0,
                null,
                null,
                "1"
        );
        listTest.add(configGradeDetailForm);
        when(configGradeDetailRepository.getAllConfGradeDetailByParentCode(any())).thenReturn(listResult);
        when(configGradeDetailRepository.isScored(any())).thenReturn(1);


        result = configGradeDetailService.saveAll(listTest);
        assertEquals(result.getStatus().name(), "BAD_REQUEST");
    }

    //case parent_code null or empty
    @Test()
    void saveAll_3(){
        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                null,
                "1",
                0,
                null,
                null,
                null
        );
        listTest.add(configGradeDetailForm);
        result = configGradeDetailService.saveAll(listTest);
        assertEquals("BAD_REQUEST", result.getStatus().name());

        listTest = new ArrayList<>();
        configGradeDetailForm = new ConfigGradeDetailForm(
                null,
                "1",
                0,
                null,
                null,
                ""
        );
        listTest.add(configGradeDetailForm);
        result = configGradeDetailService.saveAll(listTest);
        assertEquals("BAD_REQUEST", result.getStatus().name());


    }

    //case type_choose null
    @Test()
    void saveAll_4(){
        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                null,
                "1",
                null,
                null,
                null,
                "1"
        );
        listTest.add(configGradeDetailForm);
        result = configGradeDetailService.saveAll(listTest);
        assertEquals("BAD_REQUEST", result.getStatus().name());
    }

    //case type_choose = 0 but exist selected_value
    @Test()
    void saveAll_5(){
        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                null,
                "1",
                0,
                "1, 2",
                null,
                "1"
        );
        listTest.add(configGradeDetailForm);
        result = configGradeDetailService.saveAll(listTest);
        assertEquals("BAD_REQUEST", result.getStatus().name());
    }

    //case type_choose = 1 but do not exist selected_value, include selected_value = null or empty
    @Test()
    void saveAll_6(){
        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                null,
                "1",
                1,
                " ",
                null,
                "1"
        );
        listTest.add(configGradeDetailForm);
        result = configGradeDetailService.saveAll(listTest);
        assertEquals("BAD_REQUEST", result.getStatus().name());

        listTest = new ArrayList<>();
        configGradeDetailForm = new ConfigGradeDetailForm(
                null,
                "1",
                1,
                null,
                null,
                "1"
        );
        listTest.add(configGradeDetailForm);
        result = configGradeDetailService.saveAll(listTest);
        assertEquals("BAD_REQUEST", result.getStatus().name());
    }


    //case type_choose out of range [0, 1]
    @Test()
    void saveAll_7(){
        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                null,
                "1",
                10,
                "1, 2",
                null,
                "1"
        );
        listTest.add(configGradeDetailForm);
        result = configGradeDetailService.saveAll(listTest);
        assertEquals("BAD_REQUEST", result.getStatus().name());
    }

    //case ok
    @Test()
    void saveAll_8(){
        List<ConfigGradeDetail> listResult = new ArrayList<>();
        listResult.add(new ConfigGradeDetail(1,"Kiểm tra 15 phút", 0, null, "", "anyString"));

        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                null,
                "1",
                1,
                "1, 2, 3",
                null,
                "1"
        );
        listTest.add(configGradeDetailForm);

        List<ConfigGradeDetail> listExpect = new ArrayList<>();
        ConfigGradeDetail configGradeDetail = new ConfigGradeDetail(
                1,
                "1",
                1,
                "1, 2, 3",
                null,
                "1"
        );
        listExpect.add(configGradeDetail);

        when(configGradeDetailRepository.getAllConfGradeDetailByParentCode(any())).thenReturn(listResult);
        when(configGradeDetailRepository.isScored(any())).thenReturn(0);
        when(configGradeDetailRepository.saveAll(any())).thenReturn(listExpect);

        result = configGradeDetailService.saveAll(listTest);
        assertEquals(result.getData(), listExpect);

        ArgumentCaptor<List<ConfigGradeDetail>> roundArgumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(configGradeDetailRepository, times(1)).saveAll(roundArgumentCaptor.capture());
        ConfigGradeDetail temp = new ConfigGradeDetail(
                1,
                "1",
                1,
                "1, 2, 3",
                null,
                "1"
        );
        List<ConfigGradeDetail> temp2 = new ArrayList<>();
        temp2.add(temp);
        assertEquals(temp2, roundArgumentCaptor.getValue());
    }

    //case list input empty
    @Test()
    void saveAll_9(){
        result = configGradeDetailService.saveAll(listTest);
        assertEquals("OK", result.getStatus().name());
    }

    //test exception
    @Test
    void saveAll_11(){
        ServiceResult resultChange;
        List<ConfigGradeDetailForm> list = new ArrayList<>();
        List<ConfigGradeDetail> listRS = new ArrayList<>();
        list.add(new ConfigGradeDetailForm(1,"1",1,"1, 2","1","1"));
        when(configGradeDetailRepository.getAllConfGradeDetailByParentCode(any())).thenReturn(listRS);
        when(configGradeDetailRepository.isScored(any())).thenReturn(0);
        when(configGradeDetailRepository.saveAll(null)).thenReturn(null);
        resultChange = configGradeDetailService.saveAll(list);
        assertEquals("Thêm mới thất bại", resultChange.getMessage());
    }


    //case list input empty
    @Test()
    void deleteAll_1(){
        Integer[] listDelete = new Integer[0];
        resultDelete = configGradeDetailService.deleteAll(listDelete);
        assertEquals("OK", resultDelete.getStatus().name());
    }

    //case input is not list Integer
    @Test()
    void deleteAll_2(){
        resultDelete = configGradeDetailService.deleteAll(null);
        assertEquals("BAD_REQUEST", resultDelete.getStatus().name());
    }

    //case isScored
    @Test()
    void deleteAll_3(){
        Integer[] listInput = new Integer[1];
        listInput[0] = 1;
        ConfigGradeDetail data = new ConfigGradeDetail(
                1,
                "1",
                1,
                "1, 2, 3",
                "1",
                "1"
        );
        when(configGradeDetailRepository.findById(anyInt())).thenReturn(Optional.of(data));
        when(configGradeDetailRepository.isScored(any())).thenReturn(1);

        resultDelete = configGradeDetailService.deleteAll(listInput);
        assertEquals(resultDelete.getStatus().name(), "BAD_REQUEST");
    }

    //case not isScored, ok
    @Test
    void deleteAll_4(){
        Integer[] listInput = new Integer[1];
        listInput[0] = 1;
        ConfigGradeDetail data = new ConfigGradeDetail(
                1,
                "1",
                1,
                "1, 2, 3",
                "1",
                "1"
        );
        when(configGradeDetailRepository.findById(anyInt())).thenReturn(Optional.of(data));
        when(configGradeDetailRepository.isScored(any())).thenReturn(0);

        resultDelete = configGradeDetailService.deleteAll(listInput);
        assertEquals("OK", resultDelete.getStatus().name());

        ArgumentCaptor<List<ConfigGradeDetail>> roundArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(configGradeDetailRepository, times(1)).deleteAll(roundArgumentCaptor.capture());
        List<ConfigGradeDetail> listInputForDeleteAllRepository = new ArrayList<>();
        data = new ConfigGradeDetail(
                1,
                "1",
                1,
                "1, 2, 3",
                "1",
                "1"
        );
        listInputForDeleteAllRepository.add(data);
        assertEquals(listInputForDeleteAllRepository, roundArgumentCaptor.getValue());
    }

    //case null or empty
    @Test()
    void updateChange_1(){
        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                1,
                null,
                1,
                "1",
                "1",
                "1"
        );
        listTest.add(configGradeDetailForm);
        resultUpdateChange = configGradeDetailService.updateChange(listTest);
        assertEquals("BAD_REQUEST", resultUpdateChange.getStatus().name());
        listTest = new ArrayList<>();
        configGradeDetailForm = new ConfigGradeDetailForm(
                1,
                "",
                1,
                "1",
                "1",
                "1"
        );
        listTest.add(configGradeDetailForm);
        resultUpdateChange = configGradeDetailService.updateChange(listTest);
        assertEquals("BAD_REQUEST", resultUpdateChange.getStatus().name());
    }

    //case isScored
    @Test()
    void updateChange_2(){
        List<ConfigGradeDetail> listResult = new ArrayList<>();
        listResult.add(new ConfigGradeDetail(1,"Kiểm tra 15 phút", 0, null, "", "anyString"));

        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                1,
                "1",
                0,
                null,
                "1",
                "1"
        );
        listTest.add(configGradeDetailForm);
        when(configGradeDetailRepository.isScored(any())).thenReturn(1);


        resultUpdateChange = configGradeDetailService.updateChange(listTest);
        assertEquals(resultUpdateChange.getStatus().name(), "BAD_REQUEST");
    }

    //case parent_code null or empty
//    @Test()
//    void updateChange_3(){
//        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
//                1,
//                "1",
//                0,
//                null,
//                "1",
//                null
//        );
//        listTest.add(configGradeDetailForm);
//        resultUpdateChange = configGradeDetailService.updateChange(listTest);
//        assertEquals("BAD_REQUEST", resultUpdateChange.getStatus().name());
//
//        listTest = new ArrayList<>();
//        configGradeDetailForm = new ConfigGradeDetailForm(
//                1,
//                "1",
//                0,
//                null,
//                "1",
//                ""
//        );
//        listTest.add(configGradeDetailForm);
//        resultUpdateChange = configGradeDetailService.updateChange(listTest);
//        assertEquals("BAD_REQUEST", resultUpdateChange.getStatus().name());
//    }

    //case type_choose null
    @Test()
    void updateChange_4(){
        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                1,
                "1",
                null,
                null,
                "1",
                "1"
        );
        listTest.add(configGradeDetailForm);
        resultUpdateChange = configGradeDetailService.updateChange(listTest);
        assertEquals("BAD_REQUEST", resultUpdateChange.getStatus().name());
    }

    //case type_choose = 0 but exist selected_value
    @Test()
    void updateChange_5(){
        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                null,
                "1",
                0,
                "1, 2",
                "1",
                "1"
        );
        listTest.add(configGradeDetailForm);
        resultUpdateChange = configGradeDetailService.updateChange(listTest);
        assertEquals("BAD_REQUEST", resultUpdateChange.getStatus().name());
    }

    //case type_choose = 1 but do not exist selected_value, include selected_value = null or empty
    @Test()
    void updateChange_6(){
        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                1,
                "1",
                1,
                " ",
                "1",
                "1"
        );
        listTest.add(configGradeDetailForm);
        resultUpdateChange = configGradeDetailService.updateChange(listTest);
        assertEquals("BAD_REQUEST", resultUpdateChange.getStatus().name());

        listTest = new ArrayList<>();
        configGradeDetailForm = new ConfigGradeDetailForm(
                1,
                "1",
                1,
                null,
                "1",
                "1"
        );
        listTest.add(configGradeDetailForm);
        resultUpdateChange = configGradeDetailService.updateChange(listTest);
        assertEquals("BAD_REQUEST", resultUpdateChange.getStatus().name());
    }


    //case type_choose out of range [0, 1]
    @Test()
    void updateChange_7(){
        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                1,
                "1",
                10,
                "1, 2",
                "1",
                "1"
        );
        listTest.add(configGradeDetailForm);
        resultUpdateChange = configGradeDetailService.updateChange(listTest);
        assertEquals("BAD_REQUEST", resultUpdateChange.getStatus().name());
    }

    //case ok
    @Test()
    void updateChange_8(){
        List<ConfigGradeDetail> listResult = new ArrayList<>();
        listResult.add(new ConfigGradeDetail(1,"Kiểm tra 15 phút", 0, null, "", "anyString"));

        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                1,
                "1",
                1,
                "1, 2, 3",
                "1",
                "1"
        );
        listTest.add(configGradeDetailForm);

        List<ConfigGradeDetail> listExpect = new ArrayList<>();
        ConfigGradeDetail configGradeDetail = new ConfigGradeDetail(
                1,
                "1",
                1,
                "1, 2, 3",
                "1",
                "1"
        );
        listExpect.add(configGradeDetail);

        when(configGradeDetailRepository.isScored(any())).thenReturn(0);
        when(configGradeDetailRepository.findById(any())).thenReturn(Optional.of(configGradeDetail));
        when(configGradeDetailRepository.saveAll(any())).thenReturn(listExpect);

        resultUpdateChange = configGradeDetailService.updateChange(listTest);
        assertEquals(resultUpdateChange.getData(), true);

        ArgumentCaptor<List<ConfigGradeDetail>> roundArgumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(configGradeDetailRepository, times(1)).saveAll(roundArgumentCaptor.capture());
        ConfigGradeDetail temp = new ConfigGradeDetail(
                1,
                "1",
                1,
                "1, 2, 3",
                "1",
                "1"
        );
        List<ConfigGradeDetail> temp2 = new ArrayList<>();
        temp2.add(temp);
        assertEquals(temp2, roundArgumentCaptor.getValue());
    }

    //case list input empty
    @Test()
    void updateChange_9(){
        resultUpdateChange = configGradeDetailService.updateChange(listTest);
        assertEquals("OK", resultUpdateChange.getStatus().name());
    }

    //case id null
    @Test()
    void updateChange_10(){
        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                null,
                "1",
                1,
                "1",
                "1",
                "1"
        );
        listTest.add(configGradeDetailForm);
        resultUpdateChange = configGradeDetailService.updateChange(listTest);
        assertEquals("BAD_REQUEST", resultUpdateChange.getStatus().name());
    }

    //case code null or empty
    @Test
    void updateChange_11(){
        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                1,
                "1",
                1,
                "1, 2",
                " ",
                "1"
        );
        listTest.add(configGradeDetailForm);
        resultUpdateChange = configGradeDetailService.updateChange(listTest);
        assertEquals("BAD_REQUEST", resultUpdateChange.getStatus().name());

        listTest = new ArrayList<>();
        configGradeDetailForm = new ConfigGradeDetailForm(
                1,
                "1",
                1,
                "1, 2",
                null,
                "1"
        );
        listTest.add(configGradeDetailForm);
        resultUpdateChange = configGradeDetailService.updateChange(listTest);
        assertEquals("BAD_REQUEST", resultUpdateChange.getStatus().name());
    }

    //case list input contain 1 object true and 1 object false
    @Test
    void updateChange_12(){
        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                1,
                "1",
                1,
                "1, 2",
                "1",
                "1"
        );
        listTest.add(configGradeDetailForm);
        configGradeDetailForm = new ConfigGradeDetailForm(
                1,
                "1",
                1,
                "1, 2",
                null,
                "1"
        );
        listTest.add(configGradeDetailForm);
        resultUpdateChange = configGradeDetailService.updateChange(listTest);
        assertEquals("BAD_REQUEST", resultUpdateChange.getStatus().name());
    }

    //case selected_value length > 0
    @Test()
    void updateChange_13(){
        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                1,
                "1",
                0,
                "1, 2",
                "1",
                "1"
        );
        listTest.add(configGradeDetailForm);
        resultUpdateChange = configGradeDetailService.updateChange(listTest);
        assertEquals("BAD_REQUEST", resultUpdateChange.getStatus().name());
    }

    //case parent_code empty
    @Test()
    void updateChange_14(){
        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                1,
                "1",
                0,
                null,
                "1",
                ""
        );
        listTest.add(configGradeDetailForm);
        resultUpdateChange = configGradeDetailService.updateChange(listTest);
        assertEquals("BAD_REQUEST", resultUpdateChange.getStatus().name());
    }

    //case parent_code empty
    @Test()
    void updateChange_15(){
        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                1,
                "1",
                1,
                "1, 2",
                "1",
                "   "
        );
        listTest.add(configGradeDetailForm);
        resultUpdateChange = configGradeDetailService.updateChange(listTest);
        assertEquals("BAD_REQUEST", resultUpdateChange.getStatus().name());
    }

    //case pass all but parent_code null
    @Test()
    void updateChange_16(){
        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                1,
                "1",
                1,
                "1, 2",
                "1",
                null
        );
        listTest.add(configGradeDetailForm);
        resultUpdateChange = configGradeDetailService.updateChange(listTest);
        assertEquals("BAD_REQUEST", resultUpdateChange.getStatus().name());
    }

    //case pass all but parent_code null
    @Test()
    void updateChange_17(){
        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                1,
                "1",
                0,
                "",
                "1",
                null
        );
        listTest.add(configGradeDetailForm);
        resultUpdateChange = configGradeDetailService.updateChange(listTest);
        assertEquals("BAD_REQUEST", resultUpdateChange.getStatus().name());
    }

    //case pass all but parent_code null
    @Test()
    void updateChange_18(){
        ConfigGradeDetailForm configGradeDetailForm = new ConfigGradeDetailForm(
                1,
                "1",
                0,
                null,
                "1",
                null
        );
        listTest.add(configGradeDetailForm);
        resultUpdateChange = configGradeDetailService.updateChange(listTest);
        assertEquals("BAD_REQUEST", resultUpdateChange.getStatus().name());
    }

    //test exception
    @Test
    void updateChange_19(){
        ServiceResult resultChange;
        List<ConfigGradeDetailForm> list = new ArrayList<>();
        list.add(new ConfigGradeDetailForm(1,"1",1,"1, 2","1","1"));
        when(configGradeDetailRepository.isScored(any())).thenReturn(0);
        when(configGradeDetailRepository.findById(anyInt())).thenReturn(Optional.of(new ConfigGradeDetail(1,"1",1,"1, 2","1","1")));
        when(configGradeDetailRepository.saveAll(null)).thenReturn(null);
        resultChange = configGradeDetailService.updateChange(list);
        assertEquals("Cập nhật thất bại", resultChange.getMessage());
    }
}
