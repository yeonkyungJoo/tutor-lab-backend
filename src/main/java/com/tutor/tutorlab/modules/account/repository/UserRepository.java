package com.tutor.tutorlab.modules.account.repository;

import com.tutor.tutorlab.config.security.oauth.provider.OAuthType;
import com.tutor.tutorlab.modules.account.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUsername(String username);
    public User findByProviderAndProviderId(OAuthType provider, String providerId);
}
