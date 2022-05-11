package com.example.tumiweb.application.dai;

import com.example.tumiweb.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User findByEmail(String email);

  User findByUsername(String username);

  Set<User> findAllByDeleteFlag(boolean flag);

  User findByTokenResetPass(String tokenResetPass);

  List<User> findAllByUsernameContainingOrEmailContainingOrPhoneContaining(String key, String key2, String key3);

  List<User> findAllByBirthdayAndDeleteFlagAndActiveFlag(String birthday, boolean deleteFlag, boolean activeFlag);

}
