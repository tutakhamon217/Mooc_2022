package fpt.capstone.service;

import fpt.capstone.entities.*;
import fpt.capstone.form_data.TeachingAssignmentUpdate;
import fpt.capstone.payroll.TeachingAssignmentImportException;
import fpt.capstone.payroll.ValidationException;
import fpt.capstone.repository.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.TransactionRequiredException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;


@Service
@Transactional
public class TeachingAssignmentServiceImpl implements TeachingAssignmentService {
    @Autowired
    private TeachingAssignmentRepository teachingAssignmentRepository;

    @Autowired
    private SubjectsRepository subjectsRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SchoolYearRespository schoolYearRespository;

    @Autowired
    private SubjectClassRepository subjectClassRepository;

    @Autowired
    private ClassRoomRepository classRoomRepository;


    @Override
    public ServiceResult<Map<String, Object>> getAllTeachingAssignmentSearching(String nameCodeTeacher, String nameCodeSubject, Integer classId) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        try {
            List<Map<String, Object>> teachingAssignments = teachingAssignmentRepository.getAllTeachingAssignmentSearching(nameCodeTeacher, nameCodeSubject, classId);
            Map<String, Object> output = new HashMap<>();
            output.put("teachingAssignment", teachingAssignments);
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(output);
        } catch (Exception e) {
            e.printStackTrace();
            serviceResult.setMessage("fail");
            serviceResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return serviceResult;
    }

    public ServiceResult<Map<String, Object>> delete(Integer teachingAssignmentId) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        if (subjectsRepository.isMarked(teachingAssignmentId) > 0) {
            throw new ValidationException("teaching Assignment Id ", "this subject is marked");
        }
        try {
            teachingAssignmentRepository.deleteById(teachingAssignmentId);
            Map<String, Object> output = new HashMap<>();
            output.put("deleted", true);
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(output);
        } catch (Exception e) {
            e.printStackTrace();
            serviceResult.setMessage("fail");
            serviceResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<Map<String, Object>> update(TeachingAssignmentUpdate teachingAssignmentUpdate) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        Teacher teacher = teacherRepository.getById(teachingAssignmentUpdate.getTeacherId());
        try {
            Map<String, Object> output = new HashMap<>();
            TeachingAssignment update = teachingAssignmentRepository.getById(teachingAssignmentUpdate.getTeachingAssignmentId());
            List<TeachingAssignment> duplcicates = teachingAssignmentRepository.findBySubjectCodeAndClassCode(teachingAssignmentUpdate.getSubjectCode(), teachingAssignmentUpdate.getClassCode());
            TeachingAssignment duplcicate = null;
            for (TeachingAssignment duplicate : duplcicates) {
                if (!duplicate.getTeacherCode().equals(update.getTeacherCode())) {
                    duplcicate = duplicate;
                    break;
                }
            }
            if (teachingAssignmentUpdate.getSemester1() == 1) {
                if (!subjectClassRepository.findByClassCodeAndSubjectCodeSemester1(teachingAssignmentUpdate.getClassCode().trim(), teachingAssignmentUpdate.getSubjectCode().trim()).isPresent()) {
                    output.put("updated", false);
                    serviceResult.setMessage("Môn học " + teachingAssignmentUpdate.getClassCode().trim() + " chưa được cấu hình cho lớp " + teachingAssignmentUpdate.getSubjectCode().trim() + " ở học kỳ 1");
                    serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                    serviceResult.setData(output);
                    return serviceResult;
                }
            }
            if (teachingAssignmentUpdate.getSemester2() == 1) {
                if (!subjectClassRepository.findByClassCodeAndSubjectCodeSemester2(teachingAssignmentUpdate.getClassCode().trim(), teachingAssignmentUpdate.getSubjectCode().trim()).isPresent()) {
                    output.put("updated", false);
                    serviceResult.setMessage("Môn học " + teachingAssignmentUpdate.getClassCode().trim() + " chưa được cấu hình cho lớp " + teachingAssignmentUpdate.getSubjectCode().trim() + " ở học kỳ 2");
                    serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                    serviceResult.setData(output);
                    return serviceResult;                }
            }
            if (null != duplcicate) {
                if (duplcicate.getSemester1() == 1) {
                    if (teachingAssignmentUpdate.getSemester1() == 1) {
                        output.put("updated", false);
                        serviceResult.setMessage("Giáo viên " + duplcicate.getTeacherCode() + " đã được phân công giảng dạy môn " + duplcicate.getSubjectCode() + " lớp " + duplcicate.getClassCode() + " ở học kỳ 1");
                        serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                        serviceResult.setData(output);
                        return serviceResult;
                    }
                }
                if (duplcicate.getSemester2() == 1) {
                    if (teachingAssignmentUpdate.getSemester2() == 1) {
                        output.put("updated", false);
                        serviceResult.setMessage("Giáo viên " + duplcicate.getTeacherCode() + " đã được phân công giảng dạy môn " + duplcicate.getSubjectCode() + " lớp " + duplcicate.getClassCode() + " ở học kỳ 2");
                        serviceResult.setStatus(HttpStatus.BAD_REQUEST);
                        serviceResult.setData(output);
                        return serviceResult;
                    }
                }
            }

            update.setTeacherCode(teacher.getCode());
            update.setApplyAllSemester(teachingAssignmentUpdate.getApplyAllSemester());
            update.setSemester1(teachingAssignmentUpdate.getSemester1());
            update.setSemester2(teachingAssignmentUpdate.getSemester2());
            teachingAssignmentRepository.save(update);

            output.put("updated", true);
            serviceResult.setMessage("Cập nhật thành công");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(output);
        } catch (Exception e) {
            e.printStackTrace();
            serviceResult.setMessage("fail");
            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
        }
        return serviceResult;
    }

