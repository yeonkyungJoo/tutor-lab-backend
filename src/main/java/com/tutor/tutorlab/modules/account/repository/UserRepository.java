package com.tutor.tutorlab.modules.account.repository;

import com.tutor.tutorlab.config.security.oauth.provider.OAuthType;
import com.tutor.tutorlab.modules.account.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query(value = "select * from user where username = :username", nativeQuery = true)
    User findAllByUsername(@Param("username") String username);

    User findByProviderAndProviderId(OAuthType provider, String providerId);

    @Query(value = "select * from user where username = :username and email_verified = false", nativeQuery = true)
    Optional<User> findUnverifiedUserByUsername(@Param("username") String username);

    User findByName(String name);

    @Query(value = "select * from user where name = :name", nativeQuery = true)
    User findAllByName(@Param("name") String name);
}
