package com.tutor.tutorlab.modules.account.vo;

import com.tutor.tutorlab.config.security.oauth.provider.OAuthType;
import com.tutor.tutorlab.modules.account.enums.GenderType;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
@Entity
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, updatable = false)
    private String username;    // 이메일
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    private String phoneNumber;
    private String email;
    private String nickname;

    @Lob
    private String bio;         // 소개글

    // private Zone zone;
    private String zone;        // 지역

    @Enumerated(EnumType.STRING)
    private RoleType role;
/*    @ColumnDefault("'ROLE_TUTEE'")
    private String role;*/

    @Enumerated(EnumType.STRING)
    private OAuthType provider;
    private String providerId;

    private boolean deleted = false;
    private LocalDateTime deletedAt;

    @Builder
    public User(String username, String password, String name, String gender, String phoneNumber, String email, String nickname, String bio, String zone, RoleType role,
                OAuthType provider, String providerId) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.gender = gender == "MALE" ? GenderType.MALE : GenderType.FEMALE;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nickname = nickname;
        this.bio = bio;
        this.zone = zone;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    public void quit() {
        setDeleted(true);
        setDeletedAt(LocalDateTime.now());
    }

}
