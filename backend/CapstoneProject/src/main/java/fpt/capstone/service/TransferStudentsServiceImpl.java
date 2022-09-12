package fpt.capstone.service;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.entities.StudentHistory;
import fpt.capstone.entities.TransferStudents;
import fpt.capstone.entities.TransferStudentsDetails;
import fpt.capstone.form_data.TransferStudentsDetailsForm;
import fpt.capstone.form_data.TransferStudentsForm;
import fpt.capstone.form_data.TransferStudentsSearchForm;
import fpt.capstone.repository.StudentHistoryRepository;
import fpt.capstone.repository.StudentHistoryRepository;
import fpt.capstone.repository.TransferStudentCustomRepository;
import fpt.capstone.repository.TransferStudentsDetailsRepository;
import fpt.capstone.repository.TransferStudentsRepository;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;


import org.apache.poi.ss.usermodel.Font;


@Service
@Transactional(rollbackOn = {Exception.class})
public class TransferStudentsServiceImpl implements TransferStudentsService {
    private final Logger log = LoggerFactory.getLogger(TransferStudentsServiceImpl.class);
    @Autowired
    private TransferStudentsRepository transferStudentsRepository;
    @Autowired
    private TransferStudentsDetailsRepository transferStudentsDetailsRepository;
    @Autowired
    private TransferStudentCustomRepository transferStudentCustomRepository;
    @Autowired
    private StudentHistoryRepository studentHistoryRepository;


    @Override
    public Page<TransferStudentsForm> searchTransferStudents(TransferStudentsSearchForm transferStudentsSearchForm, Pageable pageable) {
        return transferStudentCustomRepository.searchTransferStudents(transferStudentsSearchForm, pageable);
    }

    @Override
    public ServiceResult<Boolean> transferStudents(List<TransferStudentsForm> transferStudentsFormList) throws Exception {
        for (TransferStudentsForm dto : transferStudentsFormList) {
            // if (dto.getCompetitionTitle() == null || dto.getCompetitionTitle().equals("")) {
            //     return new ServiceResult<>(HttpStatus.OK, "Không cho phép kết chuyển do có học sinh: " + dto.getDetails().getStudentName() + " chưa có danh hiệu thi đua", null);
            // }
            try {
                TransferStudents transferStudents = transferStudentsRepository.findBySchoolYearAndGradeCodeAndClassCode(
                        dto.getSchoolYear(),
                        dto.getGradeCode(),
                        dto.getClassCode()
                ).orElseGet(() ->
                    transferStudentsRepository.save(
                            new TransferStudents(UUID.randomUUID().toString().replace("-", ""), dto.getSchoolYear(), dto.getGradeCode(), dto.getClassCode())
                    )
                );
                TransferStudentsDetailsForm detailsDto = dto.getDetails();
                TransferStudentsDetails transferStudentsDetails = null;
                if (null!=detailsDto.getId()) {
                     transferStudentsDetails = new TransferStudentsDetails(detailsDto.getId(), detailsDto.getCode(),
                            detailsDto.getCurrentSchoolYear(), detailsDto.getCurrentClassCode(), detailsDto.getStudentCode(), detailsDto.getAcademicAbility(), detailsDto.getConduct(),
                            detailsDto.getStatus(), detailsDto.getNewClassCode(), detailsDto.getNewSchoolYear(), detailsDto.getId() == null ? transferStudents.getCode() : detailsDto.getParentCode());
                }else {
                    transferStudentsDetails = new TransferStudentsDetails(UUID.randomUUID().toString().replace("-", ""),
                            dto.getSchoolYear(), detailsDto.getCurrentClassCode(), detailsDto.getStudentCode(), detailsDto.getAcademicAbility(), detailsDto.getConduct(),
                            detailsDto.getStatus(), detailsDto.getNewClassCode(), detailsDto.getNewSchoolYear(), detailsDto.getId() == null ? transferStudents.getCode() : detailsDto.getParentCode());
                }
                transferStudentsDetailsRepository.save(transferStudentsDetails);
                StudentHistory studentHistoryUse = new StudentHistory();
                Optional<StudentHistory> studentHistory = studentHistoryRepository.findByStudentCodeAndYear(transferStudentsDetails.getStudentCode(), transferStudentsDetails.getNewSchoolYear());
                if (studentHistory.isPresent()) {
                    studentHistoryUse = studentHistory.get();
                    studentHistoryUse.setCurrentClassCode(transferStudentsDetails.getNewClassCode());
                } else {
                    studentHistoryUse.setStudentCode(transferStudentsDetails.getStudentCode());
                    studentHistoryUse.setYear(transferStudentsDetails.getNewSchoolYear());
                    studentHistoryUse.setCurrentClassCode(transferStudentsDetails.getNewClassCode());
                }
                studentHistoryRepository.save(studentHistoryUse);
            } catch (Exception e) {
                e.printStackTrace();
                // for roll back:
                throw new Exception();
            }
        }
        return new ServiceResult<>(HttpStatus.OK, "Kết chuyển học sinh thành công", null);
    }

