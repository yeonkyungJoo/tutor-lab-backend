package com.tutor.tutorlab.modules.account.vo;

import com.tutor.tutorlab.config.security.oauth.provider.OAuthType;
import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @Column(nullable = false, unique = true, updatable = false)
    private String username;    // 이메일
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;
    private String gender;
    private String phoneNumber;
    private String email;
    private String nickname;
    private String bio;         // 소개글

    // private Zone zone;
    private String zone;        // 지역
//    @Enumerated(EnumType.STRING)
//    private RoleType role;
    private String role;

    @Enumerated(EnumType.STRING)
    private OAuthType provider;
    private String providerId;

    @Builder
    public User(String username, String password, String name, String gender, String phoneNumber, String email, String nickname, String bio, String zone, String role,
                LocalDateTime createdAt, OAuthType provider, String providerId) {
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
