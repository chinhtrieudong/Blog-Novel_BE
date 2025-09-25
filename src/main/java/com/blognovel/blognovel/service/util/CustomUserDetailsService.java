package com.blognovel.blognovel.service.util;

import com.blognovel.blognovel.exception.AppException;
import com.blognovel.blognovel.exception.ErrorCode;
import com.blognovel.blognovel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.blognovel.blognovel.model.User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
        UserDetails userDetails = new User(user.getUsername(), user.getPassword(), Arrays.asList(grantedAuthority));
        return userDetails;
    }
}
