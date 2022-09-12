package fpt.capstone.repository;

import fpt.capstone.form_data.TransferStudentsForm;
import fpt.capstone.form_data.TransferStudentsSearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransferStudentCustomRepository {
    Page<TransferStudentsForm> searchTransferStudents(TransferStudentsSearchForm transferStudentsSearchForm, Pageable pageable);

//    List<TransferStudentsForm> searchTransferStudents(TransferStudentsSearchForm transferStudentsSearchForm);
}
