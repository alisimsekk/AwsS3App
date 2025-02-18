package com.simsekali.awss3demo.service;

import com.simsekali.awss3demo.controller.converter.UserConverter;
import com.simsekali.awss3demo.controller.dto.UserDto;
import com.simsekali.awss3demo.controller.dto.UserProfilePhotoUpdateRequest;
import com.simsekali.awss3demo.controller.dto.UserCreateRequest;
import com.simsekali.awss3demo.exception.UserAlreadyExist;
import com.simsekali.awss3demo.exception.UserNotFoundExecption;
import com.simsekali.awss3demo.model.User;
import com.simsekali.awss3demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class UserService {

    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public UserDto createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExist(String.format("User with email %s already exists", request.getEmail()));
        }

        String profilePhotoKey = s3Service.uploadFile(request.getProfilePhoto());

        User newUser = User.create(request, profilePhotoKey);

        return userConverter.toUserDto(userRepository.save(newUser));
    }

    public UserDto findByEmail(String email) {
        User userFromDb = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundExecption(String.format("User not found with email %s", email)));
        return userConverter.toUserDto(userFromDb);
    }

    public UserDto updateProfilePhoto(Long id, UserProfilePhotoUpdateRequest request) {
        User userFromDb = userRepository.findById(id).orElseThrow(() -> new UserNotFoundExecption(String.format("User not found with id %s", id)));

        if (!ObjectUtils.isEmpty(userFromDb.getProfilePhotoKey())) {
            s3Service.deleteFile(userFromDb.getProfilePhotoKey());
        }

        String updatedProfilePhotoKey = s3Service.uploadFile(request.getProfilePhoto());
        userFromDb.setProfilePhotoKey(updatedProfilePhotoKey);
        return userConverter.toUserDto(userRepository.save(userFromDb));
    }
}
