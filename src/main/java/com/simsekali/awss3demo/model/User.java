package com.simsekali.awss3demo.model;

import com.simsekali.awss3demo.controller.dto.UserCreateRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "\"user\"")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String profilePhotoKey;

    public static User create(UserCreateRequest request, String profilePhotoKey) {
        User user = new User();
        user.email = request.getEmail();
        user.firstName = request.getFirstName();
        user.lastName = request.getLastName();
        user.profilePhotoKey = profilePhotoKey;
        return user;
    }

}
