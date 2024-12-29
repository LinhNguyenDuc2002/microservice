package com.example.userservice.security.data;

import com.example.userservice.constant.I18nMessage;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException(I18nMessage.ERROR_USERNAME_NOT_FOUND);
                });

        AuthUser ret = new AuthUser(user.getId(), user.getUsername(), user.getPassword(), user.getRoles(), user.isStatus());
        if(StringUtils.hasText(user.getToken())) {
            ret.setRefreshToken(user.getToken());
        }
        ret.setEmail(user.getEmail());

        return ret;
    }
}
