package com.example.tumiweb.application.services;

import com.example.tumiweb.adapter.web.v1.transfer.parameter.auth.AuthenticationRequest;
import com.example.tumiweb.adapter.web.v1.transfer.response.AuthenticationResponse;
import com.example.tumiweb.adapter.web.v1.transfer.response.TrueFalseResponse;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.UserDTO;

import java.io.InvalidObjectException;

public interface IAuthService {

  AuthenticationResponse login(AuthenticationRequest request) throws VsException;

  TrueFalseResponse validateToken(AuthenticationResponse authenticationResponse);

  TrueFalseResponse logoutHandler(Long id);

  AuthenticationResponse signup(UserDTO userDTO) throws InvalidObjectException;

}