package com.example.tumiweb.application.services;

import com.example.tumiweb.application.dai.UserRepository;
import com.example.tumiweb.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class MyUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("Not found user by username: " + username);
    }

    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
    user.getRoles().forEach(item -> {
      grantedAuthorities.add(new SimpleGrantedAuthority(item.getName()));
    });

    return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
        grantedAuthorities);
  }

}
