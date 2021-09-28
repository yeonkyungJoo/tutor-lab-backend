package com.tutor.tutorlab.modules.account.controller.response;

import com.tutor.tutorlab.modules.account.vo.User;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.Data;

@Data
public class UserResponse {

    private String username;
    // private String password;
    private String name;
    private String gender;
    private String birth;
    private String phoneNumber;
    private String email;
    private String nickname;
    private String bio;

    private String zone;

    public UserResponse(User user) {
        this.username = user.getUsername();
        this.name = user.getName();
        this.gender = user.getGender().toString();
        this.birth = LocalDateTimeUtil.getDateToString(user.getBirth());
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.bio = user.getBio();
        this.zone = user.getZone();
    }
}
