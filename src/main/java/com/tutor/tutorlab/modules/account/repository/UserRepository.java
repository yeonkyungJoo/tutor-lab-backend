package com.tutor.tutorlab.modules.account.repository;

import com.tutor.tutorlab.config.security.oauth.provider.OAuthType;
import com.tutor.tutorlab.modules.account.vo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByDeleted(boolean deleted);
    Page<User> findByDeleted(boolean deleted, Pageable pageable);
    Optional<User> findByDeletedAndId(boolean deleted, Long userId);
    User findByUsername(String username);
    User findByProviderAndProviderId(OAuthType provider, String providerId);

    User findByName(String name);
}
