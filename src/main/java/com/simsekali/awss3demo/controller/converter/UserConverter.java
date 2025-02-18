package com.simsekali.awss3demo.controller.converter;

import com.simsekali.awss3demo.controller.dto.UserDto;
import com.simsekali.awss3demo.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .profilePhotoKey(user.getProfilePhotoKey())
                .build();
    }
}
