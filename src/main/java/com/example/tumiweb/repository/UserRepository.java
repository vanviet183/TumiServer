package com.example.tumiweb.repository;

import com.example.tumiweb.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByUsername(String username);
    Set<User> findAllByStatus(boolean status);
    User findByTokenResetPass(String tokenResetPass);

    //Search user by username or email or phone contain key(String)
    List<User> findAllByUsernameContainingOrEmailContainingOrPhoneContaining(String key, String key2, String key3);
    //Find by birthday
    List<User> findAllByBirthday(String birthday);
}
