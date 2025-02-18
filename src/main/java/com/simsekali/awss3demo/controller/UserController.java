package com.simsekali.awss3demo.controller;

import com.simsekali.awss3demo.controller.dto.UserDto;
import com.simsekali.awss3demo.controller.dto.UserProfilePhotoUpdateRequest;
import com.simsekali.awss3demo.controller.dto.UserCreateRequest;
import com.simsekali.awss3demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@Valid @ModelAttribute UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @GetMapping
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
        UserDto user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/profilePhoto/{id}")
    public ResponseEntity<UserDto> updateProfilePhoto(@PathVariable Long id, @Valid @ModelAttribute UserProfilePhotoUpdateRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateProfilePhoto(id, request));
    }
}
