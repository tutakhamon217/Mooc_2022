package fpt.capstone.service;

import fpt.capstone.entities.ServiceResult;
import fpt.capstone.entities.User;
import fpt.capstone.form_data.ChangePasswordForm;

import java.util.Set;

public interface UserService {
    boolean insertUserForTeacher(String login, String fullName, String phone, String ImageUrl, String email, Set authorities,String username);

    boolean updateUserForTeacher(String login, String fullName, String phone, String ImageUrl, String email, Set authorities,String username);

    User insertUserForStudent(String login, String fullName, String phone, String ImageUrl, String email, Set authorities,String username);

    ServiceResult<Boolean> resetPassword(String login);

    ServiceResult<Boolean> checkOTPandResetPass(String otp, String login);
    ServiceResult<Boolean> changePassword(ChangePasswordForm changePasswordForm);
}
