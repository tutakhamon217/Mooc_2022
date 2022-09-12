package fpt.capstone.controller;

import fpt.capstone.entities.*;
import fpt.capstone.form_data.*;
import fpt.capstone.repository.AuthorityRepository;
import fpt.capstone.repository.StudentRepository;
import fpt.capstone.repository.TeacherRepository;
import fpt.capstone.repository.UserRepository;
import fpt.capstone.security.jwt.JwtUtils;
import fpt.capstone.security.services.UserDetailsImpl;
import fpt.capstone.service.UserService;
import fpt.capstone.utility.ConvertStringToAuthority;
import fpt.capstone.utility.EmailSender;
import fpt.capstone.utility.GenerateOTP;
import fpt.capstone.utility.MyConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    ConvertStringToAuthority convertStringToAuthority;

    @PostMapping("/signin")
    ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginForm) {
        if (userRepository.findByLogin(loginForm.getLogin()) == null) {
            return ResponseEntity.ok(new ServiceResult<>(HttpStatus.BAD_REQUEST, "Tên đăng nhập không tồn tại", null));
        }

        List<Teacher> teacherTmp = teacherRepository.findByCode(loginForm.getLogin());
        if(teacherTmp.size()>0 &&  (teacherTmp.get(0).getStatus() == 1 || teacherTmp.get(0).getStatus() == 2)  ) {
            return ResponseEntity.ok(new ServiceResult<>(HttpStatus.BAD_REQUEST, "Bạn không còn quyền truy cập vào hệ thống", null));
        }

        List<Student> studentTmp = studentRepository.findByCode(loginForm.getLogin());

        if(studentTmp.size()>0 &&  (studentTmp.get(0).getStatus() == 2 || studentTmp.get(0).getStatus() == 3)  ) {
            return ResponseEntity.ok(new ServiceResult<>(HttpStatus.BAD_REQUEST, "Bạn không còn quyền truy cập vào hệ thống", null));
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.getLogin(), loginForm.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }

    @PostMapping("/signup")
    ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpForm) {
        Set<Authority> roles = convertStringToAuthority.convertStringToAuthority(signUpForm.getRole());
        User user = new User(signUpForm.getLogin(), encoder.encode(signUpForm.getPassword()), roles);
        User addUser = userRepository.save(user);
        Map<String, Object> output = new HashMap<>();
        output.put("result", addUser == null ? false : true);
        return ResponseEntity.ok(output);
    }

    @PostMapping("/forgot_password")
    ServiceResult<Boolean> forgotPassword(@RequestBody ForgotPasswordForm forgotPasswordForm) {
        return userService.resetPassword(forgotPasswordForm.getLogin());
    }

    @PostMapping("/check_otp")
    ServiceResult<Boolean> checkOTP(@RequestBody CheckOTPForm checkOTPForm) {
        return userService.checkOTPandResetPass(checkOTPForm.getOtp(), checkOTPForm.getLogin());
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(){
        Map<String, Object> output = new HashMap<>();
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
            output.put("id", userDetails.getId());
            output.put("username", userDetails.getUsername());
            output.put("roles", userDetails.getAuthorities());
            output.put("email", userDetails.getEmail());
            output.put("fullName", userDetails.getFullName());
            output.put("avatar", userDetails.getAvatar());
            output.put("phone", userDetails.getPhone());
            return ResponseEntity.ok(output);
        }catch (Exception e){
            output.put("status", 401);
            output.put("message", HttpStatus.UNAUTHORIZED);
            return ResponseEntity.of(Optional.of(output));
        }
    }

    @GetMapping("/checkAuthToken")
    public ResponseEntity checkAuthToken(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(auth);
    }

    @PostMapping("/change_password")
    ServiceResult<Boolean> ChangePassword(@RequestBody ChangePasswordForm changePasswordForm){
        return userService.changePassword(changePasswordForm);
    }
}