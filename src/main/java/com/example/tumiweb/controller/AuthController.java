package com.example.tumiweb.controller;

import com.example.tumiweb.dto.UserDTO;
import com.example.tumiweb.exception.DuplicateException;
import com.example.tumiweb.models.AuthenticationRequest;
import com.example.tumiweb.models.AuthenticationResponse;
import com.example.tumiweb.exception.LoginException;
import com.example.tumiweb.dao.Role;
import com.example.tumiweb.dao.User;
import com.example.tumiweb.services.IRoleService;
import com.example.tumiweb.services.IUserService;
import com.example.tumiweb.services.MyUserDetailsService;
import com.example.tumiweb.utils.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InvalidObjectException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IRoleService roleService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
               authenticationRequest.getUsername(), authenticationRequest.getPassword()
            ));
        }catch (BadCredentialsException e) {
            throw new LoginException("Incorrect username or password");
        }

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        User user = userService.getUserByUsername(authenticationRequest.getUsername());
        List<String> roles = new ArrayList<>();
        Set<Role> roleSet = user.getRoles();
        if(roleSet.size() > 0) {
            roleSet.forEach(item -> roles.add(item.getName()));
        }
        return ResponseEntity.ok(new AuthenticationResponse(jwt, user.getId(), user.getUsername(), roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO userDTO) throws InvalidObjectException {
        User oldUser = userService.getUserByUsername(userDTO.getUsername());
        if(oldUser != null) {
            throw new DuplicateException("Username has already exists");
        }
        User user = modelMapper.map(userDTO, User.class);
        if(user == null) {
            throw new InvalidObjectException("Invalid user");
        }
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        //gán role member cho user mới lập
        Role role = roleService.getRoleByName("MEMBER");
        user.setRoles(Set.of(role));
        User newUser = userService.save(user);

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(newUser.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt, newUser.getId(), newUser.getUsername(), List.of(role.getName())));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody AuthenticationResponse authenticationResponse) throws InvalidObjectException {
        try {
            String jwt = authenticationResponse.getJwt();
            String username = jwtUtil.extractUsername(jwt);
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            User user = userService.getUserByUsername(username);

            Set<Role> roles = user.getRoles();

            return ResponseEntity.ok(new AuthenticationResponse(
                    jwtUtil.generateToken(userDetails),
                    user.getId(),
                    user.getUsername(),
                    roles.stream().map(Role::getName).collect(Collectors.toList()))
            );
        } catch (Exception e) {
            throw new InvalidObjectException(e.getMessage());
        }
    }

}
