package fpt.capstone.service;

import fpt.capstone.entities.ConfigScoreSubject;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.repository.ConfigScoreSubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ConfigScoreSubjectServiceImpl implements ConfigScoreSubjectService {

    @Autowired
    private ConfigScoreSubjectRepository configScoreSubjectRepository;

    @Override
    public ServiceResult<String> getCodeOfCSS(String subject_code) {
        try {
            List<ConfigScoreSubject> css = configScoreSubjectRepository.getCodeOfCSS(subject_code);
            if (css.size() == 0) {
                throw new ChangeSetPersister.NotFoundException();
            }
            return new ServiceResult<>(HttpStatus.OK, "success", css.get(0).getCode());
        } catch (ChangeSetPersister.NotFoundException e) {
            return new ServiceResult<>(HttpStatus.FAILED_DEPENDENCY, "Có lỗi xảy ra", null);
        }
    }
}
