package com.example.tumiweb.config.oauth2;


import com.example.tumiweb.adapter.web.base.VsResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Oauth2Controller {

  //  /oauth2/authorization/github

  @GetMapping("/oauth2/cur-user")
  public ResponseEntity<?> getUser(OAuth2AuthenticationToken token) {
    System.out.println(token.getPrincipal());
    return VsResponseUtil.ok(token.getPrincipal());
  }

}
