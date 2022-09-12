package fpt.capstone.controller;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.form_data.TransferStudentsForm;
import fpt.capstone.form_data.TransferStudentsSearchForm;
import fpt.capstone.repository.TransferStudentsRepository;
import fpt.capstone.service.TransferStudentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@RestController
@CrossOrigin(origins = "*")
public class TransferStudentsController {
//
//    private static final String ENTITY_NAME = "transferStudents";
//
//    @Value("${jhipster.clientApp.name}")
//    private String applicationName;
    @Autowired
    private TransferStudentsService transferStudentsService;
    @Autowired
    private TransferStudentsRepository transferStudentsRepository;



//    @PostMapping("/transfer-students")
//    public ResponseEntity<TransferStudentsDTO> createTransferStudents(@RequestBody TransferStudentsDTO transferStudentsDTO)
//            throws URISyntaxException {
//        log.debug("REST request to save TransferStudents : {}", transferStudentsDTO);
//        if (transferStudentsDTO.getId() != null) {
//            throw new BadRequestAlertException("A new transferStudents cannot already have an ID", ENTITY_NAME, "idexists");
//        }
//        TransferStudentsDTO result = transferStudentsService.save(transferStudentsDTO);
//        return ResponseEntity
//                .created(new URI("/api/transfer-students/" + result.getId()))
//                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
//                .body(result);
//    }
//
//    @PutMapping("/transfer-students/{id}")
//    public ResponseEntity<TransferStudentsDTO> updateTransferStudents(
//            @PathVariable(value = "id", required = false) final Long id,
//            @RequestBody TransferStudentsDTO transferStudentsDTO
//    ) throws URISyntaxException {
//        log.debug("REST request to update TransferStudents : {}, {}", id, transferStudentsDTO);
//        if (transferStudentsDTO.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, transferStudentsDTO.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!transferStudentsRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        TransferStudentsDTO result = transferStudentsService.save(transferStudentsDTO);
//        return ResponseEntity
//                .ok()
//                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transferStudentsDTO.getId().toString()))
//                .body(result);
//    }
//
//
//    @PatchMapping(value = "/transfer-students/{id}", consumes = "application/merge-patch+json")
//    public ResponseEntity<TransferStudentsDTO> partialUpdateTransferStudents(
//            @PathVariable(value = "id", required = false) final Long id,
//            @RequestBody TransferStudentsDTO transferStudentsDTO
//    ) throws URISyntaxException {
//        log.debug("REST request to partial update TransferStudents partially : {}, {}", id, transferStudentsDTO);
//        if (transferStudentsDTO.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, transferStudentsDTO.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!transferStudentsRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        Optional<TransferStudentsDTO> result = transferStudentsService.partialUpdate(transferStudentsDTO);
//
//        return ResponseUtil.wrapOrNotFound(
//                result,
//                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transferStudentsDTO.getId().toString())
//        );
//    }
//
//    @GetMapping("/transfer-students")
//    public ResponseEntity<List<TransferStudentsDTO>> getAllTransferStudents(Pageable pageable) {
//        log.debug("REST request to get a page of TransferStudents");
//        Page<TransferStudentsDTO> page = transferStudentsService.findAll(pageable);
//        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
//        return ResponseEntity.ok().headers(headers).body(page.getContent());
//    }
//
//    @GetMapping("/transfer-students/{id}")
//    public ResponseEntity<TransferStudentsDTO> getTransferStudents(@PathVariable Long id) {
//        log.debug("REST request to get TransferStudents : {}", id);
//        Optional<TransferStudentsDTO> transferStudentsDTO = transferStudentsService.findOne(id);
//        return ResponseUtil.wrapOrNotFound(transferStudentsDTO);
//    }
//
//
//    @DeleteMapping("/transfer-students/{id}")
//    public ResponseEntity<Void> deleteTransferStudents(@PathVariable Long id) {
//        log.debug("REST request to delete TransferStudents : {}", id);
//        transferStudentsService.delete(id);
//        return ResponseEntity
//                .noContent()
//                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
//                .build();
//    }

    @PostMapping("/transfer-students/search")
    public ResponseEntity<Page<TransferStudentsForm>> searchTransferStudents(@RequestBody TransferStudentsSearchForm transferStudentsSearchForm) {
         Pageable pageable=PageRequest.of(0, Integer.MAX_VALUE);
        Page<TransferStudentsForm> result = transferStudentsService.searchTransferStudents(transferStudentsSearchForm, pageable);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/transfer-students/transfer")
    public ServiceResult<Boolean> transferStudents(@RequestBody List<TransferStudentsForm> transferStudentsFormList) {
        try {
            return transferStudentsService.transferStudents(transferStudentsFormList);
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Kết chuyển học sinh thất bại", null);
        }

    }

    @PostMapping("/transfer-students/export")
    public ResponseEntity<Resource> export(@RequestParam(required = true) String className, @RequestParam(required = true) String currentYear, @RequestParam(required = true) String newYear, @RequestBody List<TransferStudentsForm> transferStudentsFormList) {
        ByteArrayInputStream tmp = transferStudentsService.export(className, currentYear, newYear, transferStudentsFormList);
        if (tmp != null) {
            InputStreamResource file = new InputStreamResource(tmp);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=DS_KetChuyenHocSinh.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(file);
        } else {
            return ResponseEntity.badRequest().body(new InputStreamResource(new ByteArrayInputStream(null)));
        }

    }
    // @PostMapping("/transfer-students/transfer1student")
    // public ServiceResult<Boolean> transfer1Students(@RequestBody TransferStudentsForm transferStudentsForm) {
    //     return transferStudentsService.transfer1Student(transferStudentsForm);
    // }

    // @PostMapping("/transfer-students/transfer1student/update")
    // public ServiceResult<Boolean> UpdateTransfer1Students(@RequestBody TransferStudentsForm transferStudentsForm) {
    //     return transferStudentsService.UpdateTransfer1Student(transferStudentsForm);
    // }
}
