package fpt.capstone.service;

import fpt.capstone.form_data.TransferStudentsDetailsForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TransferStudentsDetailsService {
    /**
     * Save a transferStudentsDetails.
     *
     * @param transferStudentsDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    TransferStudentsDetailsForm save(TransferStudentsDetailsForm transferStudentsDetailsDTO);

    /**
     * Partially updates a transferStudentsDetails.
     *
     * @param transferStudentsDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransferStudentsDetailsForm> partialUpdate(TransferStudentsDetailsForm transferStudentsDetailsDTO);

    /**
     * Get all the transferStudentsDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TransferStudentsDetailsForm> findAll(Pageable pageable);

    /**
     * Get the "id" transferStudentsDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransferStudentsDetailsForm> findOne(Long id);

    /**
     * Delete the "id" transferStudentsDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
