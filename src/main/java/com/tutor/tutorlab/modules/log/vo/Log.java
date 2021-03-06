package com.tutor.tutorlab.modules.log.vo;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter //@Setter
@Document(collection = "logs")
public class Log {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    // TODO - sessionId를 왜 저장해야하는가
    private String sessionId;
    private String osType;
    private String accessPath;
    private String ip;
    private String lastAccessAt;

    private Long userId;
    private String username;
    private String loginAt;

    @Builder(access = AccessLevel.PRIVATE)
    public Log(String sessionId, String osType, String accessPath, String ip, String lastAccessAt, Long userId, String username, String loginAt) {
        this.sessionId = sessionId;
        this.osType = osType;
        this.accessPath = accessPath;
        this.ip = ip;
        this.lastAccessAt = lastAccessAt;
        this.userId = userId;
        this.username = username;
        this.loginAt = loginAt;
    }

    public static Log of(String sessionId, String osType, String accessPath, String ip, String lastAccessAt, Long userId, String username, String loginAt) {
        return Log.builder()
                .sessionId(sessionId)
                .osType(osType)
                .accessPath(accessPath)
                .ip(ip)
                .lastAccessAt(lastAccessAt)
                .userId(userId)
                .username(username)
                .loginAt(loginAt)
                .build();
    }
}
