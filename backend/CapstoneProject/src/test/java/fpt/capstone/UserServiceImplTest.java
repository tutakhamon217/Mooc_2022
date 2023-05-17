package fpt.capstone;

import fpt.capstone.entities.Authority;
import fpt.capstone.entities.ConfigScoreDetail;
import fpt.capstone.entities.ServiceResult;
import fpt.capstone.entities.User;
import fpt.capstone.form_data.ChangePasswordForm;
import fpt.capstone.repository.AuthorityRepository;
import fpt.capstone.repository.UserRepository;
import fpt.capstone.service.UserServiceImpl;
import fpt.capstone.utility.ConvertStringToAuthority;
import fpt.capstone.utility.GenerateOTP;
import fpt.capstone.utility.Password;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder encoder;

    @Mock
    GenerateOTP generateOTP;

    @Mock
    ConvertStringToAuthority convert;

    @Mock
    AuthorityRepository authorityRepository;

    //login doesn't exist
    @Test()
    void changePasswordTest1() {
        ChangePasswordForm changePasswordForm = new ChangePasswordForm("admin123", "123456a");
        ServiceResult<Boolean> actual = userService.changePassword(changePasswordForm);
        assertEquals("Tên đăng nhập không tồn tại", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    //login exist
    @Test()
    void changePasswordTest2() {
        DateTimeFormatter ft = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse("2022-08-16 02:36:09", ft);
        User usertest = new User(1, "admin", "$10$TiFGi7sj0xzBggmpFVe2EOKAJhyn0w3RP3gMTMELVC5PZHvAg14xO", "Administrator", "0986688009",
                null, "610950", "system", null, date, null, null, null, null, null);
        ChangePasswordForm changePasswordForm = new ChangePasswordForm("admin", "2172000");
        when(userRepository.findByLogin(any())).thenReturn(usertest);
        when(encoder.encode(any())).thenReturn("$10$TiFGi7sj0xzBggmpFVe2EOKAJhyn0w3RP3gMTMELVC5PZHvAg14xO");
        ServiceResult<Boolean> actual = userService.changePassword(changePasswordForm);

        assertEquals("Cập nhật mật khẩu thành công", actual.getMessage());
        assertEquals(HttpStatus.OK, actual.getStatus());

    }
    @Test()
    void changePasswordTest3() {
        DateTimeFormatter ft = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse("2022-08-16 02:36:09", ft);
        User usertest = new User(1, "admin", "$10$TiFGi7sj0xzBggmpFVe2EOKAJhyn0w3RP3gMTMELVC5PZHvAg14xO", "Administrator", "0986688009",
                null, "610950", "system", null, date, null, null, null, null, null);
        ChangePasswordForm changePasswordForm = new ChangePasswordForm("admin", "2172000");
        when(userRepository.findByLogin(any())).thenReturn(usertest);
        when(encoder.encode(any())).thenReturn("$10$TiFGi7sj0xzBggmpFVe2EOKAJhyn0w3RP3gMTMELVC5PZHvAg14xO");
        when(userRepository.save(any())).thenThrow( RuntimeException.class);
        ServiceResult<Boolean> actual = userService.changePassword(changePasswordForm);

        assertEquals("Cập nhật mật khẩu thất bại", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());

    }
    //login doesn't exist
    @Test()
    void checkOTPandResetPassTest1() {
        ServiceResult<Boolean> actual = userService.checkOTPandResetPass("123456", "admin123");
        assertEquals("Tên đăng nhập không tồn tại", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    //login exist but otp wrong
    @Test()
    void checkOTPandResetPassTest2() {
        DateTimeFormatter ft = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse("2022-08-16 02:36:09", ft);
        User usertest = new User(1, "admin", "$10$TiFGi7sj0xzBggmpFVe2EOKAJhyn0w3RP3gMTMELVC5PZHvAg14xO", "Administrator", "0986688009",
                null, "610950", "system", null, date, null, null, null, null, null);
        when(userRepository.findByLogin(any())).thenReturn(usertest);
        ServiceResult<Boolean> actual = userService.checkOTPandResetPass("610951", "admin");

        assertEquals("OTP nhập sai", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    //login exist but otp dung but reset date over 2 minutes
    @Test()
    void checkOTPandResetPassTest3() {
        LocalDateTime resetDate = LocalDateTime.now().minusMinutes(2);

        User usertest = new User(1, "admin", "$10$TiFGi7sj0xzBggmpFVe2EOKAJhyn0w3RP3gMTMELVC5PZHvAg14xO", "Administrator", "0986688009",
                null, "610950", "system", null, resetDate, null, null, null, null, null);
        when(userRepository.findByLogin(any())).thenReturn(usertest);

        ServiceResult<Boolean> actual = userService.checkOTPandResetPass("610950", "admin");

        assertEquals("Thời gian nhập OTP đã hết", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    //login exist but otp dung ,otp con han, reset pass ok
    @Test()
    void checkOTPandResetPassTest4() {

        LocalDateTime resetDate = LocalDateTime.now().plusSeconds(2);

        User usertest = new User(1, "admin", "$10$TiFGi7sj0xzBggmpFVe2EOKAJhyn0w3RP3gMTMELVC5PZHvAg14xO", "Administrator", "0986688009",
                null, "610950", "system", null, resetDate, null, null, null, null, null);
        when(userRepository.findByLogin(any())).thenReturn(usertest);

        ServiceResult<Boolean> actual = userService.checkOTPandResetPass("610950", "admin");

        ArgumentCaptor<User> roundArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(roundArgumentCaptor.capture());

        assertEquals("Cập nhật mật khẩu thành công. Mật khẩu đã được đặt lại thành mật khẩu mặc định", actual.getMessage());
        assertEquals(HttpStatus.OK, actual.getStatus());
    }

    @Test()
    void checkOTPandResetPassTest5() {

        LocalDateTime resetDate = LocalDateTime.now().plusSeconds(2);

        User usertest = new User(1, "admin", "$10$TiFGi7sj0xzBggmpFVe2EOKAJhyn0w3RP3gMTMELVC5PZHvAg14xO", "Administrator", "0986688009",
                null, "610950", "system", null, resetDate, null, null, null, null, null);
        when(userRepository.findByLogin(any())).thenReturn(usertest);
        when(userRepository.save(any())).thenThrow( RuntimeException.class);
        ServiceResult<Boolean> actual = userService.checkOTPandResetPass("610950", "admin");

        assertEquals("Cập nhật mật khẩu thất bại", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    //login doesn't exist
    @Test()
    void resetPasswordTest1() {
        ServiceResult<Boolean> actual = userService.resetPassword("admin123");
        assertEquals("Tên đăng nhập không tồn tại", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }

    //login doesn't exist
    @Test()
    void resetPasswordTest2() {
        String phone = "0387399342";
        DateTimeFormatter ft = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime resetDate = LocalDateTime.parse("2022-08-16 02:36:09", ft);
        User usertest = new User(1, "admin", "$10$TiFGi7sj0xzBggmpFVe2EOKAJhyn0w3RP3gMTMELVC5PZHvAg14xO", "Administrator", phone,
                null, "610950", "system", null, resetDate, null, null, null, null, null);
        when(userRepository.findByLogin(any())).thenReturn(usertest);

        ServiceResult<Boolean> actual = userService.resetPassword("admin");
        ArgumentCaptor<User> roundArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(roundArgumentCaptor.capture());

        assertEquals("Mã OTP đã được gửi đến số điện thoại " + phone.substring(0, 7) + "***", actual.getMessage());
        assertEquals(HttpStatus.OK, actual.getStatus());
    }
    @Test()
    void resetPasswordTest3() {
        String phone = "0387399342";
        DateTimeFormatter ft = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime resetDate = LocalDateTime.parse("2022-08-16 02:36:09", ft);
        User usertest = new User(1, "admin", "$10$TiFGi7sj0xzBggmpFVe2EOKAJhyn0w3RP3gMTMELVC5PZHvAg14xO", "Administrator", phone,
                null, "610950", "system", null, resetDate, null, null, null, null, null);
        when(userRepository.findByLogin(any())).thenReturn(usertest);
//        when(userRepository.save(any())).thenThrow( RuntimeException.class);
         when(userRepository.save(any())).thenAnswer(invocation -> {throw new IOException();});
        ServiceResult<Boolean> actual = userService.resetPassword("admin");
//        ArgumentCaptor<User> roundArgumentCaptor = ArgumentCaptor.forClass(User.class);
//        verify(userRepository, times(1)).save(roundArgumentCaptor.capture());
        System.out.println(actual.getMessage());

        assertEquals("Lỗi trong quá trình gửi OTP", actual.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, actual.getStatus());
    }
    //
    @Test()
    void insertUserForTeacherTest1() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVCN");
        Set<Authority> roles=new HashSet<>();
        roles.add(new Authority("ROLE_GVBM","Giáo viên bộ môn"));
        when(convert.convertStringToAuthority(any())).thenReturn(roles);
        when(encoder.encode(any())).thenReturn("$10$TiFGi7sj0xzBggmpFVe2EOKAJhyn0w3dP3gMTMELVC5PZHvAgd14x");
        when(userRepository.save(any())).thenReturn(new User());
        boolean actual = userService.insertUserForTeacher("admin123", "abc", "0387399342", "abc", "tuanvtq@gmail.com", authorities, "admin123");
        assertEquals(true, actual);
    }

    //
    @Test()
    void insertUserForTeacherTest2() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        Set<Authority> roles=new HashSet<>();
        roles.add(new Authority("ROLE_GVBM","Giáo viên bộ môn"));
        when(convert.convertStringToAuthority(any())).thenReturn(roles);
        when(encoder.encode(any())).thenReturn("$10$TiFGi7sj0xzBggmpFVe2EOKAJhyn0w3RP3gMTMELVC5PZHvAg14xO");
        boolean actual = userService.insertUserForTeacher(null, "Vũ Văn Tuấn", null, null, "tuanvtq@gmail.com", authorities, null);
        assertEquals(false, actual);
    }

    @Test()
    void insertUserForTeacherTest3() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        Set<Authority> roles=new HashSet<>();
        roles.add(new Authority("ROLE_GVBM","Giáo viên bộ môn"));
        when(convert.convertStringToAuthority(any())).thenReturn(roles);
        when(encoder.encode(any())).thenReturn("$10$TiFGi7sj0xzBggmpFVe2EOKAJhyn0w3RP3gMTMELVC5PZHvAg14xO");
        when(userRepository.save(any())).thenReturn(null);
        boolean actual = userService.insertUserForTeacher(null, "Vũ Văn Tuấn", null, null, "tuanvtq@gmail.com", authorities, null);
        assertEquals(false, actual);
    }
    @Test()
    void insertUserForTeacherTest4() {
        Set<String> authorities = new HashSet<>();
        authorities.add("ROLE_GVBM");
        Set<Authority> roles=new HashSet<>();
        roles.add(new Authority("ROLE_GVBM","Giáo viên bộ môn"));
        when(convert.convertStringToAuthority(any())).thenReturn(roles);
        when(encoder.encode(any())).thenReturn("$10$TiFGi7sj0xzBggmpFVe2EOKAJhyn0w3RP3gMTMELVC5PZHvAg14xO");
        when(userRepository.save(any())).thenThrow( RuntimeException.class);
        boolean actual = userService.insertUserForTeacher(null, "Vũ Văn Tuấn", null, null, "tuanvtq@gmail.com", authorities, null);
        assertEquals(false, actual);
    }
}
