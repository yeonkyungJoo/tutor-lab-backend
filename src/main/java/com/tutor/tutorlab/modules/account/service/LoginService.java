package com.tutor.tutorlab.modules.account.service;

import com.tutor.tutorlab.config.exception.AlreadyExistException;
import com.tutor.tutorlab.config.exception.EntityNotFoundException;
import com.tutor.tutorlab.config.exception.OAuthAuthenticationException;
import com.tutor.tutorlab.config.security.PrincipalDetails;
import com.tutor.tutorlab.config.security.jwt.JwtTokenManager;
import com.tutor.tutorlab.config.security.oauth.provider.AuthorizeResult;
import com.tutor.tutorlab.config.security.oauth.provider.OAuthInfo;
import com.tutor.tutorlab.config.security.oauth.provider.OAuthType;
import com.tutor.tutorlab.config.security.oauth.provider.google.GoogleInfo;
import com.tutor.tutorlab.config.security.oauth.provider.google.GoogleOAuth;
import com.tutor.tutorlab.config.security.oauth.provider.kakao.KakaoInfo;
import com.tutor.tutorlab.config.security.oauth.provider.kakao.KakaoOAuth;
import com.tutor.tutorlab.config.security.oauth.provider.kakao.KakaoResponse;
import com.tutor.tutorlab.config.security.oauth.provider.naver.NaverInfo;
import com.tutor.tutorlab.config.security.oauth.provider.naver.NaverOAuth;
import com.tutor.tutorlab.config.security.oauth.provider.naver.NaverResponse;
import com.tutor.tutorlab.mail.EmailMessage;
import com.tutor.tutorlab.mail.EmailService;
import com.tutor.tutorlab.modules.account.controller.request.LoginRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpOAuthDetailRequest;
import com.tutor.tutorlab.modules.account.controller.request.SignUpRequest;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.enums.GenderType;
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.USER;

