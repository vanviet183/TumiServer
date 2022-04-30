package com.example.tumiweb.adapter.web.v1.transfer.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthenticationResponse {
  private String jwt;
  private Long userId;
  private String username;
  private List<String> roles;
}
