package com.example.userservice.mapper;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends AbstractMapper<User, UserDto> {
    @Override
    public UserDto toDto(User user) {
        UserDto userDto = super.toDto(user);

        if(user.getAvatar() != null) {
            userDto.setAvatarUrl(user.getAvatar().getUrl());
        }

        if(user.getSex() == null) {
            userDto.setSex("Other");
        }
        else {
            userDto.setSex(user.getSex()?"Male":"Female");
        }
        return userDto;
    }
    @Override
    public Class<UserDto> getDtoClass() {
        return UserDto.class;
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }


}
