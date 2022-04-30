package com.example.tumiweb.application.services;

import com.example.tumiweb.adapter.web.v1.transfer.parameter.AuthenticationRequest;
import com.example.tumiweb.adapter.web.v1.transfer.response.AuthenticationResponse;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.UserDTO;

import java.io.InvalidObjectException;

public interface IAuthService {

  AuthenticationResponse login(AuthenticationRequest request) throws VsException;

  Boolean validateToken(AuthenticationResponse authenticationResponse);

  Boolean logoutHandler(Long id);

  AuthenticationResponse signup(UserDTO userDTO) throws InvalidObjectException;

}