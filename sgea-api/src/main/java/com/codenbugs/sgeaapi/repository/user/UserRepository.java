package com.codenbugs.sgeaapi.repository.user;

import com.codenbugs.sgeaapi.entity.login_test.UserTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserTest, Long> {
    Optional<UserTest> findByUserName(String username);

    Optional<UserTest> findByEmail(String email);
}