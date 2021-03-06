package com.example.tumiweb.config.oauth2;

import com.example.tumiweb.application.constants.AuthenticationProvider;
import com.example.tumiweb.application.constants.RoleConstant;
import com.example.tumiweb.application.services.IRoleService;
import com.example.tumiweb.application.services.IUserService;
import com.example.tumiweb.application.services.MyUserDetailsService;
import com.example.tumiweb.application.utils.JwtUtil;
import com.example.tumiweb.domain.entity.Role;
import com.example.tumiweb.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GoogleService {
  private final IRoleService roleService;
  private final IUserService userService;
  private final MyUserDetailsService myUserDetailsService;
  private final JwtUtil jwtUtil;

  public GoogleService(IRoleService roleService, IUserService userService, MyUserDetailsService myUserDetailsService,
                       JwtUtil jwtUtil) {
    this.roleService = roleService;
    this.userService = userService;
    this.myUserDetailsService = myUserDetailsService;
    this.jwtUtil = jwtUtil;
  }

  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  String saveUserWithFirstLogin(OAuth2AuthenticationToken token) {
    String email = token.getPrincipal().getAttributes().get("email").toString();
    String username = email.substring(0, email.indexOf('@'));

    User user = userService.getUserByUsername(username);

    if (user != null) {
      return jwtUtil.generateToken(myUserDetailsService.loadUserByUsername(username));
    }

    user = new User();
    user.setAuthProvider(AuthenticationProvider.GOOGLE);
    user.setPhone(token.getPrincipal().getAttributes().get("sub").toString().substring(0, 10));
    user.setFullName(token.getPrincipal().getAttributes().get("name").toString());

    user.setUsername(username);
    user.setPassword(passwordEncoder().encode(username));
    user.setAvatar((String) token.getPrincipal().getAttributes().get("picture"));
    user.setEmail(email);
    user.setMark(0L);

    Role role = roleService.getRoleByName(RoleConstant.STUDENT_NAME);
    user.setRoles(Set.of(role));

    User newUser = userService.save(user);

    final UserDetails userDetails = myUserDetailsService.loadUserByUsername(newUser.getUsername());

    return jwtUtil.generateToken(userDetails);
  }

}
