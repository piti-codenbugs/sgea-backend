package com.codenbugs.sgeaapi.repository.user;

import com.codenbugs.sgeaapi.entity.users.Role;
import com.codenbugs.sgeaapi.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByFirstName(String username);

    Optional<User> findByEmail(String email);

    List<User> findAllByActiveIs(boolean active);

    List<User> findAllByActiveIsAndRole(boolean active, Role role);

    List<User> findAllByActiveIsAndRole_Name(boolean active, String roleName);
}