package com.example.tumiweb.oauth2;

import com.example.tumiweb.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/google")
public class AuthGoogleController extends BaseController<String> {

    ///oauth2/authorization/google

    @Autowired
    private GoogleService googleService;

    @GetMapping("/save")
    public ResponseEntity<?> save(OAuth2AuthenticationToken token) {
        return this.resSuccess(googleService.saveUserWithFirstLogin(token));
    }

}
