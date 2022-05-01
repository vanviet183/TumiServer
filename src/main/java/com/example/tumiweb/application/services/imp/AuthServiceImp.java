package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.adapter.web.v1.transfer.parameter.AuthenticationRequest;
import com.example.tumiweb.adapter.web.v1.transfer.response.AuthenticationResponse;
import com.example.tumiweb.application.constants.AuthenticationProvider;
import com.example.tumiweb.application.mapper.UserMapper;
import com.example.tumiweb.application.services.*;
import com.example.tumiweb.application.utils.JwtUtil;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.UserDTO;
import com.example.tumiweb.domain.entity.Diary;
import com.example.tumiweb.domain.entity.Role;
import com.example.tumiweb.domain.entity.User;
import org.mapstruct.factory.Mappers;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.InvalidObjectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class AuthServiceImp implements IAuthService {
  private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
  private final JwtUtil jwtUtil;
  private final MyUserDetailsService myUserDetailsService;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final IUserService userService;
  private final IRoleService roleService;
  private final IDiaryService diaryService;

  public AuthServiceImp(JwtUtil jwtUtil, MyUserDetailsService myUserDetailsService,
                        AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
                        IUserService userService, IRoleService roleService, IDiaryService diaryService) {
    this.jwtUtil = jwtUtil;
    this.myUserDetailsService = myUserDetailsService;
    this.authenticationManager = authenticationManager;
    this.passwordEncoder = passwordEncoder;
    this.userService = userService;
    this.roleService = roleService;
    this.diaryService = diaryService;
  }

  @Override
  public AuthenticationResponse login(AuthenticationRequest request) throws VsException {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
          request.getUsername(), request.getPassword()
      ));
    } catch (BadCredentialsException e) {
      throw new VsException("Incorrect username or password");
    }

    final UserDetails userDetails = myUserDetailsService.loadUserByUsername(request.getUsername());
    final String jwt = jwtUtil.generateToken(userDetails);

    User user = userService.getUserByUsername(request.getUsername());
    List<String> roles = new ArrayList<>();
    Set<Role> roleSet = user.getRoles();
    if (roleSet.size() > 0) {
      roleSet.forEach(item -> roles.add(item.getName()));
    }

    UserDetails principal = myUserDetailsService.loadUserByUsername(user.getUsername());
    Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(),
        principal.getAuthorities());
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);

    //lưu lại lịch sử
    diaryService.createNewDiary(user.getId());

    return new AuthenticationResponse(jwt, user.getId(), user.getUsername(), roles);
  }

  @Override
  public AuthenticationResponse signup(UserDTO userDTO) throws InvalidObjectException {
    User oldUser = userService.getUserByUsername(userDTO.getUsername());
    if (oldUser != null) {
      throw new VsException("Username has already exists");
    }
    User user = userMapper.toUser(userDTO);
    if (user == null) {
      throw new InvalidObjectException("Invalid user");
    }
    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

    //gán role member cho user mới lập
    Role role = roleService.getRoleByName("ROLE_MEMBER");
    user.setRoles(Set.of(role));

    // set provider
    user.setAuthProvider(AuthenticationProvider.LOCAL);
    User newUser = userService.save(user);

    final UserDetails userDetails = myUserDetailsService.loadUserByUsername(newUser.getUsername());
    final String jwt = jwtUtil.generateToken(userDetails);

    return new AuthenticationResponse(jwt, newUser.getId(), newUser.getUsername(), List.of(role.getName()));
  }

  @Override
  public Boolean logoutHandler(Long id) {
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
      List<Diary> diaries = new ArrayList<>(diaryService.findAllByUserIdAndOnDay(id,
          simpleDateFormat.format(new Date())));

      diaryService.editDiaryById(diaries.get(diaries.size() - 1).getId());
      return true;
    } catch (Exception ex) {
      return false;
    }
  }

  @Override
  public Boolean validateToken(AuthenticationResponse authenticationResponse) {
    try {
      String jwt = authenticationResponse.getJwt();
      String username = jwtUtil.extractUsername(jwt);
      UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

      return jwtUtil.validateToken(jwt, userDetails);
    } catch (Exception e) {
      return false;
    }
  }

}