package fpt.capstone.form_data;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class StudentForm {
    private String avatar = "";
    private String code = "";
    private String fullName = "";
    private Integer status;
    private String phone = "";
    private LocalDateTime birthDay;
    private String nation = "";
    private String permanentAddress = "";
    private String socialInsuranceNumber = "";
    private LocalDateTime issuedDate;
    private Integer sex;
    private Integer deptId;
    private Integer trainingSystem;
    private String email = "";
    private String religion = "";
    private String homeTown = "";
    private String temporaryAddress = "";
    private String identityCard = "";
    private String issuedAddress = "";
    private LocalDateTime startDate;
    private Integer electFormat;
    private Integer graduationType;
    private Integer quanHe;
    private String hoTenPhuHuynh = "";
    private String soDienThoaiPhuHuynh = "";
    private Integer quanHeSecond;
    private String hoTenPhuHuynhSecond = "";
    private String soDienThoaiPhuHuynhSecond = "";
    private LocalDateTime createdTime = LocalDateTime.now();
    private String createdName = "";
    private LocalDateTime updateTime = LocalDateTime.now();
    private String updateName = "";
    private String classCode = "";
    private String year = "";



   public StudentForm() {
    
   }


    public StudentForm(String avatar, String code, String fullName, Integer status, String phone, String birthDay, String nation, String permanentAddress, String socialInsuranceNumber, String issuedDate, Integer sex, Integer deptId, Integer trainingSystem, String email, String religion, String homeTown, String temporaryAddress, String identityCard, String issuedAddress, String startDate, Integer electFormat, Integer graduationType, Integer quanHe, String hoTenPhuHuynh, String soDienThoaiPhuHuynh, Integer quanHeSecond, String hoTenPhuHuynhSecond, String soDienThoaiPhuHuynhSecond, String createdName, String updateTime, String updateName, String classCode, String year) {
        this.avatar = avatar;
        this.code = code;
        this.fullName = fullName;
        this.status = status;
        this.phone = phone;
        this.birthDay = convertDate(birthDay);
        this.nation = nation;
        this.permanentAddress = permanentAddress;
        this.socialInsuranceNumber = socialInsuranceNumber;
        this.issuedDate = convertDate(issuedDate);
        this.sex = sex;
        this.deptId = deptId;
        this.trainingSystem = trainingSystem;
        this.email = email;
        this.religion = religion;
        this.homeTown = homeTown;
        this.temporaryAddress = temporaryAddress;
        this.identityCard = identityCard;
        this.issuedAddress = issuedAddress;
        this.startDate = convertDate(startDate);
        this.electFormat = electFormat;
        this.graduationType = graduationType;
        this.quanHe = quanHe;
        this.hoTenPhuHuynh = hoTenPhuHuynh;
        this.soDienThoaiPhuHuynh = soDienThoaiPhuHuynh;
        this.quanHeSecond = quanHeSecond;
        this.hoTenPhuHuynhSecond = hoTenPhuHuynhSecond;
        this.soDienThoaiPhuHuynhSecond = soDienThoaiPhuHuynhSecond;
        this.createdTime = LocalDateTime.now();
        this.createdName = createdName;
        this.updateTime = convertDate(updateTime);
        this.updateName = updateName;
        this.classCode = classCode;
        this.year = year;
    }

    public String getYear() {
        return this.year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public LocalDateTime convertDate(String dateString) {
        if (dateString == null || dateString.trim().length() == 0) {
            return null;
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(dateString + " 00:00", formatter);
            return dateTime;
        }
    }


    public Integer getQuanHeSecond() {
        return this.quanHeSecond;
    }

    public void setQuanHeSecond(Integer quanHeSecond) {
        this.quanHeSecond = quanHeSecond;
    }

    public String getHoTenPhuHuynhSecond() {
        return this.hoTenPhuHuynhSecond;
    }

    public void setHoTenPhuHuynhSecond(String hoTenPhuHuynhSecond) {
        this.hoTenPhuHuynhSecond = hoTenPhuHuynhSecond;
    }

    public String getSoDienThoaiPhuHuynhSecond() {
        return this.soDienThoaiPhuHuynhSecond;
    }

    public void setSoDienThoaiPhuHuynhSecond(String soDienThoaiPhuHuynhSecond) {
        this.soDienThoaiPhuHuynhSecond = soDienThoaiPhuHuynhSecond;
    }


    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getBirthDay() {
        return this.birthDay;
    }

    public void setBirthDay(String birthDay) {
        if (birthDay.length() == 0) {
            this.startDate = null;
        } else {
            this.birthDay = convertDate(birthDay);
        }
    }

    public String getNation() {
        return this.nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getClassCode() {
        return this.classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getPermanentAddress() {
        return this.permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getSocialInsuranceNumber() {
        return this.socialInsuranceNumber;
    }

    public void setSocialInsuranceNumber(String socialInsuranceNumber) {
        this.socialInsuranceNumber = socialInsuranceNumber;
    }

    public LocalDateTime getIssuedDate() {
        return this.issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        if (issuedDate.length() == 0) {
            this.issuedDate = null;
        } else {
            this.issuedDate = convertDate(issuedDate);
        }
    }

    public Integer getSex() {
        return this.sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getDeptId() {
        return this.deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public Integer getTrainingSystem() {
        return this.trainingSystem;
    }

    public void setTrainingSystem(Integer trainingSystem) {
        this.trainingSystem = trainingSystem;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReligion() {
        return this.religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getHomeTown() {
        return this.homeTown;
    }

    public void setHomeTown(String homeTown) {
        this.homeTown = homeTown;
    }

    public String getTemporaryAddress() {
        return this.temporaryAddress;
    }

    public void setTemporaryAddress(String temporaryAddress) {
        this.temporaryAddress = temporaryAddress;
    }

    public String getIdentityCard() {
        return this.identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getIssuedAddress() {
        return this.issuedAddress;
    }

    public void setIssuedAddress(String issuedAddress) {
        this.issuedAddress = issuedAddress;
    }

    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        if (startDate.length() == 0) {
            this.startDate = null;
        } else {
            this.startDate = convertDate(startDate);
        }
    }

    public Integer getElectFormat() {
        return this.electFormat;
    }

    public void setElectFormat(Integer electFormat) {
        this.electFormat = electFormat;
    }

    public Integer getGraduationType() {
        return this.graduationType;
    }

    public void setGraduationType(Integer graduationType) {
        this.graduationType = graduationType;
    }

    public Integer getQuanHe() {
        return this.quanHe;
    }

    public void setQuanHe(Integer quanHe) {
        this.quanHe = quanHe;
    }

    public String getHoTenPhuHuynh() {
        return this.hoTenPhuHuynh;
    }

    public void setHoTenPhuHuynh(String hoTenPhuHuynh) {
        this.hoTenPhuHuynh = hoTenPhuHuynh;
    }

    public String getSoDienThoaiPhuHuynh() {
        return this.soDienThoaiPhuHuynh;
    }

    public void setSoDienThoaiPhuHuynh(String soDienThoaiPhuHuynh) {
        this.soDienThoaiPhuHuynh = soDienThoaiPhuHuynh;
    }

    public LocalDateTime getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime() {
        this.createdTime = LocalDateTime.now();
    }

    public String getCreatedName() {
        return this.createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = convertDate(updateTime);
    }

    public String getUpdateName() {
        return this.updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }




    @Override
    public String toString() {
        return "{" +
            " avatar='" + getAvatar() + "'" +
            ", code='" + getCode() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", status='" + getStatus() + "'" +
            ", phone='" + getPhone() + "'" +
            ", birthDay='" + getBirthDay() + "'" +
            ", nation='" + getNation() + "'" +
            ", permanentAddress='" + getPermanentAddress() + "'" +
            ", socialInsuranceNumber='" + getSocialInsuranceNumber() + "'" +
            ", issuedDate='" + getIssuedDate() + "'" +
            ", sex='" + getSex() + "'" +
            ", deptId='" + getDeptId() + "'" +
            ", trainingSystem='" + getTrainingSystem() + "'" +
            ", email='" + getEmail() + "'" +
            ", religion='" + getReligion() + "'" +
            ", homeTown='" + getHomeTown() + "'" +
            ", temporaryAddress='" + getTemporaryAddress() + "'" +
            ", identityCard='" + getIdentityCard() + "'" +
            ", issuedAddress='" + getIssuedAddress() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", electFormat='" + getElectFormat() + "'" +
            ", graduationType='" + getGraduationType() + "'" +
            ", quanHe='" + getQuanHe() + "'" +
            ", hoTenPhuHuynh='" + getHoTenPhuHuynh() + "'" +
            ", soDienThoaiPhuHuynh='" + getSoDienThoaiPhuHuynh() + "'" +
            ", quanHeSecond='" + getQuanHeSecond() + "'" +
            ", hoTenPhuHuynhSecond='" + getHoTenPhuHuynhSecond() + "'" +
            ", soDienThoaiPhuHuynhSecond='" + getSoDienThoaiPhuHuynhSecond() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", createdName='" + getCreatedName() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", updateName='" + getUpdateName() + "'" +
            ", classCode='" + getClassCode() + "'" +
            ", year='" + getYear() + "'" +
            "}";
    }
}
