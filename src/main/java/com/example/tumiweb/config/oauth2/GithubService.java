package com.example.tumiweb.config.oauth2;

import com.example.tumiweb.application.constants.AuthenticationProvider;
import com.example.tumiweb.application.services.IRoleService;
import com.example.tumiweb.application.services.IUserService;
import com.example.tumiweb.application.services.MyUserDetailsService;
import com.example.tumiweb.application.utils.JwtUtil;
import com.example.tumiweb.domain.entity.Role;
import com.example.tumiweb.domain.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GithubService {

  @Autowired
  private IRoleService roleService;
  @Autowired
  private IUserService userService;
  @Autowired
  private MyUserDetailsService myUserDetailsService;
  @Autowired
  private JwtUtil jwtUtil;

  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  String saveUserWithFirstLogin(OAuth2AuthenticationToken token) {
    long id = Long.parseLong(token.getPrincipal().getAttributes().get("id").toString());
    User user = userService.getUserByUsername((String) token.getPrincipal().getAttributes().get("login"));

    if (user != null && user.getPhone().compareTo(String.valueOf(id)) == 0) {
      final UserDetails userDetails = myUserDetailsService.loadUserByUsername(user.getUsername());
      return jwtUtil.generateToken(userDetails);
    }

    user = new User();
    user.setAuthProvider(AuthenticationProvider.GITHUB);
    user.setPhone(String.valueOf(id));
    user.setFullName(token.getPrincipal().getName());
    user.setUsername((String) token.getPrincipal().getAttributes().get("login"));
    user.setPassword(passwordEncoder().encode((CharSequence) token.getPrincipal().getAttributes().get("login")));
    user.setAvatar((String) token.getPrincipal().getAttributes().get("avatar_url"));
    user.setEmail(token.getPrincipal().getAttributes().get("login") + "@gmail.com");
    user.setMark(0L);
    Role role = roleService.getRoleByName("ROLE_MEMBER");
    user.setRoles(Set.of(role));

    User newUser = userService.save(user);

    final UserDetails userDetails = myUserDetailsService.loadUserByUsername(newUser.getUsername());

    return jwtUtil.generateToken(userDetails);
  }

}
