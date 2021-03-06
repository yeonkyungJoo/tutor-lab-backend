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
import com.tutor.tutorlab.modules.account.enums.RoleType;
import com.tutor.tutorlab.modules.account.repository.TuteeRepository;
import com.tutor.tutorlab.modules.account.repository.UserRepository;
import com.tutor.tutorlab.modules.account.vo.Tutee;
import com.tutor.tutorlab.modules.account.vo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
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

import static com.tutor.tutorlab.config.exception.AlreadyExistException.ID;
import static com.tutor.tutorlab.config.exception.AlreadyExistException.NICKNAME;
import static com.tutor.tutorlab.config.exception.EntityNotFoundException.EntityType.USER;
import static com.tutor.tutorlab.config.exception.OAuthAuthenticationException.UNPARSABLE;
import static com.tutor.tutorlab.config.exception.OAuthAuthenticationException.UNSUPPORTED;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {

    @Value("${server.ip}")
    private String ip;
    @Value("${server.port}")
    private Integer port;

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

    public boolean checkUsernameDuplication(String username) {
        boolean duplicated = false;

        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException();
        }

        User user = userRepository.findAllByUsername(username);
        if (user != null) {
            duplicated = true;
        }
        return duplicated;
    }

    public boolean checkNicknameDuplication(String nickname) {
        boolean duplicated = false;

        if (StringUtils.isBlank(nickname)) {
            throw new IllegalArgumentException();
        }

        User user = userRepository.findAllByNickname(nickname);
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
                // ?????????
                return loginOAuth(user);
            }
        }

        return null;
    }

    public Map<String, String> oauth(String provider, String code) {

        Map<String, String> result = null;

        OAuthInfo oAuthInfo = getOAuthInfo(provider, code);
        if (oAuthInfo != null) {

            User user = userRepository.findByProviderAndProviderId(oAuthInfo.getProvider(), oAuthInfo.getProviderId());
            if (user != null) {
                // ?????? ????????? ??????????????? ?????? ????????? ??????
                result = loginOAuth(user);
            } else {
                // ???????????? ??? ?????? ?????????
                // ?????? ?????? ??????
                // ?????? ?????? ?????? ??????
                result = signUpOAuth(oAuthInfo);
            }
        }

        log.info("#oauth-result : " + result);
        return result;
    }

    public OAuthInfo getOAuthInfo(String provider, AuthorizeResult authorizeResult) {

        OAuthInfo oAuthInfo = null;

        OAuthType oAuthType = OAuthInfo.getOAuthType(provider);
        if (oAuthType == null) {
            throw new OAuthAuthenticationException(UNSUPPORTED);
        }

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
                throw new OAuthAuthenticationException(UNSUPPORTED);
        }

        if (oAuthInfo == null) {
            throw new OAuthAuthenticationException(UNPARSABLE);
        }

        return oAuthInfo;
    }

    public OAuthInfo getOAuthInfo(String provider, String code) {

        OAuthInfo oAuthInfo = null;

        OAuthType oAuthType = OAuthInfo.getOAuthType(provider);
        if (oAuthType == null) {
            throw new OAuthAuthenticationException(UNSUPPORTED);
        }

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
                throw new OAuthAuthenticationException(UNSUPPORTED);
        }

        if (oAuthInfo == null) {
            throw new OAuthAuthenticationException(UNPARSABLE);
        }

        return oAuthInfo;
    }

    public Map<String, String> signUpOAuth(OAuthInfo oAuthInfo) {

        String username = oAuthInfo.getEmail();
        if (checkUsernameDuplication(username)) {
            throw new AlreadyExistException(ID);
        }

        User user = User.of(
                username,
                bCryptPasswordEncoder.encode(username),
                oAuthInfo.getName(),
                null,
                null,
                null,
                null,
                username,
                null,
                null,
                null,
                RoleType.TUTEE,
                oAuthInfo.getProvider(),
                oAuthInfo.getProviderId()
        );
        // ?????? ??????
        user.verifyEmail();

        Tutee tutee = Tutee.of(user);
        tuteeRepository.save(tutee);
        // ?????? ?????????
        return loginOAuth(user);
    }

    public Map<String, String> loginOAuth(User user) {
        String username = user.getUsername();
        return login(username, username);
    }

    public void signUpOAuthDetail(User user, SignUpOAuthDetailRequest signUpOAuthDetailRequest) {

        user = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(USER));

        // TODO - ?????? : OAuth??? ????????? ????????? ?????? ??????
        if (user.getProvider() == null || StringUtils.isBlank(user.getProviderId())) {
            throw new RuntimeException("OAuth??? ????????? ????????? ????????????.");
        }

        if (checkNicknameDuplication(signUpOAuthDetailRequest.getNickname())) {
            throw new AlreadyExistException(NICKNAME);
        }

        user.updateOAuthDetail(signUpOAuthDetailRequest);
    }

    public User signUp(SignUpRequest signUpRequest) {

        String username = signUpRequest.getUsername();
        if (checkUsernameDuplication(username)) {
            throw new AlreadyExistException(ID);
        }

        User user = User.of(
                username,
                bCryptPasswordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getName(),
                signUpRequest.getGender(),
                signUpRequest.getBirthYear(),
                signUpRequest.getPhoneNumber(),
                signUpRequest.getEmail(),
                signUpRequest.getNickname(),
                signUpRequest.getBio(),
                signUpRequest.getZone(),
                signUpRequest.getImage(),
                RoleType.TUTEE,
                null,
                null
        );
        User unverified = userRepository.save(user);

        // TODO - ??????
        Map<String, Object> variables = new HashMap<>();
        variables.put("host", String.format("http://%s:%d", ip, port));
        variables.put("link", "/verify-email?email=" + unverified.getUsername() + "&token=" + unverified.getEmailVerifyToken());
        variables.put("content", "Welcome! We recently received a request to create an account. To verify that you made this request, we're sending this confirmation email.");

        String content = templateEngine.process("verify-email", getContext(variables));
        sendEmail(unverified.getUsername(), "Welcome to TUTORLAB, please verify your email!", content);

        return unverified;
    }

    public Tutee verifyEmail(String email, String token) {

        User user = userRepository.findUnverifiedUserByUsername(email)
                .orElseThrow(() -> new RuntimeException("?????? ????????? ????????? ???????????? ???????????? ????????????."));

        if (token.equals(user.getEmailVerifyToken())) {
            user.verifyEmail();

            Tutee tutee = Tutee.of(user);
            return tuteeRepository.save(tutee);
        }
        throw new RuntimeException("?????? ??????");
        // return null;
    }

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
        EmailMessage emailMessage = EmailMessage.builder()
                .to(to)
                .subject(subject)
                .content(content)
                .build();
        emailService.send(emailMessage);
    }

    private Authentication authenticate(String username, String password) {

        try {
            // SecurityContextHolder.getContext().setAuthentication(authentication);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

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

            Object principal = authentication.getPrincipal();
            if (principal instanceof PrincipalDetails) {

                PrincipalDetails principalDetails = (PrincipalDetails) principal;

                Map<String, Object> claims = new HashMap<>();
                claims.put("username", username);
                String jwtToken = jwtTokenManager.createToken(principalDetails.getUsername(), claims);

                // lastLoginAt
                principalDetails.getUser().login();
                return jwtTokenManager.convertTokenToMap(jwtToken);
            }
        }

        // TODO - CHECK : ?????? ??????
        return null;
    }

    public Map<String, String> login(LoginRequest request) {
        return this.login(request.getUsername(), request.getPassword());
    }

    public void findPassword(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(USER));

        // ?????? ??????????????? ??????
        String randomPassword = generateRandomPassword(10);
        user.updatePassword(bCryptPasswordEncoder.encode(randomPassword));

        // ?????? ??????????????? ?????? ????????? ??????
        // TODO - ??????
        Map<String, Object> variables = new HashMap<>();
        variables.put("host", String.format("http://{}:{}", ip, port));
        variables.put("password", randomPassword);

        String content = templateEngine.process("find-password", getContext(variables));
        sendEmail(user.getUsername(), "Welcome to TUTORLAB, find your password!", content);
    }

    private String generateRandomPassword(int count) {
        return RandomStringUtils.randomAlphanumeric(count);
    }

}