    @Override
    public ByteArrayInputStream export(String className, String currentYear, String newYear,List<TransferStudentsForm> transferStudentsFormList)  {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet transferSheet = workbook.createSheet("transferStudent");
            
            CellStyle commonStyle = workbook.createCellStyle();
            commonStyle.setAlignment(HorizontalAlignment.CENTER);
            commonStyle.setVerticalAlignment(VerticalAlignment.CENTER);


            
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setTopBorderColor(IndexedColors.BLACK.index);
            cellStyle.setRightBorderColor(IndexedColors.BLACK.index);
            cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);
            cellStyle.setLeftBorderColor(IndexedColors.BLACK.index);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);


            CellStyle cellStyleBold = workbook.createCellStyle();
            cellStyleBold.setTopBorderColor(IndexedColors.BLACK.index);
            cellStyleBold.setRightBorderColor(IndexedColors.BLACK.index);
            cellStyleBold.setBottomBorderColor(IndexedColors.BLACK.index);
            cellStyleBold.setLeftBorderColor(IndexedColors.BLACK.index);
            cellStyleBold.setBorderTop(BorderStyle.THIN);
            cellStyleBold.setBorderBottom(BorderStyle.THIN);
            cellStyleBold.setBorderLeft(BorderStyle.THIN);
            cellStyleBold.setBorderRight(BorderStyle.THIN);
            cellStyleBold.setAlignment(HorizontalAlignment.CENTER);
            cellStyleBold.setVerticalAlignment(VerticalAlignment.CENTER);
            Font font= workbook.createFont();
            font.setBold(true);
            cellStyleBold.setFont(font);


            CellStyle cellOnlyBold = workbook.createCellStyle();
            cellStyleBold.setAlignment(HorizontalAlignment.CENTER);
            cellStyleBold.setVerticalAlignment(VerticalAlignment.CENTER);
            Font fontf= workbook.createFont();
            fontf.setBold(true);
            fontf.setFontHeightInPoints((short) 14);
            cellOnlyBold.setFont(fontf);   

            transferSheet.createRow(0).createCell(0).setCellValue("DANH SÁCH KẾT CHUYỂN HỌC SINH " + className);
            transferSheet.getRow(0).getCell(0).setCellStyle(cellOnlyBold);
            transferSheet.createRow(0).createCell(1).setCellStyle(cellOnlyBold);
            transferSheet.createRow(0).createCell(2).setCellStyle(cellOnlyBold);
            transferSheet.createRow(0).createCell(3).setCellStyle(cellOnlyBold);
            transferSheet.createRow(0).createCell(4).setCellStyle(cellOnlyBold);
            transferSheet.createRow(0).createCell(5).setCellStyle(cellOnlyBold);
            transferSheet.createRow(0).createCell(6).setCellStyle(cellOnlyBold);
            transferSheet.createRow(0).createCell(7).setCellStyle(cellOnlyBold);
            transferSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
            

            transferSheet.createRow(1).createCell(0).setCellValue("Năm học " + currentYear);
            transferSheet.getRow(1).getCell(0).setCellStyle(commonStyle);
            transferSheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
            transferSheet.createRow(2);
            transferSheet.createRow(3);

            transferSheet.createRow(4).createCell(0).setCellValue("STT");
            transferSheet.getRow(4).createCell(1).setCellValue("Năm học " + currentYear);
            transferSheet.getRow(4).createCell(6).setCellValue("Năm học " + newYear);

            transferSheet.getRow(4).getCell(0).setCellStyle(cellStyleBold);
            transferSheet.getRow(4).getCell(1).setCellStyle(cellStyleBold);
            transferSheet.getRow(4).getCell(6).setCellStyle(cellStyleBold);

            transferSheet.getRow(4).createCell(2).setCellStyle(cellStyleBold);
            transferSheet.getRow(4).createCell(3).setCellStyle(cellStyleBold);
            transferSheet.getRow(4).createCell(4).setCellStyle(cellStyleBold);
            transferSheet.getRow(4).createCell(5).setCellStyle(cellStyleBold);

            transferSheet.getRow(4).createCell(7).setCellStyle(cellStyleBold);


            transferSheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 5));
            transferSheet.addMergedRegion(new CellRangeAddress(4, 4, 6, 7));

            transferSheet.createRow(5).createCell(1).setCellValue("Mã học sinh");
            transferSheet.getRow(5).getCell(1).setCellStyle(cellStyleBold);

            transferSheet.getRow(5).createCell(0).setCellValue("STT");
            transferSheet.getRow(5).getCell(0).setCellStyle(cellStyleBold);

            transferSheet.addMergedRegion(new CellRangeAddress(4, 5, 0, 0));

            transferSheet.getRow(5).createCell(2).setCellValue("Tên học sinh");
            transferSheet.getRow(5).getCell(2).setCellStyle(cellStyleBold);

            transferSheet.getRow(5).createCell(3).setCellValue("Tên lớp");
            transferSheet.getRow(5).getCell(3).setCellStyle(cellStyleBold);

            transferSheet.getRow(5).createCell(4).setCellValue("Danh hiệu thi đua");
            transferSheet.getRow(5).getCell(4).setCellStyle(cellStyleBold);

            transferSheet.getRow(5).createCell(5).setCellValue("Trạng thái");
            transferSheet.getRow(5).getCell(5).setCellStyle(cellStyleBold);

            transferSheet.getRow(5).createCell(6).setCellValue("Được lên lớp");
            transferSheet.getRow(5).getCell(6).setCellStyle(cellStyleBold);

            transferSheet.getRow(5).createCell(7).setCellValue("Lớp lưu ban");
            transferSheet.getRow(5).getCell(7).setCellStyle(cellStyleBold);


            
            int startRowCreate = 5;
            // Get all rows
            int rowNum = 0;
            for(TransferStudentsForm i: transferStudentsFormList) {
                rowNum += 1;
                startRowCreate += 1;
                
                Row currentRow = transferSheet.getRow(startRowCreate);
                if (currentRow == null) {
                    currentRow = transferSheet.createRow(startRowCreate);
                }
                for(int j=0;j<8;j++) {
                    Cell cell = currentRow.getCell(j);
                    if (cell == null) {
                        cell = currentRow.createCell(j);
                    }
                    cell.setCellStyle(cellStyle);
                    if(j == 0) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(rowNum);
                    
                    } else if (j==1) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellValue(i.getDetails().getStudentCode());   

                    } else if (j==2) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellValue(i.getDetails().getStudentName());    
                    } else if (j==3) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellValue(i.getClassCode() + " - " + i.getClassName());    
                    }
                    else if (j==4) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if(i.getCompetitionTitle() !=  null) {
                            cell.setCellValue(i.getCompetitionTitle());   
                        } else {
                            cell.setCellValue("");
                        }
                    } else if (j==5) {
                        if (i.getDetails().getStatus() == null) {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            cell.setCellValue("Chưa kết chuyển");   
                        } else if (i.getDetails().getStatus() == 0) {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            cell.setCellValue("Lưu ban");   
                        } else {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            cell.setCellValue("Được lên lớp");   
                        }
                    } else if (j==6) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (i.getDetails().getStatus() == null) {
                            cell.setCellValue("");
                        }
                        else if (i.getDetails().getStatus() == 1){
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            if(i.getDetails().getNewClassName() !=  null) {
                                cell.setCellValue(i.getDetails().getNewClassName());   
                            } else  {
                                cell.setCellValue("");   
                            }  
                        } else {
                            cell.setCellValue("");
                        }
                    } else if (j == 7) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if (i.getDetails().getStatus() == null) {
                            cell.setCellValue("");
                        } else if (i.getDetails().getStatus() == 0) {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            if(i.getDetails().getNewClassName() !=  null) {
                                cell.setCellValue(i.getDetails().getNewClassName()); 
                            } else {
                                cell.setCellValue("");
                            }
                        } else {
                            cell.setCellValue("");
                        }
                    }
                }        
            }

            for (int j = 0; j < 8; j++) {
                transferSheet.autoSizeColumn(j);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }
    // @Override
    // public ServiceResult<Boolean> transfer1Student(TransferStudentsForm form) {
    //     if (form.getCompetitionTitle() == null || form.getCompetitionTitle().equals("")) {
    //         return new ServiceResult<>(HttpStatus.OK, "Không cho phép kết chuyển do có học sinh: " + form.getDetails().getStudentName() + " không hợp lệ", null);
    //     }
    //     else{
    //         TransferStudents transferStudents = transferStudentsRepository.findBySchoolYearAndGradeCodeAndClassCode(
    //                 form.getSchoolYear(),
    //                 form.getGradeCode(),
    //                 form.getClassCode()
    //         ).orElseGet(() ->
    //                 transferStudentsRepository.save(
    //                         new TransferStudents(UUID.randomUUID().toString().replace("-", ""), form.getSchoolYear(), form.getGradeCode(), form.getClassCode())
    //                 )
    //         );
    //         TransferStudentsDetailsForm detailsDto = form.getDetails();
    //         TransferStudentsDetails transferStudentsDetails = new TransferStudentsDetails(detailsDto.getId(), detailsDto.getId() == null ? UUID.randomUUID().toString().replace("-", "") : detailsDto.getCode(),
    //                 detailsDto.getCurrentClassCode(), detailsDto.getCurrentClassCode(), detailsDto.getStudentCode(), detailsDto.getAcademicAbility(), detailsDto.getConduct(),
    //                 detailsDto.getStatus(), detailsDto.getNewClassCode(), detailsDto.getNewSchoolYear(), detailsDto.getId() == null ? transferStudents.getCode() : detailsDto.getParentCode());

    //         transferStudentsDetailsRepository.save(transferStudentsDetails);
    //         StudentHistory studentHistoryUse = new StudentHistory();
    //         Optional<StudentHistory> studentHistory = studentHistoryRepository.findByStudentCodeAndYear(transferStudentsDetails.getStudentCode(), transferStudentsDetails.getNewSchoolYear());
    //         if (null != studentHistory) {
    //             studentHistoryUse = studentHistory.get();
    //             studentHistoryUse.setCurrentClassCode(transferStudentsDetails.getNewClassCode());
    //         } else {
    //             studentHistoryUse.setStudentCode(transferStudentsDetails.getStudentCode());
    //             studentHistoryUse.setYear(transferStudentsDetails.getNewSchoolYear());
    //             studentHistoryUse.setCurrentClassCode(transferStudentsDetails.getNewClassCode());
    //         }
    //         studentHistoryRepository.save(studentHistoryUse);
    //     }
    //     return new ServiceResult<>(HttpStatus.OK, "Kết chuyển học sinh thành công", null);
    // }

    // @Override
    // public ServiceResult<Boolean> UpdateTransfer1Student(TransferStudentsForm transferStudentsForm) {
    //     if (null==transferStudentsForm.getDetails().getId()||transferStudentsDetailsRepository.getById(transferStudentsForm.getDetails().getId())==null){
    //         return new ServiceResult<>(HttpStatus.OK, "Học sinh này chưa kết chuyển nên không thể cập nhât", null);
    //     }
    //     else {
    //         TransferStudentsDetails transferStudentsDetails =transferStudentsDetailsRepository.getById(transferStudentsForm.getDetails().getId());
    //         transferStudentsDetails.setNewClassCode(transferStudentsForm.getDetails().getNewClassCode());
    //         transferStudentsDetails.setStatus(transferStudentsForm.getDetails().getStatus());
    //         transferStudentsDetailsRepository.save(transferStudentsDetails);
    //         Optional<StudentHistory> studentHistory = studentHistoryRepository.findByStudentCodeAndYear(transferStudentsDetails.getStudentCode(), transferStudentsDetails.getNewSchoolYear());
    //         StudentHistory studentHistoryUpdate=studentHistory.get();
    //         if (studentHistory.isPresent()) {
    //             studentHistoryUpdate.setCurrentClassCode(transferStudentsForm.getDetails().getNewClassCode());
    //             studentHistoryRepository.save(studentHistoryUpdate);
    //         } else {
    //             StudentHistory studentHistoryNew =new StudentHistory(transferStudentsForm.getDetails().getStudentCode(),transferStudentsForm.getDetails().getNewClassCode(),transferStudentsForm.getDetails().getNewSchoolYear());
    //             studentHistoryRepository.save(studentHistoryNew);
    //         }
    //     }
    //     return new ServiceResult<>(HttpStatus.OK, "Cập nhật kết chuyển học sinh thành công", null);    }
}