@Slf4j
@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final TuteeRepository tuteeRepository;
    private final GoogleOAuth googleOAuth;
    private final KakaoOAuth kakaoOAuth;
    private final NaverOAuth naverOAuth;

    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenManager jwtTokenManager;

    private final EmailService emailService;
    private final TemplateEngine templateEngine;

    private boolean checkUsernameDuplication(String username) {
        boolean duplicated = false;

        User user = userRepository.findByUsername(username);
        if (user != null) {
            duplicated = true;
        }
        return duplicated;
    }

    public Map<String, String> processLoginOAuth(String provider, AuthorizeResult authorizeResult) {

        OAuthInfo oAuthInfo = getOAuthInfo(provider, authorizeResult);
        if (oAuthInfo != null) {

            User user = userRepository.findByProviderAndProviderId(oAuthInfo.getProvider(), oAuthInfo.getProviderId());
            if (user != null) {
                // 로그인
                return loginOAuth(user);
            }
        }

        return null;
    }

    // TODO - 리팩토링
    public OAuthInfo getOAuthInfo(String provider, AuthorizeResult authorizeResult) {

        OAuthInfo oAuthInfo = null;

        OAuthType oAuthType = OAuthInfo.getOAuthType(provider);
        switch (oAuthType) {
            case GOOGLE:
                // authorizeResult.getUser() -> Map<String, String>
                Map<String, String> googleOAuthUserInfo = googleOAuth.getUserInfo(authorizeResult.getUser());
                if (googleOAuthUserInfo != null) {
                    oAuthInfo = new GoogleInfo(googleOAuthUserInfo);
                }
                break;
            case KAKAO:
                break;
            case NAVER:
                break;
            default:
                throw new OAuthAuthenticationException("지원하지 않는 OAuth입니다.");
        }

        if (oAuthInfo == null) {
            throw new OAuthAuthenticationException("User 정보를 가져올 수 없습니다.");
        }

        return oAuthInfo;
    }

    // TODO - 리팩토링
    public OAuthInfo getOAuthInfo(String provider, String code) {

        OAuthInfo oAuthInfo = null;

        // convert
        OAuthType oAuthType = OAuthInfo.getOAuthType(provider);
        switch (oAuthType) {
            case GOOGLE:
                Map<String, String> googleOAuthUserInfo = googleOAuth.getUserInfo(code);
                if (googleOAuthUserInfo != null) {
                    oAuthInfo = new GoogleInfo(googleOAuthUserInfo);
                }
                break;
            case KAKAO:
                KakaoResponse kakaoOAuthUserInfo = kakaoOAuth.getUserInfo(code);
                if (kakaoOAuthUserInfo != null) {
                    oAuthInfo = new KakaoInfo(kakaoOAuthUserInfo);
                }
                break;
            case NAVER:
                NaverResponse naverOAuthUserInfo = naverOAuth.getUserInfo(code);
                if (naverOAuthUserInfo != null) {
                    oAuthInfo = new NaverInfo(naverOAuthUserInfo);
                }
                break;
            default:
                throw new OAuthAuthenticationException("지원하지 않는 OAuth입니다.");
        }

        if (oAuthInfo == null) {
            throw new OAuthAuthenticationException("User 정보를 가져올 수 없습니다.");
        }

        return oAuthInfo;
    }

    public Map<String, String> signUpOAuth(OAuthInfo oAuthInfo) {

        String username = oAuthInfo.getEmail();
        if (checkUsernameDuplication(username)) {
            throw new AlreadyExistException("동일한 ID가 존재합니다.");
        }

        User user = User.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(username))
                .name(oAuthInfo.getName())
                .gender(null)
                .phoneNumber(null)
                .email(null)
                .nickname(null)
                .bio(null)
                .zone(null)
                .role(RoleType.ROLE_TUTEE)
                .provider(oAuthInfo.getProvider())
                .providerId(oAuthInfo.getProviderId())
                .build();

        Tutee tutee = new Tutee(user);
        tuteeRepository.save(tutee);
        // 강제 로그인
        return loginOAuth(user);
    }

    public Map<String, String> loginOAuth(User user) {

        String username = user.getUsername();
        return login(username, username);
    }

    public void signUpOAuthDetail(User user, SignUpOAuthDetailRequest signUpOAuthDetailRequest) {

        user = userRepository.findByUsername(user.getUsername());

        // CHECK : OAuth로 가입한 회원이 아니라면?
        if (user.getProvider() == null || StringUtils.isBlank(user.getProviderId())) {
            throw new EntityNotFoundException("OAuth로 가입한 회원이 아닙니다.");
        }

        user.setGender(signUpOAuthDetailRequest.getGender() == "MALE" ? GenderType.MALE : GenderType.FEMALE);
        user.setPhoneNumber(signUpOAuthDetailRequest.getPhoneNumber());
        user.setEmail(signUpOAuthDetailRequest.getEmail());
        user.setNickname(signUpOAuthDetailRequest.getNickname());
        user.setBio(signUpOAuthDetailRequest.getBio());
        user.setZone(signUpOAuthDetailRequest.getZone());
        user.setImage(signUpOAuthDetailRequest.getImage());
    }

    public Tutee signUp(SignUpRequest signUpRequest) {

        String username = signUpRequest.getUsername();
        if (checkUsernameDuplication(username)) {
            throw new AlreadyExistException("동일한 ID가 존재합니다.");
        }

        User user = User.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(signUpRequest.getPassword()))
                .name(signUpRequest.getName())
                .gender(signUpRequest.getGender())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .email(signUpRequest.getEmail())
                .nickname(signUpRequest.getNickname())
                .bio(signUpRequest.getBio())
                .zone(signUpRequest.getZone())
                .image(signUpRequest.getImage())
                .role(RoleType.ROLE_TUTEE)
                .provider(null)
                .providerId(null)
                .build();

        // TODO - email-verify
        User unverified = userRepository.save(user);
        // TODO - builder
