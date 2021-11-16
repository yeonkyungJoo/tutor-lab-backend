package com.tutor.tutorlab.modules.log.vo;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Document(collection = "logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class Log {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String sessionId;
    private String osType;
    private String accessPath;
    private String ip;
    private String lastAccessAt;

    private Long userId;
    private String username;
    private String loginAt;

    @Builder(access = AccessLevel.PRIVATE)
    private Log(String loginAt, String osType, String accessPath, String ip, String lastAccessAt, Long userId, String username) {
        this.loginAt = loginAt;
        this.osType = osType;
        this.accessPath = accessPath;
        this.ip = ip;
        this.lastAccessAt = lastAccessAt;
        this.userId = userId;
        this.username = username;
    }

    public static Log of(String loginAt, String osType, String accessPath, String ip, String lastAccessAt, Long userId, String username) {
        return Log.builder()
                .loginAt(loginAt)
                .osType(osType)
                .accessPath(accessPath)
                .ip(ip)
                .lastAccessAt(lastAccessAt)
                .userId(userId)
                .username(username)
                .build();
    }
}
