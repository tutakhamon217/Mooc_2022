package fpt.capstone.helper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.DoubleAccumulator;
import java.util.stream.Stream;
import fpt.capstone.repository.*;

import fpt.capstone.form_data.TimetableForm;
import fpt.capstone.payroll.FileParserException;
import fpt.capstone.payroll.ValidationException;
import fpt.capstone.repository.ClassRoomRepository;
import fpt.capstone.repository.SubjectClassRepository;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import fpt.capstone.vo.DropDownVo;
import fpt.capstone.vo.TimeTableVoV2;
import fpt.capstone.vo.TimetableExcel;
import fpt.capstone.vo.UploadTimetableVO;

public class TimetableExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static String sheetName = "timetable";
    public static String[] DAYS_OF_WEEK = {
            "Thứ 2",
            "Thứ 3",
            "Thứ 4",
            "Thứ 5",
            "Thứ 6",
            "Thứ 7"
    };
    public static int LAST_ROW = 1 << 20 - 1;

    public static ByteArrayInputStream export(String years, String semester, String className, String applyDate,
            List<TimeTableVoV2> timetable) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet timetableSheet = workbook.createSheet(sheetName);

            // Thời khóa biểu - Lớp :className
            CellStyle commonStyle = workbook.createCellStyle();
            commonStyle.setAlignment(HorizontalAlignment.CENTER);
            commonStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            timetableSheet.createRow(0).createCell(0).setCellValue("Thời khóa biểu - Lớp " + className);
            timetableSheet.getRow(0).getCell(0).setCellStyle(commonStyle);
            timetableSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));

            // Học kỳ :semester - Năm học :years (ngày áp dụng :applyDate)
            String applyDateText = null;
            try {
                applyDateText = timetable.get(0).getApplyDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeException e) {
                workbook.close();
                throw new ValidationException("applyDate", "Invalid applyDate");
            }
            timetableSheet.createRow(1).createCell(0).setCellValue(
                    "Học kỳ " + semester + " - Năm học " + years + "(ngày áp dụng " + applyDateText + ")");
            timetableSheet.getRow(1).getCell(0).setCellStyle(commonStyle);
            timetableSheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
            // Headers

            for (int row = 3; row <= 34; ++row) {
                timetableSheet.createRow(row);
                for (int col = 0; col <= 5; ++col) {
                    timetableSheet.getRow(row).createCell(col);
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
                    timetableSheet.getRow(row).getCell(col).setCellStyle(cellStyle);
                }
            }

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.cloneStyleFrom(commonStyle);

            timetableSheet.getRow(3).getCell(0).setCellValue("Thứ");

            timetableSheet.addMergedRegion(new CellRangeAddress(3, 4, 0, 0));

            timetableSheet.getRow(3).getCell(1).setCellValue("Tiết");
            timetableSheet.addMergedRegion(new CellRangeAddress(3, 4, 1, 1));

            timetableSheet.getRow(3).getCell(2).setCellValue("Buổi sáng");
            timetableSheet.addMergedRegion(new CellRangeAddress(3, 3, 2, 3));

            timetableSheet.getRow(3).getCell(4).setCellValue("Buổi chiều");
            timetableSheet.addMergedRegion(new CellRangeAddress(3, 3, 4, 5));

            timetableSheet.getRow(4).getCell(2).setCellValue("Tên môn học");
            timetableSheet.getRow(4).getCell(3).setCellValue("Giáo viên giảng dạy");
            timetableSheet.getRow(4).getCell(4).setCellValue("Tên môn học");
            timetableSheet.getRow(4).getCell(5).setCellValue("Giáo viên giảng dạy");

            // create timetable
            for (int weekDay = 2; weekDay <= 7; ++weekDay) {
                for (int lession = 1; lession <= 5; ++lession) {
                    int row = 4 + (weekDay - 2) * 5 + lession;

                    timetableSheet.getRow(row).getCell(1).setCellValue(lession);
                    timetableSheet.getRow(row).getCell(2).setCellValue("-");
                    timetableSheet.getRow(row).getCell(3).setCellValue("-");
                    timetableSheet.getRow(row).getCell(4).setCellValue("-");
                    timetableSheet.getRow(row).getCell(5).setCellValue("-");

                }

                int weekDayRow = 5 + (weekDay - 2) * 5;
                timetableSheet.getRow(weekDayRow).getCell(0).setCellValue("Thứ " +
                        weekDay);
                timetableSheet.addMergedRegion(new CellRangeAddress(weekDayRow, weekDayRow +
                        4, 0, 0));
            }

            // // Load subject
            // for (TimetableExcel tb : timetable) {
            // int weekDay = Integer.parseInt(tb.getWeekDay().substring(1)); // remove t in
            // weekday
            // int row = 4 + (weekDay - 2) * 5 + Integer.parseInt(tb.getLesson());
            // int col = tb.getType().equalsIgnoreCase("0") ? 2 : 4;

            // timetableSheet.getRow(row).getCell(col)
            // .setCellValue(tb.getSubject().getCode() + " - " + tb.getSubject().getName());
            // timetableSheet.getRow(row).getCell(col + 1)
            // .setCellValue(tb.getTeacher().getCode() + " - " + tb.getTeacher().getName());
            // }

            for (TimeTableVoV2 tb : timetable) {
                if (tb.getSubject().getId() == null) {
                    continue;
                }

                int weekDay = Integer.parseInt(tb.getWeekDay().substring(1));
                int row = 4 + (weekDay - 2) * 5 + Integer.parseInt(tb.getLesson());
                int col = tb.getType().equalsIgnoreCase("0") ? 2 : 4;

                timetableSheet.getRow(row).getCell(col)
                        .setCellValue(tb.getSubject().getCode() + " - " + tb.getSubject().getName());
                timetableSheet.getRow(row).getCell(col + 1)
                        .setCellValue(tb.getTeacher().getCode() + " - " + tb.getTeacher().getName());
            }

            for (int cellIndex = 0; cellIndex < 6; ++cellIndex)
                timetableSheet.autoSizeColumn(cellIndex);

            timetableSheet.setColumnWidth(1, 256 * 5);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();

            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new ValidationException("file", "Không thể xuất file");
        }
    }

    public static ByteArrayInputStream downloadTemplateFile(String years, String semester, String gradeLevel,
            List<DropDownVo> lstClass) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet timetableSheet = workbook.createSheet(sheetName);

            for (int row = 0; row <= 1; ++row) {
                timetableSheet.createRow(row);
                for (int col = 0; col <= 12; ++col) {
                    timetableSheet.getRow(row).createCell(col);
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
                    timetableSheet.getRow(row).getCell(col).setCellStyle(cellStyle);
                }
            }

            timetableSheet.getRow(0).getCell(0).setCellValue("STT");
            timetableSheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));

            timetableSheet.getRow(0).getCell(1).setCellValue("Mã lớp(*)");
            timetableSheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));

            timetableSheet.getRow(0).getCell(2).setCellValue("Thứ(*)");
            timetableSheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));

            timetableSheet.getRow(0).getCell(3).setCellValue("Buổi sáng (Nhập mã môn học)");
            timetableSheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 7));

            timetableSheet.getRow(0).getCell(8).setCellValue("Buổi chiều (Nhập mã môn học)");
            timetableSheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 12));

            for (int lession = 1; lession <= 5; ++lession) {
                timetableSheet.getRow(1).getCell(2 + lession).setCellValue("Tiết " + String.valueOf(lession));
                timetableSheet.getRow(1).getCell(7 + lession).setCellValue("Tiết " + String.valueOf(lession));
            }

            DataValidationHelper dayOfWeekValidationHelper = new XSSFDataValidationHelper((XSSFSheet) timetableSheet);
            CellRangeAddressList addressList = new CellRangeAddressList(2, LAST_ROW, 2, 2);
            DataValidationConstraint dayOfWeekConstraint = dayOfWeekValidationHelper
                    .createExplicitListConstraint(DAYS_OF_WEEK);
            DataValidation dayOfWeekDataValidation = dayOfWeekValidationHelper.createValidation(dayOfWeekConstraint,
                    addressList);
            dayOfWeekDataValidation.setSuppressDropDownArrow(true);
            timetableSheet.addValidationData(dayOfWeekDataValidation);

            String[] classCodes = new String[lstClass.size()];

            for (int i = 0; i < lstClass.size(); ++i) {
                classCodes[i] = lstClass.get(i).getCode();
            }

            DataValidationHelper classValidationHelper = new XSSFDataValidationHelper((XSSFSheet) timetableSheet);
            // CellRangeAddressList addressList = new CellRangeAddressList(2, LAST_ROW, 2,
            // 2);
            DataValidationConstraint classConstraint = classValidationHelper.createExplicitListConstraint(classCodes);
            DataValidation classDataValidation = classValidationHelper.createValidation(classConstraint,
                    new CellRangeAddressList(2, LAST_ROW, 1, 1));
            dayOfWeekDataValidation.setSuppressDropDownArrow(true);
            timetableSheet.addValidationData(classDataValidation);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            workbook.close();

            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new ValidationException("file", "Không thể xuất file");
        }
    }

    public static Boolean isExcelFormat(MultipartFile file) {
        return file.getContentType().equals(TYPE);
    }

    public static HashMap<String, TimetableForm> parseFromExcel(InputStream is, String years, String semester,
            Integer gradeLevel, String applyDate, ClassRoomRepository classroomRespository,
            SubjectClassRepository subjectClassRepository, SubjectsRepository subjectsRepository) {
        HashMap<String, TimetableForm> data = new HashMap();
        List<String> errors = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet timetableSheet = workbook.getSheet(sheetName);
            // System.out.println(timetableSheet.getLastRowNum());

            String[] firstHeaderValue = {
                    "STT",
                    "Mã lớp(*)",
                    "Thứ(*)",
                    "Buổi sáng (Nhập mã môn học)",
                    "",
                    "",
                    "",
                    "",
                    "Buổi chiều (Nhập mã môn học)",
                    "",
                    "",
                    "",
                    ""
            };

            Row firstHeaderRow = timetableSheet.getRow(0);
            if (firstHeaderRow == null) {
                errors.add("Dòng 0 Sai đầu đề");
                workbook.close();
                throw new FileParserException(errors);
            }

            if(firstHeaderRow.getLastCellNum() != 13)
            {
                errors.add("Dòng 0 sai đầu đề");
                workbook.close();
                throw new FileParserException(errors);
            }

            for (int i = 0; i < firstHeaderRow.getLastCellNum(); ++i) {
                // System.out.println("\'" + firstHeaderRow.getCell(i) + "\'");
                if (!firstHeaderValue[i].equals(firstHeaderRow.getCell(i).toString())) {
                    errors.add("Dòng 0 sai đầu đề");
                    workbook.close();
                    throw new FileParserException(errors);
                }
            }

            Row secondHeaderRow = timetableSheet.getRow(1);

            if (secondHeaderRow == null) {
                errors.add("Dòng 1 sai đầu đề");
                workbook.close();
                throw new FileParserException(errors);
            }

            if(secondHeaderRow.getLastCellNum() != 13)
            {
                errors.add("Dòng 1 sai đầu đề");
                workbook.close();
                throw new FileParserException(errors);
            }

            String[] secondHeaderValue = {
                "",
                "",
                "",
                "Tiết 1",
                "Tiết 2",
                "Tiết 3",
                "Tiết 4",
                "Tiết 5",
                "Tiết 1",
                "Tiết 2",
                "Tiết 3",
                "Tiết 4",
                "Tiết 5"
        };

            for (int i = 0; i < secondHeaderRow.getLastCellNum(); ++i) {
                //System.out.println("\'" + secondHeaderRow.getCell(i) + "\'");
                if (!secondHeaderValue[i].equals(secondHeaderRow.getCell(i).toString())) {
                    errors.add("Dòng 1 sai đầu đề");
                    workbook.close();
                    throw new FileParserException(errors);
                }
            }

            for (int row = 2; row <= timetableSheet.getLastRowNum(); ++row) {
                Row rowValue = timetableSheet.getRow(row);
                if (rowValue == null) {
                    // throw new ValidationException("file", String.format("(%d, ) Dòng rỗng", row +
                    // 1));
                    errors.add(String.format("Dòng %d rỗng", row + 1));
                    continue;
                }
                // System.out.println(rowValue.getLastCellNum());
                int lastCol = rowValue.getLastCellNum();
                if (lastCol < 3) {
                    errors.add(String.format("Dòng %d Thiếu dữ liệu", row + 1));
                    continue;
                }
                if (lastCol > 13) {
                    errors.add(String.format("Dòng %d thừa dữ liệu", row + 1));
                    continue;
                }

                double no = 0.0;
                try {
                    no = rowValue.getCell(0) == null ? -1.0 : rowValue.getCell(0).getNumericCellValue();
                } catch (Exception e) {
                    errors.add(String.format("(%d, %c) sai số thứ tự", row + 1, 1+'A' - 1));
                    continue;
                }

                if (Double.compare(no, row - 1) != 0) {
                    errors.add(String.format("(%d, %c) Sai số thứ tự, ở bản tính: %s, giá trị đúng: %.1f", row + 1, 1+'A' - 1,
                            rowValue.getCell(0), row * 1.0 - 1));
                    continue;
                }

                String classCode = rowValue.getCell(1) == null ? "" : rowValue.getCell(1).toString();

                if (!classroomRespository.isExist(gradeLevel, years, classCode)) {
                    errors.add(String.format("(%d, %c) Lớp học %s không hợp lệ", row + 1, 2+'A' - 1, rowValue.getCell(1)));
                    continue;
                }

                if(!data.containsKey(classCode))
                {
                    //data.put(classCode, new UploadTimetableVO(weekDay, lesson, type, subject, teacher));
                    List<UploadTimetableVO> list = new ArrayList<>();
                    data.put(classCode, new TimetableForm(years, semester, classCode, gradeLevel.toString(), applyDate, list));
                }

                String dayOfWeek = rowValue.getCell(2) == null ? "" : rowValue.getCell(2).toString();
                if (!Stream.of(DAYS_OF_WEEK).anyMatch(x -> dayOfWeek.trim().equals(x))) {
                    errors.add(String.format("(%d, %c) %s không phải là thứ trong tuần hợp lệ", row + 1, 3+'A' - 1,
                            rowValue.getCell(2)));
                    continue;
                }

                String dow = "t" + dayOfWeek.substring(dayOfWeek.length() - 1);

                int lession = 1;
                boolean type = true;
                for (int i = 3; i <= 12; ++i) {
                    String subjectCode = (rowValue.getCell(i) == null
                            || rowValue.getCell(i).toString().trim().length() == 0) ? null
                                    : rowValue.getCell(i).toString();

                    // System.out.println(subjectCode);
                    if (subjectCode != null
                            && !subjectClassRepository.isValidForTimetable(years, semester, classCode, subjectCode)) {
                        errors.add(
                                String.format("(%d, %c) Môn học %s không hợp lệ", row + 1, i + 1+'A' - 1, rowValue.getCell(i)));
                        continue;
                    }

                    Integer subjectId = subjectCode == null ? null : subjectsRepository.findByCodeOptional(subjectCode).orElse(null).getId();
                    if(lession == 1 && type == false)
                    {
                        System.out.println("subject ID = " + subjectId);
                    }
                    data.get(classCode).getTimetable().add(new UploadTimetableVO(dow, String.valueOf(lession), type ? "0" : "1" , subjectId , null));
                    ++lession;
                    if(lession == 6)
                    {
                        lession = 1;
                        type = false;
                    }
                }



            }


            workbook.close();

        } catch (ValidationException ex) {
            throw ex;
        } catch(FileParserException exception) {
            throw exception;
        } 
        catch (Exception e) {
            throw new ValidationException("file", "Lỗi khi đọc file");
        }

        if (errors.size() > 0) {
            throw new FileParserException(errors);
        }

        return data;
    }
}
