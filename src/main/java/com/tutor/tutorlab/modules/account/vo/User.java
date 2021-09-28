package com.tutor.tutorlab.modules.account.vo;

import com.tutor.tutorlab.config.security.oauth.provider.OAuthType;
import com.tutor.tutorlab.modules.account.enums.GenderType;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.base.BaseEntity;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Where(clause = "deleted = false and email_verified = true")
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

    private LocalDate birth;

    private String phoneNumber;
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String image;       // 프로필 이미지

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

    private boolean emailVerified = false;
    private String emailVerifyToken;
    private LocalDateTime emailVerifiedAt;

    private boolean deleted = false;
    private LocalDateTime deletedAt;

    // TODO - Notification과 양방향

    @Builder
    public User(String username, String password, String name, String gender, String birth, String phoneNumber, String email, String nickname, String bio, String zone, String image,
                RoleType role, OAuthType provider, String providerId) {
        this.username = username;
        this.password = password;
        this.name = name;
        if (!StringUtils.isBlank(gender)) {
            this.gender = gender.equals("MALE") ? GenderType.MALE : GenderType.FEMALE;
        } else {
            this.gender = null;
        }
        this.birth = LocalDateTimeUtil.getStringToDate(birth);
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nickname = nickname;
        this.bio = bio;
        this.zone = zone;
        this.image = image;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    public void quit() {
        setDeleted(true);
        setDeletedAt(LocalDateTime.now());
    }

    // TODO - CHECK : pre or post
    @PrePersist
    public void generateEmailVerifyToken() {
        this.emailVerifyToken = UUID.randomUUID().toString();
    }

    public void verifyEmail() {
        if (isEmailVerified()) {
            // TODO - 예외
        }
        setEmailVerified(true);
        setEmailVerifiedAt(LocalDateTime.now());
    }

}
