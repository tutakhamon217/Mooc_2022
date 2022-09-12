package fpt.capstone.form_data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ImportScoreForm {
    private String subjectCode;
    private String classCode;
    private String schoolYear;
    private String semester;
    private String  login;
    private List<Integer> confScoreDetailsIdList;
}
