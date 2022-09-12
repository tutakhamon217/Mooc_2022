package fpt.capstone.security.services;

import fpt.capstone.entities.User;
import fpt.capstone.repository.UserRepository;
import fpt.capstone.service.UserService;
import fpt.capstone.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

/*
chứa info user get from UserDetailServiceImpl
 */
public class UserDetailsImpl implements UserDetails {


    private Integer id;
    private String login;
    private String passWord;
    private String avatar;
    private String fullName;
    private String email;
    private String phone;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetails build(User user) {
        List<GrantedAuthority> authorities = user.getAuthorities().stream().map(authority -> new SimpleGrantedAuthority(authority.getCode())).collect(Collectors.toList());
        return new UserDetailsImpl(
                user.getId(),
                user.getLogin(),
                user.getPasswordHash(),
                user.getImageUrl(),
                user.getFullName(),
                user.getEmail(),
                user.getPhoneNumber(),
                authorities);
    }

    public UserDetailsImpl(int id, String login, String passWord, String avatar, String fullName, String email, String phone, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.login = login;
        this.passWord = passWord;
        this.avatar = avatar;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.authorities = authorities;
    }


    public void setLogin(String login) {
        this.login = login;
    }


    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return passWord;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
