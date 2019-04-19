package com.hancai.browser.authentication.service;

import com.hancai.core.module.user.dto.User;
import com.hancai.core.module.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Spring Security 获取用户
 *
 * @author diaohancai
 */
@Component
public class DhcUserDetailsService implements UserDetailsService {

    private UserService userService;

    @Autowired
    public DhcUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*
         * UserDetailsService 只负责获取用户，并不负责用户认证
         */
        User user = userService.getUserByName(username);

        if(user == null) {
            throw new UsernameNotFoundException("找不到用户:" + username);
        }

        return new org.springframework.security.core.userdetails.User(
                username, user.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    }

}
