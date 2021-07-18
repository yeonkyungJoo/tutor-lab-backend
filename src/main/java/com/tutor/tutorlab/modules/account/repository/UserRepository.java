package com.tutor.tutorlab.modules.account.repository;

import com.tutor.tutorlab.modules.account.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUsername(String username);
    public User findByProviderAndProviderId(String provider, String providerId);
}
