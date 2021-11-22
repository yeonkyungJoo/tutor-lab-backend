package com.tutor.tutorlab.modules.account.vo;

import com.tutor.tutorlab.config.security.oauth.provider.OAuthType;
import com.tutor.tutorlab.modules.account.enums.GenderType;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.address.embeddable.Address;
import com.tutor.tutorlab.modules.address.util.AddressUtils;
import com.tutor.tutorlab.modules.base.BaseEntity;
import com.tutor.tutorlab.utils.LocalDateTimeUtil;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Where(clause = "deleted = false and email_verified = true")
@ToString(callSuper = true)
//@EqualsAndHashCode(callSuper = true)
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private String birthYear;

    private String phoneNumber;
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String image;       // 프로필 이미지

    @Lob
    private String bio;         // 소개글

    // TODO - CHECK : 정적 테이블 관리
    @Embedded
    private Address zone;        // 지역
    // private String zone;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @Enumerated(EnumType.STRING)
    private OAuthType provider;
    private String providerId;

    private boolean emailVerified = false;
    private String emailVerifyToken;
    private LocalDateTime emailVerifiedAt;

    @Lob
    private String fcmToken;

    private boolean deleted = false;
    private LocalDateTime deletedAt;
    @Lob
    private String quitReason;

    private LocalDateTime lastLoginAt;

    // TODO - Notification과 양방향

    @Builder(access = AccessLevel.PRIVATE)
    private User(String username, String password, String name, String gender, String birthYear, String phoneNumber, String email, String nickname, String bio, String zone, String image,
                RoleType role, OAuthType provider, String providerId) {
        this.username = username;
        this.password = password;
        this.name = name;
        if (!StringUtils.isBlank(gender)) {
            this.gender = gender.equals("MALE") ? GenderType.MALE : GenderType.FEMALE;
        } else {
            this.gender = null;
        }
        this.birthYear = birthYear;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nickname = nickname;
        this.bio = bio;
        this.zone = AddressUtils.convertStringToEmbeddableAddress(zone);
        this.image = image;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    public static User of(String username, String password, String name, String gender, String birthYear, String phoneNumber, String email, String nickname, String bio, String zone, String image,
                                  RoleType role, OAuthType provider, String providerId) {
        return User.builder()
                .username(username)
                .password(password)
                .name(name)
                .gender(gender)
                .birthYear(birthYear)
                .phoneNumber(phoneNumber)
                .email(email)
                .nickname(nickname)
                .bio(bio)
                .zone(zone)
                .image(image)
                .role(role)
                .provider(provider)
                .providerId(providerId)
                .build();
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
