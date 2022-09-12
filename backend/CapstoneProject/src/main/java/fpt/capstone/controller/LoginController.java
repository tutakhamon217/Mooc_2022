package fpt.capstone.controller;

import fpt.capstone.form_data.LoginForm;
import fpt.capstone.payroll.LoginFailException;
import fpt.capstone.repository.UserRepository;
import fpt.capstone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    private final UserRepository repository;

    LoginController(UserRepository repository)
    {
        this.repository = repository;
    }

    // @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    // public UserVo login(@RequestBody LoginForm form) throws NoSuchAlgorithmException {
    //     MessageDigest md = MessageDigest.getInstance("MD5");
    //     md.update(form.getPassword().getBytes());
    //     byte[] digest = md.digest();
    //     String passwordhash = DatatypeConverter
    //             .printHexBinary(digest).toUpperCase();
    //     System.out.println(passwordhash);

    //     return repository.findUserByLoginAndPasswordHashForOptional(form., passwordhash).orElseThrow(() -> new LoginFailException(form.getUsername()));

    // }

    @PostMapping(value="/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginForm test(@RequestBody LoginForm form) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(form.getPassword().getBytes());
        byte[] digest = md.digest();
        String passwordhash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        form.setPassword(passwordhash);
        return form;
    }
}
//
//        if (user == null) {
//            PrimeFaces.current().executeScript("setMessageError('Tài khoản không tồn tại')");
//            return;
//        }
//        if (!EncryptedPassword.checkPassword(passwordSignin, user.getPassword())) {
//            PrimeFaces.current().executeScript("setMessageError('Mật khẩu không chính xác')");
//            return;
//        }
//        if (user.getUserStatus().equals(UserStatusConstant.BAN)) {
//            PrimeFaces.current().executeScript("setMessageError('Tài khoản bị khóa, hãy liên hệ quản trị viên')");
//            return;
//        }
//        if (user!= null && EncryptedPassword.checkPassword(passwordSignin, user.getPassword())) {
//            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(usernameSignin, passwordSignin);
//            SecurityContextHolder.getContext().setAuthentication(token);
//            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//            try {
//                ec.redirect(ec.getRequestContextPath() + "/homepage.xhtml");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    @PostMapping("/login")
//
//    public ResponseEntity<UserVo> login(@RequestBody TraineeDTO traineeDTO) throws URISyntaxException {
//        log.debug("REST request to save Trainee : {}", traineeDTO);
//
//        TraineeDTO result = traineeService.save(traineeDTO);
//        return ResponseEntity.created(new URI("/api/trainees/" + result.getId()))
//                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
//        return new ResponseEntity<UserVo>(userService.findUserByLoginAndPasswordHash(username,password), HttpStatus.OK);
//    }
