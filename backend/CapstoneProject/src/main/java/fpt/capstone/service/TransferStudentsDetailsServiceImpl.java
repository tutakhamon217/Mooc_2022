//package fpt.capstone.service;
//
//import fpt.capstone.entities.TransferStudentsDetails;
//import fpt.capstone.form_data.TransferStudentsDetailsForm;
//import fpt.capstone.repository.TransferStudentsDetailsRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//@Service
//@javax.transaction.Transactional
//public class TransferStudentsDetailsServiceImpl implements TransferStudentsDetailsService{
//    private final Logger log = LoggerFactory.getLogger(TransferStudentsDetailsServiceImpl.class);
//    @Autowired
//    private TransferStudentsDetailsRepository transferStudentsDetailsRepository;
//    @Autowired
//    private  TransferStudentsDetailsMapper transferStudentsDetailsMapper;
//
//    @Override
//    public TransferStudentsDetailsForm save(TransferStudentsDetailsForm transferStudentsDetailsDTO) {
//        log.debug("Request to save TransferStudentsDetails : {}", transferStudentsDetailsDTO);
//        TransferStudentsDetails transferStudentsDetails = transferStudentsDetailsMapper.toEntity(transferStudentsDetailsDTO);
//        transferStudentsDetails = transferStudentsDetailsRepository.save(transferStudentsDetails);
//        return transferStudentsDetailsMapper.toDto(transferStudentsDetails);
//    }
//
////    @Override
////    public Optional<TransferStudentsDetailsForm> partialUpdate(TransferStudentsDetailsForm transferStudentsDetailsDTO) {
////        log.debug("Request to partially update TransferStudentsDetails : {}", transferStudentsDetailsDTO);
////
////        return transferStudentsDetailsRepository
////                .findById(transferStudentsDetailsDTO.getId())
////                .map(
////                        existingTransferStudentsDetails -> {
////                            transferStudentsDetailsMapper.partialUpdate(existingTransferStudentsDetails, transferStudentsDetailsDTO);
////
////                            return existingTransferStudentsDetails;
////                        }
////                )
////                .map(transferStudentsDetailsRepository::save)
////                .map(transferStudentsDetailsMapper::toDto);
////    }
////
////    @Override
////    @Transactional(readOnly = true)
////    public Page<TransferStudentsDetailsDTO> findAll(Pageable pageable) {
////        log.debug("Request to get all TransferStudentsDetails");
////        return transferStudentsDetailsRepository.findAll(pageable).map(transferStudentsDetailsMapper::toDto);
////    }
////
////    @Override
////    @Transactional(readOnly = true)
////    public Optional<TransferStudentsDetailsDTO> findOne(Long id) {
////        log.debug("Request to get TransferStudentsDetails : {}", id);
////        return transferStudentsDetailsRepository.findById(id).map(transferStudentsDetailsMapper::toDto);
////    }
////
////    @Override
////    public void delete(Long id) {
////        log.debug("Request to delete TransferStudentsDetails : {}", id);
////        transferStudentsDetailsRepository.deleteById(id);
////    }
//}