//        EmailMessage emailMessage = new EmailMessage();
//        emailMessage.setTo(unverified.getUsername());
//        emailMessage.setSubject("Welcome to TUTORLAB, please verify your email!");
//        // emailMessage.setContent("Welcome! We recently received a request to create an account. To verify that you made this request, we're sending this confirmation email.");
//        emailMessage.setContent("/verifyEmail?email=" + unverified.getUsername() + "&token=" + unverified.getEmailVerifyToken());
//        emailService.send(emailMessage);


        Map<String, Object> variables = new HashMap<>();
        // TODO - 상수
        variables.put("host", "http://localhost:8080");
        variables.put("link", "/verify-email?email=" + unverified.getUsername() + "&token=" + unverified.getEmailVerifyToken());
        variables.put("content", "Welcome! We recently received a request to create an account. To verify that you made this request, we're sending this confirmation email.");
        String content = templateEngine.process("verify-email", getContext(variables));
        
        // TODO - 상수
        sendEmail(unverified.getUsername(), "Welcome to TUTORLAB, please verify your email!", content);

        // TODO - CHECK : 튜티 생성?
        Tutee tutee = new Tutee(user);
        return tuteeRepository.save(tutee);
    }

    // TODO
    private Context getContext(Map<String, Object> variables) {

        Context context = new Context();

        Iterator<Map.Entry<String, Object>> iterator = variables.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> variable = iterator.next();
            context.setVariable(variable.getKey(), variable.getValue());
        }
        return context;
    }

    private void sendEmail(String to, String subject, String content) {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTo(to);
        emailMessage.setSubject(subject);
        emailMessage.setContent(content);
        emailService.send(emailMessage);
    }

    private Authentication authenticate(String username, String password) {

        try {

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            // SecurityContextHolder.getContext().setAuthentication(authentication);
            return authentication;

        } catch(BadCredentialsException e) {
            throw new BadCredentialsException("BadCredentialsException");
        } catch(DisabledException e) {
            throw new DisabledException("DisabledException");
        } catch(LockedException e) {
            throw new LockedException("LockedException");
        } catch(UsernameNotFoundException e) {
            throw new UsernameNotFoundException("UsernameNotFoundException");
        } catch(AuthenticationException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }

        return null;
    }

    public Map<String, String> login(String username, String password) {

        Authentication authentication = authenticate(username, password);
        if (authentication != null) {

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

            Map<String, Object> claims = new HashMap<>();
            claims.put("username", username);
            String jwtToken = jwtTokenManager.createToken(principalDetails.getUsername(), claims);
            return jwtTokenManager.convertTokenToMap(jwtToken);
        }

        // TODO - CHECK : 예외 처리
        return null;
    }

    public Map<String, String> login(LoginRequest request) {
        return this.login(request.getUsername(), request.getPassword());
    }

    public void verifyEmail(String email, String token) {

        User user = userRepository.findByUsername(email);
        if (token.equals(user.getEmailVerifyToken())) {
            user.verifyEmail();
        }
    }

    public void findPassword(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException(USER);
        }
        // 랜덤 비밀번호로 변경
        String randomPassword = generateRandomPassword(10);
        user.setPassword(bCryptPasswordEncoder.encode(randomPassword));

        // 랜덤 비밀번호가 담긴 이메일 전송
        Map<String, Object> variables = new HashMap<>();
        // TODO - 상수
        variables.put("host", "http://localhost:8080");
        variables.put("password", randomPassword);
        String content = templateEngine.process("find-password", getContext(variables));

        // TODO - 상수
        sendEmail(user.getUsername(), "Welcome to TUTORLAB, find your password!", content);
    }

    public String generateRandomPassword(int count) {
        return RandomStringUtils.randomAlphanumeric(count);
    }

}
