package fpt.capstone.service;

import fpt.capstone.entities.Authority;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.entities.User;
import fpt.capstone.form_data.ChangePasswordForm;
import fpt.capstone.repository.UserRepository;

import fpt.capstone.utility.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    ConvertStringToAuthority convert;

    @Override
    public boolean insertUserForTeacher(String login, String fullName, String phone, String ImageUrl, String email, Set authorities, String username) {
        Set<Authority> roles = convert.convertStringToAuthority(authorities);
        try {
            User user = new User(login, encoder.encode(Password.PassWord), fullName, phone, ImageUrl, username, LocalDateTime.now(), email, roles);
            User userAdded = userRepository.save(user);
            if (null == userAdded) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateUserForTeacher(String login, String fullName, String phone, String ImageUrl, String email, Set authorities, String username) {
        Set<Authority> roles = convert.convertStringToAuthority(authorities);
        try {
            User oldUser = userRepository.findByLogin(login);
            oldUser.setAuthorities(roles);
            oldUser.setPhoneNumber(phone);
            oldUser.setFullName(fullName);
            oldUser.setImageUrl(ImageUrl);
            oldUser.setLastModifiedBy(username);
            oldUser.setLastModifiedDate(LocalDateTime.now());
            oldUser.setEmail(email);
            User userUpdated = userRepository.save(oldUser);
            if (null == userUpdated) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public User insertUserForStudent(String login, String fullName, String phone, String ImageUrl, String email, Set authorities, String username) {
        User userSaved = null;
        Set<Authority> roles = convert.convertStringToAuthority(authorities);
        try {
            User user = new User(login, encoder.encode(Password.PassWord), fullName, phone, ImageUrl, username, LocalDateTime.now(), email, roles);
            userSaved = userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userSaved;
    }

    @Override
    public ServiceResult<Boolean> resetPassword(String login) {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Tên đăng nhập không tồn tại", true);
        } else {
            String phone = user.getPhoneNumber();
            String OTP = GenerateOTP.getOTP();
            SpeedSMSAPI api = new SpeedSMSAPI("op4HavZ6pbkaaq4jI_4T32xMXDs4xj43");
            String result = null;
            try {
                result = api.sendSMS(phone, "Chào mừng đến với hệ thống SmartEdu. Mã OTP để đặt lại mật khẩu mặc định của bạn là: " + OTP, 2, "015e1b3e8e7998f8");
                LocalDateTime resetDate = LocalDateTime.now().plusMinutes(2);
                user.setResetDate(resetDate);
                user.setResetKey(OTP);
                userRepository.save(user);
                return new ServiceResult<>(HttpStatus.OK, "Mã OTP đã được gửi đến số điện thoại " + "******+" +phone.substring(7, 10), true);
            } catch (IOException e) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Lỗi trong quá trình gửi OTP", true);
            }
        }
    }

    @Override
    public ServiceResult<Boolean> checkOTPandResetPass(String otp, String login) {
        try {
            User user = userRepository.findByLogin(login);
            if (user == null) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Tên đăng nhập không tồn tại", true);
            } else {
                String otpDB = user.getResetKey();
                if (!otpDB.equals(otp)) {
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "OTP nhập sai", true);
                }
                if (LocalDateTime.now().isAfter(user.getResetDate())) {
                    return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Thời gian nhập OTP đã hết", true);
                }
                user.setPasswordHash(encoder.encode(Password.PassWord));
                userRepository.save(user);
                return new ServiceResult<>(HttpStatus.OK, "Cập nhật mật khẩu thành công. Mật khẩu đã được đặt lại thành mật khẩu mặc định", true);
            }
        } catch (Exception e) {
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Cập nhật mật khẩu thất bại", true);
        }
    }

    @Override
    public ServiceResult<Boolean> changePassword(ChangePasswordForm changePasswordForm) {
        try {
            User user = userRepository.findByLogin(changePasswordForm.getLogin());
            if (user == null) {
                return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Tên đăng nhập không tồn tại", true);
            } else {
                user.setPasswordHash(encoder.encode(changePasswordForm.getPassword()));
                userRepository.save(user);
                return new ServiceResult<>(HttpStatus.OK, "Cập nhật mật khẩu thành công", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ServiceResult<>(HttpStatus.BAD_REQUEST, "Cập nhật mật khẩu thất bại", true);
        }
    }
}
