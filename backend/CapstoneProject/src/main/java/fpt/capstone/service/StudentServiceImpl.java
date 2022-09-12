package fpt.capstone.service;

import fpt.capstone.repository.*;
import fpt.capstone.vo.StudentVo;
import fpt.capstone.vo.StudentDetailInformationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.entities.Student;
import fpt.capstone.entities.StudentHistory;
import fpt.capstone.entities.User;
import fpt.capstone.entities.Contact;
import fpt.capstone.entities.ClassRoom;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import fpt.capstone.form_data.StudentForm;

import fpt.capstone.payroll.ValidationException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fpt.capstone.security.services.UserDetailsImpl;
import fpt.capstone.utility.PhoneCheck;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;

import org.apache.commons.codec.binary.Base64;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository repository;

    @Autowired
    private ContactRepository repositoryContact;

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Autowired
    private StudentHistoryRepository repositoryStudentHistory;

    @Autowired
    private StudentRepository repositoryStudent;

    @Autowired
    private UserRepository repositoryUser;

    @Autowired
    private UserService userService;

    @Autowired
    private GradebookSubjectsDetailsRepository gradebookSubjectsDetailsRepository;

    @Autowired
    private GradebookRepository gradebookRepository;

    private String dsHocSinhTemplate = "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";

    @Override
    public ByteArrayInputStream exportTemplateExcel() {
        byte[] result;
        try {
            result = Base64.decodeBase64(dsHocSinhTemplate);
            // result = Files.readAllBytes(Paths.get("src/main/resources/importTemplate/DS_HocSinh.xlsx"));
            ByteArrayInputStream in = new ByteArrayInputStream(result);
            return in;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        
        return null;
    }

    public Boolean isExcelFormat(MultipartFile file) {
        return file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public Boolean isNumber(String tmp) {
        try {
            Integer.parseInt(tmp);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Boolean isEmpty(String tmp) {
        if (tmp.trim().isEmpty() || tmp == null) {
            return true;
        } else {
            return false;
        }
    }


    //  [...]
    public Boolean isInrange(Integer start, Integer end, Integer value) {
        if ( value >= start && value <= end) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isPhoneNumber(String tmp) {
        if (!isNumber(tmp)) {
            return false;
        }
        boolean check = false;
        for (String phone : PhoneCheck.phoneHead) {
            if (tmp.startsWith(phone) && tmp.length() == 10) {
                check = true;
                break;
            }
        }
        return check;
    }

    @Override
    public ServiceResult<List<String>> importExcel(MultipartFile file, String year) throws IOException {
        if (isExcelFormat(file)) {
            Integer countSuccess = 0;
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            // Get all rows
            Iterator<Row> rows = sheet.iterator();
            int rowNumber = 0;

            Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();
            result.put("student", new ArrayList<Integer>());
            result.put("contact", new ArrayList<Integer>());
            result.put("studenthistory", new ArrayList<Integer>());
            result.put("user", new ArrayList<Integer>());

            int processRow = 0;

            try {
                
                while (rows.hasNext()) {
                    Row currentRow = rows.next();
                    String code = "";
                    String fullName = "";
                    Integer status = 0;
                    String phone = "";
                    String birthDay = "";
                    String nation = "";
                    String permanentAddress = "";
                    String socialInsuranceNumber = "";
                    String issuedDate ="";
                    Integer sex = null;
                    Integer deptId = null;
                    Integer trainingSystem = null;
                    String email = "";
                    String religion = "";
                    String homeTown = "";
                    String temporaryAddress = "";
                    String identityCard = "";
                    String issuedAddress = "";
                    String startDate = "";
                    Integer electFormat = null;
                    Integer graduationType = null;
                    Integer quanHe = null;
                    String hoTenPhuHuynh = "";
                    String soDienThoaiPhuHuynh = "";
                    Integer quanHeSecond = null;
                    String hoTenPhuHuynhSecond = "";
                    String soDienThoaiPhuHuynhSecond = "";
                    String createdTime = "";
                    String createdName = "";
                    String updateTime = "";
                    String updateName = "";
                    String classCode = "";

                    Boolean reachLast = false;


                    processRow = rowNumber;

                    if (rowNumber < 2) {
                        rowNumber++;
                        continue;
                    }
                    Boolean isFailed = false;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                    
                    
                    Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

                    Boolean endLine = false;
                    List<String> errorMessage = new ArrayList<String>();
                    if (rowNumber >= 2) {
                        for(int cellIdx = 0; cellIdx <= 26; cellIdx ++){
                            Cell currentCell = currentRow.getCell(cellIdx);
                            if (currentCell  == null) {
                                currentCell = currentRow.createCell(cellIdx);
                            }
                            String tmp = "";

                            if(currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                Double tmpNum = currentCell.getNumericCellValue();
                                if ((double)tmpNum.longValue()  == tmpNum ) {
                                    tmp = "" + tmpNum.longValue();
                                } else {
                                    tmp = "" + tmpNum;
                                }
                            } else if( currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                                tmp = currentCell.getStringCellValue();
                            } else if (currentCell.getCellType() == Cell.CELL_TYPE_BLANK){
                                tmp = "";
                            } else {
                                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Lỗi định dạng dữ liêu khi đọc dòng "+ (processRow+1), null);
                            }
                            if (cellIdx == 0) {
                                if (isEmpty(tmp)) {
                                    endLine = true;
                                    break;
                                }
                            }
                            
                            if (cellIdx == 1) {
                                fullName = tmp;
                                if (isEmpty(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Tên học sinh không được để trống.");
                                } else if (tmp.length() > 250) {
                                    isFailed = true;
                                    errorMessage.add("Tên học sinh không được dài quá 250 ký tự.");
                                }
                            }
                            if (cellIdx == 2) {
                                code = tmp;
                                if (isEmpty(tmp)) {
                                    isFailed = true;
                                    errorMessage.add( "Mã học sinh không được để trống.");
                                } 
                                else if (tmp.length() > 50) {
                                    isFailed = true;
                                    errorMessage.add("Mã học sinh không được dài quá 50 ký tự.");
                                }
                                else if (repository.findByCode(code).size() > 0) {
                                    isFailed = true;
                                    errorMessage.add("Mã học sinh đã tồn tại.");
                                }
                            }
                            if (cellIdx == 3) {
                                classCode = tmp;
                                deptId = null;
                                if (isEmpty(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Mã lớp học không được để trống.");
                                } else if (tmp.length() > 50) {
                                    isFailed = true;
                                    errorMessage.add("Mã lớp học không được dài quá 50 ký tự.");
                                } else {

                                    List<Map<String, Object>> dep = classRoomRepository.getDepIdOfClassroom(tmp);

                                    if (dep.size() > 0) {
                                        deptId = Integer.parseInt("" + dep.get(0).get("dept_id"));
                                    } else {
                                        deptId =  null;
                                    }
                                }
                                if (deptId == null) {
                                    isFailed = true;
                                    errorMessage.add("Mã lớp học không tồn tại.");
                                }
                            }
                            if (cellIdx == 4) {
                                if (isEmpty(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Hệ đào tạo không được để trống.");
                                } 
                                else if (!isNumber(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Hệ đào tạo phải là số.");
                                } else if (!isInrange(1, 6, Integer.parseInt(tmp))) {
                                    isFailed = true;
                                    errorMessage.add("Hệ đào tạo phải có giá trị trong khoảng 1 đến 6.");
                                } else {
                                    trainingSystem = Integer.parseInt(tmp);
                                }
                            }
                            if (cellIdx == 5) {
                                if (isEmpty(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Giới tính không được để trống.");
                                }
                                else if (!isNumber(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Giới tính phải là số.");
                                } else if (!isInrange(0, 1, Integer.parseInt(tmp))) {
                                    isFailed = true;
                                    errorMessage.add("Giới tính phải có giá trị là 0 hoặc 1.");
                                } else {
                                    sex = Integer.parseInt(tmp);
                                }
                            }
                            if (cellIdx == 6) {
                                if (isEmpty(tmp)) {
                                    startDate = null;
                                }
                                else {
                                    try{
                                        LocalDateTime dateTmp = LocalDateTime.parse(tmp + " 00:00", formatter);
                                        startDate = tmp;
                                    } catch (Exception e) {
                                        isFailed = true;
                                        errorMessage.add("Ngày vào trường phải có định dạng dd-mm-yyy hoặc ngày vào trường không tồn tại.");
                                        startDate = null;
                                    }
                                }
                            }
                            if (cellIdx == 7) {
                                if (isEmpty(tmp)) {
                                    electFormat = null;
                                }
                                else if (!isNumber(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Hình thức trúng tuyển phải là số.");
                                } else if (!isInrange(0, 1, Integer.parseInt(tmp))) {
                                    isFailed = true;
                                    errorMessage.add("Hình thức trúng tuyển phải có giá trị là 0 hoặc 1.");
                                } else {
                                    electFormat = Integer.parseInt(tmp);
                                }
                            }
                            if (cellIdx == 8) {
                                if (isEmpty(tmp)) {
                                    graduationType = null;
                                }
                                else if (!isNumber(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Loại tốt nghiệp cấp dưới phải là số.");
                                } else if (!isInrange(1, 3, Integer.parseInt(tmp))) {
                                    isFailed = true;
                                    errorMessage.add("Loại tốt nghiệp cấp dưới phải có giá trị từ 1 đến 3.");
                                } else {
                                    graduationType = Integer.parseInt(tmp);
                                }
                            }
                            if (cellIdx == 9) {
                                if (isEmpty(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Loại quan hệ liên hệ 1 không được để trống.");
                                }
                                else if (!isNumber(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Loại quan hệ liên hệ 1 phải là số.");
                                } else if (!isInrange(0, 5, Integer.parseInt(tmp))) {
                                    isFailed = true;
                                    errorMessage.add("Loại quan hệ liên hệ 1 phải có giá trị từ 0 đến 5.");
                                } else {
                                    quanHe = Integer.parseInt(tmp);
                                }
                            }
                            if (cellIdx == 10) {
                                if (isEmpty(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Họ tên phụ huynh liên hệ 1 không được để trống.");
                                } else {
                                    hoTenPhuHuynh = tmp;
                                }
                            }
                            if (cellIdx == 11) {
                                if (isEmpty(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Số điện thoại phụ huynh 1 không được để trống. ");
                                }
                                else if (!isPhoneNumber(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Số điện thoại phụ huynh 1 không đúng định dạng.");
                                } else {
                                    soDienThoaiPhuHuynh = tmp;
                                }
                            }
                            if (cellIdx == 12) {
                                if (isEmpty(tmp)) {
                                    quanHeSecond = null;
                                }
                                else if (!isNumber(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Loại quan hệ liên hệ 2 phải là số.");
                                } else if (!isInrange(0, 5, Integer.parseInt(tmp))) {
                                    isFailed = true;
                                    errorMessage.add("Loại quan hệ liên hệ 2 phải có giá trị từ 0 đến 5.");
                                } else {
                                    quanHeSecond = Integer.parseInt(tmp);
                                }
                            }
                            if (cellIdx == 13) {
                                hoTenPhuHuynhSecond = tmp;
                            }
                            if (cellIdx == 14) {
                                if (isEmpty(tmp)) {
                                    soDienThoaiPhuHuynhSecond = "";
                                }
                                else if (!isPhoneNumber(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Số điện thoại phụ huynh 2 không đúng định dạng.");
                                } else {
                                    soDienThoaiPhuHuynhSecond = tmp;
                                }
                            }
                            if (cellIdx == 15) {
                                if (isEmpty(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Số điện thoại không được để trống.");
                                }
                                else if (!isPhoneNumber(tmp)) {
                                    isFailed = true;
                                    errorMessage.add("Số điện thoại không đúng định dạng.");
                                } else {
                                    phone = tmp;
                                }
                            } 
                            if (cellIdx == 16) {
                                if (isEmpty(tmp)) {
                                    email = "";
                                }
                                else {
                                    Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(tmp);
                                    if (matcher.find()) {
                                        email =  tmp;
                                    } else if (tmp.length() > 250) {
                                        isFailed = true;
                                        errorMessage.add("Email không quá 250 ký tự.");                
                                    }
                                    else {
                                        isFailed = true;
                                        errorMessage.add("Email có định dạng không hợp lệ.");       
                                    }
                                }
                            }
                            if (cellIdx == 17) {
                                if (isEmpty(tmp)) {
                                    birthDay = null;
                                }
                                else {
                                    try{
                                        LocalDateTime dateTmp = LocalDateTime.parse(tmp + " 00:00", formatter);
                                        birthDay = tmp;
                                    } catch (Exception e) {
                                        isFailed = true;
                                        errorMessage.add("Ngày sinh phải có định dạng dd-mm-yyy hoặc ngày sinh không tồn tại.");
                                        birthDay = null;
                                    }
                                }
                            }
                            if (cellIdx == 18) {
                                religion = tmp;
                                if (tmp.length() > 250) {
                                    isFailed = true;
                                    errorMessage.add("Tôn giáo không được quá 250 ký tự.");
                                } else {
                                    religion = tmp;
                                }
                            }
                            if (cellIdx == 19) {                            
                                if (tmp.length() > 250) {
                                    isFailed = true;
                                    errorMessage.add("Dân tộc không được quá 250 ký tự.");
                                } else {
                                    nation = tmp;
                                }
                            }
                            if (cellIdx == 20) {
                                if (tmp.length() > 250) {
                                    isFailed = true;
                                    errorMessage.add("Quê quán không được quá 250 ký tự.");
                                } else {
                                    homeTown = tmp;
                                }
                            }
                            if (cellIdx == 21) {
                                if (tmp.length() > 250) {
                                    isFailed = true;
                                    errorMessage.add("Địa chỉ thường chú không được quá 250 ký tự.");
                                } else {
                                    permanentAddress = tmp;
                                }
                            }
                            if (cellIdx == 22) {
                                if (tmp.length() > 250) {
                                    isFailed = true;
                                    errorMessage.add("Địa chỉ tạm chú không được quá 250 ký tự.");
                                } else {
                                    temporaryAddress = tmp;
                                }
                            }
                            if (cellIdx == 23) {
                                if (tmp.length() > 250) {
                                    isFailed = true;
                                    errorMessage.add("Số CMND/CCCD không được quá 250 ký tự.");
                                } else {
                                    identityCard = tmp;
                                }
                            }
                            if (cellIdx == 24) {
                                if (tmp.length() > 250) {
                                    isFailed = true;
                                    errorMessage.add("Nơi cấp CMND/CCCD không được quá 250 ký tự.");
                                } else {
                                    issuedAddress = tmp;
                                }
                            }
                            if (cellIdx == 25) {
                                if (isEmpty(tmp)) {
                                    issuedDate = null;
                                }
                                else {
                                    try{
                                        LocalDateTime dateTmp = LocalDateTime.parse(tmp + " 00:00", formatter);
                                        issuedDate = tmp;
                                    } catch (Exception e) {
                                        isFailed = true;
                                        errorMessage.add("Ngày cấp CMND phải có định dạng dd-mm-yyy hoặc Ngày cấp CMND không tồn tại.");
                                        issuedDate = null;
                                    }
                                }
                                
                            }

                            if (cellIdx == 26) {
                                if (tmp.length() > 250) {
                                    isFailed = true;
                                    errorMessage.add("Số sổ BHXH không được quá 250 ký tự.");
                                } else {
                                    socialInsuranceNumber = tmp;
                                }
                            }
                            if (cellIdx == 26) {
                                reachLast = true;
                                break;
                            }
                        }
                    }
                    if (endLine) {
                        break;
                    }
                    if (isFailed || !reachLast) {
                        rollBackForStudent(result);
                        return new ServiceResult<>(HttpStatus.BAD_REQUEST,"Lỗi tại dòng " + (processRow+1), errorMessage);
                    } else {
                        StudentForm form = new StudentForm("", code, fullName, status, phone, birthDay, nation, permanentAddress, socialInsuranceNumber, issuedDate, sex, deptId, trainingSystem, email, religion, homeTown, temporaryAddress, identityCard, issuedAddress, startDate, electFormat, graduationType, quanHe, hoTenPhuHuynh, soDienThoaiPhuHuynh, quanHeSecond, hoTenPhuHuynhSecond, soDienThoaiPhuHuynhSecond, createdName, updateTime, updateName, classCode, year);
                        Map<String, List<Integer>> insertAStudent = insertAStudent(form);
                        if (insertAStudent == null) {
                            rollBackForStudent(result);
                        } else {
                            countSuccess += 1;
                            result.get("student").addAll(insertAStudent.get("student"));
                            result.get("contact").addAll(insertAStudent.get("contact"));
                            result.get("studenthistory").addAll(insertAStudent.get("studenthistory"));
                            result.get("user").addAll(insertAStudent.get("user"));
                        }
                    }
                    rowNumber++;
                }
            } catch (Exception e) {
                rollBackForStudent(result);
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Import file không thành công. Lỗi khi sử lý dòng "+ (processRow+1)+"\n", null);
            }
            if (countSuccess == 0) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "File import không có dữ liệu học sinh", null);
            } else {
                return new ServiceResult<>(HttpStatus.OK, countSuccess + " học sinh đã được thêm mới", null);
            }
        } else {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "File không đúng định dạng", null);
        }
    }

    @Override
    public ServiceResult<Page<StudentVo>> getAllStudentByNameOrCodeInClass(String status, String class_code, String search_word, String grade_level, String years, Pageable pageable) {
        return new ServiceResult<>(HttpStatus.OK, "Student list", repository.getAllStudentByNameOrCodeInClassAllStatus(status, class_code, search_word, grade_level, years, pageable));        
    }

    @Override
    public ServiceResult<Boolean> insert(StudentForm form) {
        if (form.getCode().length() == 0) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin mã học sinh", true);
        } else {
            if (repository.findByCode(form.getCode()).size() > 0) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Mã học sinh đã tồn tại", true);
            }
        }

        if (form.getFullName().length() == 0) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin tên học sinh", true);
        }

        if (form.getStatus() == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin trạng thái", true);
        }

        if (form.getTrainingSystem() == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin hệ đào tạo", true);
        }

        if (form.getQuanHe() == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin phụ huynh", true);
        }

        if (form.getHoTenPhuHuynh().length() == 0) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin họ tên phụ huynh", true);
        }

        if (form.getSoDienThoaiPhuHuynh().length() == 0) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin số điện thoại phụ huynh", true);
        }

        if (form.getClassCode().length() == 0) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin lớp học", true);
        }

        Map<String, List<Integer>> insertAStudent = insertAStudent(form);
        if (insertAStudent == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Lỗi khi tạo học sinh", true);
        } else {
            return new ServiceResult<>(HttpStatus.OK, "Tạo mới học sinh thành công", true);
        }
      
    }


    private Map<String, List<Integer>> insertAStudent(StudentForm form) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();
        result.put("student", new ArrayList<Integer>());
        result.put("contact", new ArrayList<Integer>());
        result.put("studenthistory", new ArrayList<Integer>());
        result.put("user", new ArrayList<Integer>());

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
            String createdName = userDetails.getUsername();

            Student s = new Student(form.getCreatedTime(), createdName, form.getUpdateTime(), form.getUpdateName(), form.getFullName(), form.getCode(), form.getDeptId(), form.getStartDate(), form.getPhone(), form.getEmail(), form.getBirthDay(), form.getHomeTown(), form.getNation(), form.getPermanentAddress(), form.getSocialInsuranceNumber(), form.getIdentityCard(),form.getIssuedAddress(), form.getIssuedDate(), form.getSex(), form.getAvatar(), form.getElectFormat(), form.getGraduationType(), form.getStatus(), form.getTrainingSystem(), form.getReligion(), form.getTemporaryAddress());
            List<Student> students = new ArrayList<>();
            students.add(s);
            List<Student> resultSaveStudent = repository.saveAll(students);
            for(Student i: resultSaveStudent) {
                result.get("student").add(i.getId());
            }
            Integer studentId = resultSaveStudent.get(0).getId();
            
            Contact c_primary = new Contact("", LocalDateTime.now(), form.getHoTenPhuHuynh(), form.getSoDienThoaiPhuHuynh(), 1, form.getQuanHe(), studentId);
            List<Contact> contacts = new ArrayList<>();
            contacts.add(c_primary);

            Contact c_second = new Contact("", LocalDateTime.now(), form.getHoTenPhuHuynhSecond(), form.getSoDienThoaiPhuHuynhSecond(), 0, form.getQuanHeSecond(), studentId);
            contacts.add(c_second);

            List<Contact> resultSaveContact = repositoryContact.saveAll(contacts);

            for (Contact i : resultSaveContact) {
                result.get("contact").add(i.getId());
            }

            StudentHistory sh = new StudentHistory(form.getCode(), form.getClassCode(), form.getYear());
            List<StudentHistory> studentHistoryList = new ArrayList<>();
            studentHistoryList.add(sh);
            List<StudentHistory> resultSaveStudentHistory = repositoryStudentHistory.saveAll(studentHistoryList);
            for(StudentHistory i: resultSaveStudentHistory) {
                result.get("studenthistory").add(i.getId());
            }

            User userSaved = userService.insertUserForStudent(form.getCode(), form.getFullName(),  form.getSoDienThoaiPhuHuynh(),form.getAvatar(), form.getEmail(), null,username);
            if (userSaved == null) {
                throw new Exception();
            }
            result.get("user").add(userSaved.getId());
        } catch (Exception e) {
            rollBackForStudent(result);
            return null;
        }
        return result;
    }

    private void rollBackForStudent (Map<String, List<Integer>> result) {
        for(Integer i: result.get("student")) {
            try {
                repository.deleteById(i);
            } catch (Exception e) {

            }
            
        }
        for(Integer i: result.get("contact")) {
            try {
                repositoryContact.deleteById(i);
            } catch (Exception e) {
            }
        }
        for(Integer i: result.get("studenthistory")) {
            try {
                repositoryStudentHistory.deleteById(i);
            } catch (Exception e) {

            }
        }
        for(Integer i: result.get("user")) {
            try {
                repositoryUser.deleteById(i);
            } catch  (Exception e) {
            }
        }
    }

    @Override
    public ServiceResult<Boolean> update(StudentForm form) {
        if(form.getCode() == null || form.getCode().length() == 0)
        {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin mã học sinh", true);
        }

        if(form.getFullName() == null || form.getFullName().length() == 0)
        {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin tên học sinh", true);
        }

        if(form.getStatus() == null)
        {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin trạng thái", true);
        }

        if(form.getTrainingSystem() == null)
        {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin hệ đào tạo", true);
        }

        if(form.getQuanHe() == null)
        {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thiếu thông tin phụ huynh", true);
        }

        if(form.getHoTenPhuHuynh() == null || form.getHoTenPhuHuynh().length() == 0)
        {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST,"Thiếu thông tin họ tên phụ huynh", true);
        }

        if (form.getSoDienThoaiPhuHuynh() == null || form.getSoDienThoaiPhuHuynh().length() == 0) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST,"Thiếu thông tin số điện thoại phụ huynh", true);
        }

        if (form.getClassCode() ==null || form.getClassCode().length() == 0) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST,"Thiếu thông tin lớp học", true);
        }

        Optional<ClassRoom> tt = classRoomRepository.getClassRoomByCode(form.getClassCode());
        if(!tt.isPresent()) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST,"Lớp học không tồn tại", true);
        }
        // if (tt.size() == 0  ) {
        //     return new ServiceResult<>(HttpStatus.BAD_REQUEST,"Mã lớp học không tồn tại", true);
        // }

        List<StudentDetailInformationVo> hstmp = repository.getStudentDetailInformation(form.getCode(), form.getYear(), PageRequest.of(0, 1));
        if(hstmp.size() == 0) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST,"Sai thông tin học sinh", true);
        }

        String oldClassCode = hstmp.get(0).getClass_code();
        if(!oldClassCode.equals(form.getClassCode())) {
            if(gradebookRepository.checkExistScoreToChangeClass(form.getYear(), form.getCode())){
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Không thể đổi lớp cho học sinh đã có thông tin điểm", null);
            }
        }

        Student s = new Student(form.getCreatedTime(), "", form.getUpdateTime(), form.getUpdateName(), form.getFullName(), form.getCode(), form.getDeptId(), form.getStartDate(), form.getPhone(), form.getEmail(), form.getBirthDay(), form.getHomeTown(), form.getNation(), form.getPermanentAddress(), form.getSocialInsuranceNumber(), form.getIdentityCard(),form.getIssuedAddress(), form.getIssuedDate(), form.getSex(), form.getAvatar(), form.getElectFormat(), form.getGraduationType(), form.getStatus(), form.getTrainingSystem(), form.getReligion(), form.getTemporaryAddress());
        
        
        // gradebookRepository.checkExistScoreToChangeClass(year, studentCode)
        repository.update(s.getCode(), s.getUpdateName(), s.getFullName(), s.getDeptId(), s.getStartDate(), s.getPhone(), s.getEmail(), s.getBirthDay(), s.getHomeTown(), s.getNation(), s.getPermanentAddress(), s.getSocialInsuranceNumber(), s.getIdentityCard(), s.getIssuedAddress(), s.getIssuedDate(), s.getSex(), s.getAvatar(), s.getElectFormat(), s.getGraduationType(), s.getStatus(), s.getTrainingSystem(), s.getReligion(), s.getTemporaryAddress());
        Integer studentId = repository.findByCode(s.getCode()).get(0).getId();

        repositoryContact.updatePrimary(form.getHoTenPhuHuynh(), form.getSoDienThoaiPhuHuynh(), form.getQuanHe(), studentId);
        repositoryContact.updateSecond(form.getHoTenPhuHuynhSecond(), form.getSoDienThoaiPhuHuynhSecond(), form.getQuanHeSecond(), studentId);

        repositoryStudentHistory.update(form.getClassCode(), form.getCode(), form.getYear());
        // StudentHistory sh = new StudentHistory(form.getCode(), form.getClassCode(), form.getYear());
        // List<StudentHistory> studentHistoryList = new ArrayList<>();
        // studentHistoryList.add(sh);
        // repositoryStudentHistory.saveAll(studentHistoryList);

        return new ServiceResult<>(HttpStatus.OK, "Cập nhật học sinh thành công!", true);
    }

    @Override
    public ServiceResult<List<Map<String, Object>>> getStudentOfClass(String year, String classCode, Integer semester, String subjectCode) {
        ServiceResult serviceResult = new ServiceResult();
        try{
            List<Student> list = repository.getStudentOfClass(year, classCode);
            List<Map<String, Object>> listStudentEvaluated = gradebookSubjectsDetailsRepository.listStudentOfClassEvaluated(year, classCode, semester, subjectCode);
            List<Map<String, Object>> listOutput = new ArrayList<>();
            for(int i = 0; i < list.size(); i++){
                Map<String, Object> output = new HashMap<>();
                output.put("code", list.get(i).getCode());
                output.put("fullName", list.get(i).getFullName());
                output.put("isEvaluated", 0);
                output.put("evaluate", null);
                for(int j = 0;j < listStudentEvaluated.size(); j++){
                    if(list.get(i).getCode().equals(listStudentEvaluated.get(j).get("code").toString())){
                        output.put("isEvaluated", 1);
                        if(listStudentEvaluated.get(j).get("evaluate").equals(null)){
                            output.put("isEvaluated", 0);
                        }else{
                            if(listStudentEvaluated.get(j).get("evaluate").toString().trim().equals("")){
                                output.put("isEvaluated", 0);
                            }else{
                                output.put("evaluate", listStudentEvaluated.get(j).get("evaluate"));
                            }
                        }
                        break;
                    }
                }
                listOutput.add(output);
            }
            serviceResult.setData(listOutput);
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
        }catch (Exception e){
            serviceResult.setMessage("fail");
            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
        }
        return serviceResult;
    }

    @Override
    public ServiceResult<List<Student>> getStudentOfClassScreenDanhGiaHocLuc(String year, String classCode) {
        ServiceResult serviceResult = new ServiceResult();
        try{
            List<Student> list = repository.getStudentOfClassManDanhGiaHocLuc(year, classCode);
            Map<String, Object> output = new HashMap<>();
            output.put("data", list);
            serviceResult.setData(output);
            serviceResult.setMessage("success");
            serviceResult.setStatus(HttpStatus.OK);
        }catch (Exception e){
            serviceResult.setMessage("failed");
            serviceResult.setStatus(HttpStatus.BAD_REQUEST);
        }
        return serviceResult;
    }

    public ServiceResult<List<StudentDetailInformationVo>> getStudentDetailInformation(String student_code, String year) {
        return new ServiceResult<>(HttpStatus.OK, "Student information", repository.getStudentDetailInformation(student_code, year, PageRequest.of(0, 1)));        
    }

    @Override
    public ServiceResult<List<Student>> getStudentManagedByClass(String classCode, String year) {
        return new ServiceResult<>(HttpStatus.OK, "students", repository.getCurrentStudentsInClass(classCode, year));
    }

    @Override
    public ServiceResult<List<Map<String, Object>>> getAllYearHistoryOfStudent(String studentCode) {
        try {
            return new ServiceResult<>(HttpStatus.OK, "success", repository.getAllYearHistoryOfStudent(studentCode));
        }catch (Exception e){
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "fail", null);
        }
    }

    @Override
    public ServiceResult<List<Map<String, Object>>> getResultStudyEver(String classCode, String year, String studentCode, Integer classId) {
        try {
            return new ServiceResult<>(HttpStatus.OK, "success", repository.getResultStudyEver(classCode, year, studentCode, classId));
        }catch (Exception e){
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Có lỗi xảy ra", null);
        }
    }

    @Override
    public ServiceResult<List<StudentDetailInformationVo>> getStudentDetailInformationSideParent(String studentCode, String year) {
        try {
            return new ServiceResult<>(HttpStatus.OK, "success", repository.getStudentDetailInformationSideParent(studentCode, year,PageRequest.of(0, 1)));
        }catch (Exception e){
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Lấy thông tin học sinh thất bại", null);
        }
    }

}
