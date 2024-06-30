package com.example.userservice.security.data;

import com.example.userservice.constant.ExceptionMessage;
import com.example.userservice.entity.User;
import com.example.userservice.exception.CommonRuntimeException;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    return CommonRuntimeException.builder()
                            .status(HttpStatus.NOT_FOUND)
                            .message(ExceptionMessage.ERROR_USER_NOT_FOUND)
                            .build();
                });

        AuthUser ret = new AuthUser(user.getUsername(), user.getPassword(), user.getRoles());
        ret.setId(user.getId());
        ret.setEmail(user.getEmail());

        return ret;
    }
}
