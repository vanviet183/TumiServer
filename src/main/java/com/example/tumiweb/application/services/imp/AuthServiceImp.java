package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.adapter.web.v1.transfer.parameter.auth.AuthenticationRequest;
import com.example.tumiweb.adapter.web.v1.transfer.response.AuthenticationResponse;
import com.example.tumiweb.adapter.web.v1.transfer.response.TrueFalseResponse;
import com.example.tumiweb.application.constants.AuthenticationProvider;
import com.example.tumiweb.application.constants.RoleConstant;
import com.example.tumiweb.application.mapper.UserMapper;
import com.example.tumiweb.application.services.*;
import com.example.tumiweb.application.utils.DateTimeUtil;
import com.example.tumiweb.application.utils.JwtUtil;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.UserDTO;
import com.example.tumiweb.domain.entity.Diary;
import com.example.tumiweb.domain.entity.Role;
import com.example.tumiweb.domain.entity.User;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    if (CollectionUtils.isNotEmpty(roleSet)) {
      roles = List.copyOf(roles);
    }

    diaryService.createNewDiary(user.getId());

    return new AuthenticationResponse(jwt, user.getId(), user.getUsername(), roles);
  }

  @Override
  public AuthenticationResponse signup(UserDTO userDTO) {
    User oldUser = userService.getUserByUsername(userDTO.getUsername());
    if (oldUser != null) {
      throw new VsException("Username has already exists");
    }
    User user = userMapper.toUser(userDTO, null);
    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

    Role role = roleService.getRoleByName(RoleConstant.STUDENT_NAME);
    user.setRoles(Set.of(role));

    user.setAuthProvider(AuthenticationProvider.LOCAL);
    User newUser = userService.save(user);

    final UserDetails userDetails = myUserDetailsService.loadUserByUsername(newUser.getUsername());
    final String jwt = jwtUtil.generateToken(userDetails);

    return new AuthenticationResponse(jwt, newUser.getId(), newUser.getUsername(), List.of(role.getName()));
  }

  @Override
  public TrueFalseResponse logoutHandler(Long id) {
    try {
      List<Diary> diaries = new ArrayList<>(diaryService.findAllByUserIdAndOnDay(id,
          DateTimeUtil.getDateTimeNow()));

      diaryService.editDiaryById(diaries.get(diaries.size() - 1).getId());
      return new TrueFalseResponse(true);
    } catch (Exception ex) {
      return new TrueFalseResponse(false);
    }
  }

  @Override
  public TrueFalseResponse validateToken(AuthenticationResponse authenticationResponse) {
    try {
      String jwt = authenticationResponse.getJwt();
      String username = jwtUtil.extractUsername(jwt);
      UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

      return new TrueFalseResponse(jwtUtil.validateToken(jwt, userDetails));
    } catch (Exception e) {
      return new TrueFalseResponse(false);
    }
  }

}