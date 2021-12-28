package com.tutor.tutorlab.modules.account.controller.response;

import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.modules.address.util.AddressUtils;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.Data;

@Data
public class UserResponse {

    private Long userId;
    private String username;
    // private String password;
    private RoleType role;
    private String name;
    private String gender;
    private String birthYear;
    private String phoneNumber;
    private String email;
    private String nickname;
    private String bio;
    private String image;
    private String zone;

    public UserResponse(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.name = user.getName();
        this.gender = user.getGender() != null ? user.getGender().toString() : null;
        this.birthYear = user.getBirthYear();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.bio = user.getBio();
        this.image = user.getImage();
        this.zone = AddressUtils.convertEmbeddableToStringAddress(user.getZone());
    }
}
