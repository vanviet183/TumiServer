package com.example.tumiweb.adapter.web.v1.controller;

import com.example.tumiweb.adapter.web.base.RestApiV1;
import com.example.tumiweb.adapter.web.base.VsResponseUtil;
import com.example.tumiweb.adapter.web.v1.transfer.parameter.auth.AuthenticationRequest;
import com.example.tumiweb.adapter.web.v1.transfer.response.AuthenticationResponse;
import com.example.tumiweb.application.constants.UrlConstant;
import com.example.tumiweb.application.services.IAuthService;
import com.example.tumiweb.domain.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.InvalidObjectException;

@RestApiV1
public class AuthController {
  private final IAuthService authService;

  public AuthController(IAuthService authService) {
    this.authService = authService;
  }

  @PostMapping(UrlConstant.Auth.LOGIN)
  public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
    return VsResponseUtil.ok(authService.login(authenticationRequest));
  }

  @PostMapping(UrlConstant.Auth.SIGNUP)
  public ResponseEntity<?> signup(@RequestBody UserDTO userDTO) throws InvalidObjectException {
    return VsResponseUtil.ok(authService.signup(userDTO));
  }

  @PostMapping(UrlConstant.Auth.VALIDATE)
  public ResponseEntity<?> validateToken(@RequestBody AuthenticationResponse authenticationResponse) {
    return VsResponseUtil.ok(authService.validateToken(authenticationResponse));
  }

  @PostMapping(UrlConstant.Auth.LOGOUT)
  public ResponseEntity<?> logoutHandler(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(authService.logoutHandler(id));
  }

}
