package com.example.tumiweb.config.oauth2;

import com.example.tumiweb.adapter.web.base.VsResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/github")
public class AuthGithubController {

  ///oauth2/authorization/github

  @Autowired
  private GithubService githubService;

  @GetMapping("/save")
  public ResponseEntity<?> save(OAuth2AuthenticationToken token) {
    return VsResponseUtil.ok(githubService.saveUserWithFirstLogin(token));
  }

}
