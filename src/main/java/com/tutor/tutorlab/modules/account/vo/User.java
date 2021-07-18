package com.tutor.tutorlab.modules.account.vo;

import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
public class User extends BaseEntity {

    private String username;    // 이메일
    private String password;

    private String name;
    private String gender;
    private String phoneNumber;
    private String email;
    private String nickname;
    private String bio;         // 소개글

    // private Zone zone;
    private String zone;        // 지역
    private String role;       // ROLE_USER,ROLE_ADMIN

    private String provider;
    private String providerId;

    @Builder
    public User(String username, String password, String name, String gender, String phoneNumber, String email, String nickname, String bio, String zone, String role,
                LocalDateTime createdAt, String provider, String providerId) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nickname = nickname;
        this.bio = bio;
        this.zone = zone;
        this.role = role;
        this.createdAt = createdAt;

        this.provider = provider;
        this.providerId = providerId;
    }

    public List<String> getRoleList() {
        if (this.role.length() > 0) {
            return Arrays.asList(this.role.split(","));
        }
        return new ArrayList<>();
    }

}