    @Transactional(rollbackFor = {TeachingAssignmentImportException.class, Exception.class})
    @Override
    public ServiceResult<Boolean> importExcel(MultipartFile file, String years) throws IOException, TeachingAssignmentImportException {
        Integer semesterAmount = schoolYearRespository.getSemesterAmount(years);
//        Set<Integer> idAdded = new HashSet<>();
        if (isExcelFormat(file)) {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            int noOfColumns = sheet.getRow(3).getPhysicalNumberOfCells();
            if (noOfColumns - 2 != semesterAmount) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Số lượng học kỳ trong file đang sai, năm học đang có " + semesterAmount + " học kỳ", null);
            }
            // Get all rows
            Iterator<Row> rows = sheet.iterator();
            int rowNumber = 0;
            String String00 = sheet.getRow(0).getCell(0).getStringCellValue();
            if (!String00.equals("DANH SÁCH PHÂN CÔNG GIẢNG DẠY")) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Nội dung file tại ô A1 đang sai", null);
            }
            String yearInExcel = sheet.getRow(1).getCell(0).getStringCellValue().substring(10, 19);
            if (!yearInExcel.equals(years)) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "năm học trong file đang sai, năm học đúng là " + years, null);
            }
            String String31 = sheet.getRow(3).getCell(1).getStringCellValue();
            if (!String31.equals("Giáo viên giảng dạy")) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Nội dung file tại ô B4 đang sai", null);
            }
            if (sheet.getRow(3).getCell(2) == null || sheet.getRow(3).getCell(2).getCellTypeEnum() == CellType.BLANK) {// khong nhap diem thi nhay sang o tiepư
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu cột Phân công Giảng dạy Học kỳ I", null);
            }
            if (semesterAmount == 2) {
                if (sheet.getRow(3).getCell(3) == null || sheet.getRow(3).getCell(2).getCellTypeEnum() == CellType.BLANK) {// khong nhap diem thi nhay sang o tiepư
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu cột Phân công Giảng dạy Học kỳ II", null);
                }
            }
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                Iterator<Cell> cellsInRow = currentRow.iterator();
                String semester1 = null;
                String semester2 = null;
                String teacherCode = null;
                String teacherName = null;

                if (rowNumber < 3) {
                    rowNumber++;
                    continue;
                }
                int cellIdx = 0;
                if (rowNumber >= 3 && rowNumber <= sheet.getPhysicalNumberOfRows() - 1) {
                    while (cellsInRow.hasNext()) {
                        Cell currentCell = cellsInRow.next();
                        if (cellIdx == 1) {
                            String teacherCodeTeacherName = currentCell.getStringCellValue();
                            if (teacherCodeTeacherName.equals("")) {
                                throw new TeachingAssignmentImportException("Cần nhập đầy đủ dữ liệu cho các cột đã tạo ra");
                            }
                            String[] s = teacherCodeTeacherName.split("\\-");
                            teacherCode = s[0];
                            teacherName = s[1].trim();
                            if (teacherRepository.findByCode(teacherCode).size() == 0) {
                                throw new TeachingAssignmentImportException("Mã giáo viên " + teacherCode + " ở dòng " + (rowNumber + 2) + " không tồn tại");
                            } else {
                                if (!teacherRepository.findByCode(teacherCode).get(0).getFullName().equals(teacherName)) {
                                    throw new TeachingAssignmentImportException("Tên giáo viên: " + teacherName + " đang không đúng với mã giáo viên: " + teacherCode + " ở dòng " + (rowNumber + 2));
                                }
                            }
                        }
                        if (cellIdx == 2) {
                            semester1 = currentCell.getStringCellValue();
                        }
                        if (semesterAmount == 2) {
                            if (cellIdx == 3) {

                                semester2 = currentCell.getStringCellValue();
                            }
                        } else {
                            semester2 = null;
                        }
                        cellIdx++;
                    }
                }
                ServiceResult<Boolean> result = updateDataImport(teacherCode, semester1, semester2, years, rowNumber + 2);
                if (result.getStatus() == HttpStatus.BAD_REQUEST) {
                    return result;
                }
                rowNumber++;
            }
        } else {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "File không đúng định dạng", null);
        }
        return new ServiceResult<>(HttpStatus.OK, "Import file thành công", null);
    }

    public ServiceResult<Boolean> updateDataImport(String teacherCode, String semester1, String semester2, String year, Integer rowNumber) throws TeachingAssignmentImportException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        List<SchoolYear> schoolYear = schoolYearRespository.getSchoolYearByYearsOrderBySemesterAsc(year);
        if (!semester1.trim().isEmpty()) {
            if (!semester1.endsWith(")")) {
                throw new TeachingAssignmentImportException("Dòng " + rowNumber + "cột Phân công Giảng dạy Học kỳ I đang nhập sai định dạng");
                //         return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + rowNumber + "cột Phân công Giảng dạy Học kỳ I đang nhập sai định dạng", null);
            }
            String[] lstSemester1 = semester1.split("\\+");//Toan(6A3,7A3)+HH(7A2,8A2)+HH(7A2,8A2)
            for (String a : lstSemester1) {//lstSemester1=["Toan(6A3,7A3)","HH(7A2,8A2)","HH(7A2,8A2)"]   a=Toan(6A3,7A3)
                List<String> lstSubject1 = new ArrayList<>();
                String[] b = a.split("\\(");
                lstSubject1.add(b[0]); //add Toan vao lstSubject1
                String[] classCode = b[1].split("\\,");   //b[1]=6A3,7A3)
                if (!subjectsRepository.isExistForSubjectByCode(lstSubject1.get(0).trim())) {
                    // return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + rowNumber + ", môn học : " + lstSubject1.get(0).trim() + " không tồn tại", null);
                    throw new TeachingAssignmentImportException("Dòng " + rowNumber + ", môn học : " + lstSubject1.get(0).trim() + " không tồn tại");
                }
                for (String d : classCode) {// vong for cho classCode classCode =["6A3","7A3)"]
                    List<String> lstClass1 = new ArrayList<>();
                    int idx = d.lastIndexOf(")");
                    if (idx > 0) {
                        if (!classRoomRepository.isExistForClassRoomByCode(d.substring(0, idx))) {
                            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + rowNumber + ", lớp học : " + d.substring(0, idx) + " không tồn tại", null);
                        }
                        lstClass1.add(d.substring(0, idx));
                    } else {
                        lstClass1.add(d);
                    }
                    if (!subjectClassRepository.findByClassCodeAndSubjectCodeSemester1(lstClass1.get(0).trim(), lstSubject1.get(0).trim()).isPresent()) {
                        System.out.println("check cho hk1");
                        throw new TeachingAssignmentImportException("Dòng " + rowNumber + ", môn học " + lstSubject1.get(0) + " chưa được cấu hình cho lớp " + lstClass1.get(0) + " ở học kỳ 1");

                        //   return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + rowNumber + ", môn học " + lstSubject1.get(0) + " chưa được cấu hình cho lớp " + lstClass1.get(0) + " ở học kỳ 1", null);
                    }
                    if (!teacherRepository.isExistBySubjectCodeAndTeacherCode(teacherCode.trim(), lstSubject1.get(0))) {
                        throw new TeachingAssignmentImportException("Dòng " + rowNumber + ", giáo viên " + teacherCode.trim() + " không thuộc khoa ban của môn học " + lstSubject1.get(0));

                        //      return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + rowNumber + ", giáo viên " + teacherCode.trim() + " không thuộc khoa ban của môn học " + lstSubject1.get(0), null);
                    }
                    TeachingAssignment teachingAssignment = new TeachingAssignment();
                    teachingAssignment.setTeacherCode(teacherCode.trim());
                    teachingAssignment.setYear(year);
                    teachingAssignment.setSubjectCode(lstSubject1.get(0).trim());
                    teachingAssignment.setClassCode(lstClass1.get(0).trim());
                    teachingAssignment.setSemester1(1);
                    teachingAssignment.setSemester2(0);
                    teachingAssignment.setApplyAllSemester(0);
                    List<TeachingAssignment> check = teachingAssignmentRepository.findBySubjectCodeAndClassCode(lstSubject1.get(0).trim(), lstClass1.get(0).trim());
                    if (!check.isEmpty()) {
                        if (teachingAssignment.getTeacherCode().equals(check.get(0).getTeacherCode())) {
                            teachingAssignment.setId(check.get(0).getId());
                            teachingAssignment.setCode(check.get(0).getCode());

                            if (check.get(0).getSemester2() != null && check.get(0).getSemester2() != 0) {
                                teachingAssignment.setSemester2(check.get(0).getSemester2());
                                teachingAssignment.setApplyAllSemester(1);
                            }
                            if (schoolYear.get(0).getSemesterAmount() == 1) {
                                teachingAssignment.setApplyAllSemester(1);
                            }
                            teachingAssignment.setCreateDate(check.get(0).getCreateDate());
                            teachingAssignment.setCreator(check.get(0).getCreator());
                            teachingAssignment.setUpdateDate(LocalDate.now());
                            teachingAssignment.setUpdater(username);
                        } else {
                            if (check.get(0).getSemester1() == 0) {
                                teachingAssignment.setCode("ts_" + new Date().getTime());
                                teachingAssignment.setCreateDate(LocalDate.now());
                                teachingAssignment.setCreator(username);
                            }
                            if (check.get(0).getSemester1() == 1) {
                                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + rowNumber + ", môn học " + check.get(0).getSubjectCode() + " lớp " + check.get(0).getClassCode() + " đã được phân công cho giáo viên " + check.get(0).getTeacherCode() + " ở học kỳ 1", null);
                            }
                        }
                    }
                    if (check.isEmpty()) {
                        if (schoolYear.get(0).getSemesterAmount() == 1)
                            teachingAssignment.setApplyAllSemester(1);
                        teachingAssignment.setCode("ts_" + new Date().getTime());
                        teachingAssignment.setCreateDate(LocalDate.now());
                        teachingAssignment.setCreator(username);
                    }
                    teachingAssignmentRepository.save(teachingAssignment);
                }
            }
        }
        if (schoolYearRespository.getSemesterAmount(year) == 2) {
            if (!semester2.trim().isEmpty()) {
                if (!semester2.endsWith(")")) {
                    throw new TeachingAssignmentImportException("Dòng " + rowNumber + "cột Phân công Giảng dạy Học kỳ II đang nhập sai định dạng");
                    //  return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + rowNumber + "cột Phân công Giảng dạy Học kỳ II đang nhập sai định dạng", null);
                }
                String[] lstSemester2 = semester2.split("\\+");
                for (String a : lstSemester2) {
                    List<String> lstSubject2 = new ArrayList<>();

                    String[] b = a.split("\\(");
                    lstSubject2.add(b[0]);
                    String[] classCode = b[1].split("\\,");
                    if (!subjectsRepository.isExistForSubjectByCode(lstSubject2.get(0).trim())) {
                        throw new TeachingAssignmentImportException("Dòng " + rowNumber + ", môn học : " + lstSubject2.get(0).trim() + " không tồn tại");
                        //return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + rowNumber + ", môn học : " + lstSubject2.get(0).trim() + " không tồn tại", null);
                    }
                    for (String d : classCode) {
                        List<String> lstClass2 = new ArrayList<>();
                        int idx = d.lastIndexOf(")");
                        if (idx > 0) {
                            if (!classRoomRepository.isExistForClassRoomByCode(d.substring(0, idx))) {
                                throw new TeachingAssignmentImportException("Dòng " + rowNumber + ", lớp học : " + d.substring(0, idx) + " không tồn tại");
                                // return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + rowNumber + ", lớp học : " + d.substring(0, idx) + " không tồn tại", null);
                            }
                            lstClass2.add(d.substring(0, idx));
                        } else {
                            lstClass2.add(d);
                        }
                        if (!subjectClassRepository.findByClassCodeAndSubjectCodeSemester2(lstClass2.get(0).trim(), lstSubject2.get(0).trim()).isPresent()) {
                            System.out.println("check cho hk2");
                            throw new TeachingAssignmentImportException("Dòng " + rowNumber + ", môn học " + lstSubject2.get(0) + " chưa được cấu hình cho lớp " + lstClass2.get(0) + " ở học kỳ 2");
                            //  return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + rowNumber + ", môn học " + lstSubject2.get(0) + " chưa được cấu hình cho lớp " + lstClass2.get(0) + " ở học kỳ 2", null);
                        }
                        if (!teacherRepository.isExistBySubjectCodeAndTeacherCode(teacherCode.trim(), lstSubject2.get(0))) {
                            throw new TeachingAssignmentImportException("Dòng " + rowNumber + ", giáo viên " + teacherCode.trim() + " không thuộc khoa ban của môn học ");
                            //return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + rowNumber + ", giáo viên " + teacherCode.trim() + " không thuộc khoa ban của môn học " + lstSubject2.get(0), null);
                        }
                        TeachingAssignment teachingAssignment = new TeachingAssignment();
                        teachingAssignment.setTeacherCode(teacherCode.trim());
                        teachingAssignment.setYear(year);
                        teachingAssignment.setSubjectCode(lstSubject2.get(0).trim());
                        teachingAssignment.setClassCode(lstClass2.get(0).trim());
                        teachingAssignment.setSemester2(1);
                        teachingAssignment.setSemester1(0);
                        teachingAssignment.setApplyAllSemester(0);
                        List<TeachingAssignment> check = teachingAssignmentRepository.findBySubjectCodeAndClassCode(lstSubject2.get(0).trim(), lstClass2.get(0).trim());
                        if (!check.isEmpty()) {
                            if (teachingAssignment.getTeacherCode().equals(check.get(0).getTeacherCode())) {
                                teachingAssignment.setId(check.get(0).getId());
                                teachingAssignment.setCode(check.get(0).getCode());

                                if (check.get(0).getSemester1() != null && check.get(0).getSemester1() != 0) {
                                    teachingAssignment.setSemester1(check.get(0).getSemester1());
                                    teachingAssignment.setApplyAllSemester(1);
                                }
                                teachingAssignment.setCreateDate(check.get(0).getCreateDate());
                                teachingAssignment.setCreator(check.get(0).getCreator());
                                teachingAssignment.setUpdateDate(LocalDate.now());
                                teachingAssignment.setUpdater(username);
                            } else {
                                if (check.get(0).getSemester2() == 0) {
                                    teachingAssignment.setCode("ts_" + new Date().getTime());
                                    teachingAssignment.setCreateDate(LocalDate.now());
                                    teachingAssignment.setCreator(username);
                                }
                                if (check.get(0).getSemester2() == 1) {
                                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Dòng " + rowNumber + ", môn học " + check.get(0).getSubjectCode() + " lớp " + check.get(0).getClassCode() + " đã được phân công cho giáo viên " + check.get(0).getTeacherCode() + " ở học kỳ 2", null);
                                }
                            }
                        }
                        if (check.isEmpty()) {
                            teachingAssignment.setCode("ts_" + new Date().getTime());
                            teachingAssignment.setCreateDate(LocalDate.now());
                            teachingAssignment.setCreator(username);
                        }
                        teachingAssignmentRepository.save(teachingAssignment);
                    }
                }
            }
        }
        return new ServiceResult<>(HttpStatus.OK, "Cập nhật phân công giảng dạy thành công", null);
    }

    public Boolean isExcelFormat(MultipartFile file) {
        return file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    @Override
    public ByteArrayInputStream exportExcel(String year) {
        ByteArrayInputStream in = export(year);
        return in;
    }

    public ByteArrayInputStream export(String years) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet teachingAssignmentSheet = workbook.createSheet("teachingAssignment");

            CellStyle commonStyle = workbook.createCellStyle();
            commonStyle.setAlignment(HorizontalAlignment.CENTER);
            commonStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            teachingAssignmentSheet.createRow(0).createCell(0).setCellValue("DANH SÁCH PHÂN CÔNG GIẢNG DẠY");
            teachingAssignmentSheet.getRow(0).getCell(0).setCellStyle(commonStyle);
            teachingAssignmentSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

            teachingAssignmentSheet.createRow(1).createCell(0).setCellValue("Năm học : " + years);
            teachingAssignmentSheet.getRow(1).getCell(0).setCellStyle(commonStyle);
            teachingAssignmentSheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
            teachingAssignmentSheet.createRow(2);
            //row 3
            teachingAssignmentSheet.createRow(3);
            teachingAssignmentSheet.getRow(3).createCell(0).setCellValue("STT");
            teachingAssignmentSheet.getRow(3).createCell(1).setCellValue("Giáo viên giảng dạy");
            teachingAssignmentSheet.getRow(3).createCell(2).setCellValue("Phân công Giảng dạy Học kỳ I");
            Integer semesterAmount = schoolYearRespository.getSemesterAmount(years);
            if (semesterAmount == 2) {
                teachingAssignmentSheet.getRow(3).createCell(3).setCellValue("Phân công Giảng dạy Học kỳ II");
                teachingAssignmentSheet.getRow(3).getCell(3).setCellStyle(commonStyle);
            }
            teachingAssignmentSheet.getRow(3).getCell(0).setCellStyle(commonStyle);
            teachingAssignmentSheet.getRow(3).getCell(1).setCellStyle(commonStyle);
            teachingAssignmentSheet.getRow(3).getCell(2).setCellStyle(commonStyle);

            //row 4
            teachingAssignmentSheet.createRow(4);
            teachingAssignmentSheet.getRow(4).createCell(0).setCellValue("1");
            teachingAssignmentSheet.getRow(4).getCell(0).setCellStyle(commonStyle);
            teachingAssignmentSheet.getRow(4).createCell(1);
            teachingAssignmentSheet.getRow(4).createCell(2);
            teachingAssignmentSheet.getRow(4).createCell(3);
            teachingAssignmentSheet.getRow(4).getCell(2).setCellValue("AN9(L1B1)+KT9(L1C1)");
            if (semesterAmount == 2) {
                teachingAssignmentSheet.getRow(4).getCell(3).setCellValue("AN9(L1B1)+KT9(L1C1)");
            }
            //row 5
            for (int i = 5; i < 20; i++) {
                teachingAssignmentSheet.createRow(i);
                teachingAssignmentSheet.getRow(i).createCell(0).setCellValue(i - 3);
                teachingAssignmentSheet.getRow(i).createCell(1);
                teachingAssignmentSheet.getRow(i).createCell(2);
                teachingAssignmentSheet.getRow(i).createCell(3);
                teachingAssignmentSheet.getRow(i).getCell(0).setCellStyle(commonStyle);
            }

            List<Map<String, Object>> teacherList = teacherRepository.getAllTeacher();
            String[] selectedValues = new String[teacherList.size()];
            Sheet hidden = workbook.createSheet("hidden");

            for (int x = 0; x < teacherList.size(); x++) {
                selectedValues[x] = teacherList.get(x).get("code").toString() + " - " + teacherList.get(x).get("full_name").toString();
            }
            for (int i = 0; i < teacherList.size(); i++) {
                String name = selectedValues[i];
                Row row = hidden.createRow(i);
                Cell cell = row.createCell(0);
                cell.setCellValue(name);
            }
            DataValidation dataValidation = null;
            DataValidationConstraint constraint = null;
            DataValidationHelper validationHelper = null;

            validationHelper = new XSSFDataValidationHelper((XSSFSheet) teachingAssignmentSheet);
            CellRangeAddressList addressList = new CellRangeAddressList(4, 19, 1, 1);

            constraint = validationHelper.createFormulaListConstraint("hidden!$A$1:$A$" + selectedValues.length);
            dataValidation = validationHelper.createValidation(constraint, addressList);
            dataValidation.setSuppressDropDownArrow(true);
            teachingAssignmentSheet.addValidationData(dataValidation);
            workbook.setSheetHidden(1, true);
            if (semesterAmount == 2) {
                for (int j = 0; j < 4; j++) {
                    teachingAssignmentSheet.autoSizeColumn(j);
                }
            } else {
                for (int j = 0; j < 3; j++) {
                    teachingAssignmentSheet.autoSizeColumn(j);
                }
            }
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setTopBorderColor(IndexedColors.BLACK.index);
            cellStyle.setRightBorderColor(IndexedColors.BLACK.index);
            cellStyle.setBottomBorderColor(IndexedColors.BLACK.index);
            cellStyle.setLeftBorderColor(IndexedColors.BLACK.index);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);

            for (int i = 3; i < 20; i++) {
                if (semesterAmount == 2) {
                    for (int j = 0; j < 4; j++) {
                        teachingAssignmentSheet.getRow(i).getCell(j).setCellStyle(cellStyle);
                    }
                } else {
                    for (int j = 0; j < 3; j++) {
                        teachingAssignmentSheet.getRow(i).getCell(j).setCellStyle(cellStyle);
                    }
                }
            }
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            teachingAssignmentSheet.getRow(3).getCell(0).setCellStyle(cellStyle);
            teachingAssignmentSheet.getRow(3).getCell(1).setCellStyle(cellStyle);
            teachingAssignmentSheet.getRow(3).getCell(2).setCellStyle(cellStyle);
            if (semesterAmount == 2) {
                teachingAssignmentSheet.getRow(3).getCell(3).setCellStyle(cellStyle);
            }
            teachingAssignmentSheet.setColumnWidth(1, 40 * 256);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();

            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ValidationException("file", "Không thể export file ");
        }
    }

    @Override
    public ServiceResult<Map<String, Object>> getClassSubjectByYearAndSemesterAndTeacherCode(String teacherCode, String year, Integer semester) {
        ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
        try {
            List<Map<String, Object>> classCodeSubjectCode = teachingAssignmentRepository.getClassSubjectByYearAndSemesterAndTeacherCode(teacherCode, year, semester);
            Map<String, Object> output = new HashMap<>();
            output.put("classCodeSubjectCode", classCodeSubjectCode);
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
            serviceResult.setData(output);
        } catch (Exception e) {
            e.printStackTrace();
            serviceResult.setMessage("fail");
            serviceResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return serviceResult;
    }

}
