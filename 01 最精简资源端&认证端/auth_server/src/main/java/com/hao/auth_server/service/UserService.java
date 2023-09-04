package com.hao.auth_server.service;

//import com.hao.auth_demo.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user01 = new User("admin", passwordEncoder.encode("123456"), AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        User user02 = new User("hao", passwordEncoder.encode("1234"), AuthorityUtils.commaSeparatedStringToAuthorityList("add"));
        if ("admin".equals(username)) {
            return user01;
        } else if ("hao".equals(username)) {
            return user02;
        } else {
            throw new UsernameNotFoundException("用户名不存在");
        }
    }
}
