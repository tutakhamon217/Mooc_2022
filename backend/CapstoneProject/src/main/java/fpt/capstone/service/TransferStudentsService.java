package fpt.capstone.service;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.TransferStudentsForm;
import fpt.capstone.form_data.TransferStudentsSearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TransferStudentsService {

//    TransferStudentsForm save(TransferStudentsForm transferStudentsDTO);
//
//    /**
//     * Partially updates a transferStudents.
//     *
//     * @param transferStudentsDTO the entity to update partially.
//     * @return the persisted entity.
//     */
//    Optional<TransferStudentsForm> partialUpdate(TransferStudentsForm transferStudentsDTO);
//
//    /**
//     * Get all the transferStudents.
//     *
//     * @param pageable the pagination information.
//     * @return the list of entities.
//     */
//    Page<TransferStudentsForm> findAll(Pageable pageable);
//
//    /**
//     * Get the "id" transferStudents.
//     *
//     * @param id the id of the entity.
//     * @return the entity.
//     */
//    Optional<TransferStudentsForm> findOne(Long id);
//
//    /**
//     * Delete the "id" transferStudents.
//     *
//     * @param id the id of the entity.
//     */
//    void delete(Long id);

    Page<TransferStudentsForm> searchTransferStudents(TransferStudentsSearchForm transferStudentsSearchForm, Pageable pageable);

    ServiceResult<Boolean> transferStudents(List<TransferStudentsForm> transferStudentsFormList) throws Exception;

    ByteArrayInputStream export(String className, String currentYear, String newYear,List<TransferStudentsForm> transferStudentsFormList);

    // ServiceResult<Boolean> transfer1Student(TransferStudentsForm transferStudentsForm);

    // ServiceResult<Boolean> UpdateTransfer1Student(TransferStudentsForm transferStudentsForm);

}
