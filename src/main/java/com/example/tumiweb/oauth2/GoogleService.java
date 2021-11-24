package com.example.tumiweb.oauth2;

import com.example.tumiweb.constants.AuthenticationProvider;
import com.example.tumiweb.dao.Role;
import com.example.tumiweb.dao.User;
import com.example.tumiweb.services.IRoleService;
import com.example.tumiweb.services.IUserService;
import com.example.tumiweb.services.MyUserDetailsService;
import com.example.tumiweb.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GoogleService {
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserService userService;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    String saveUserWithFirstLogin(OAuth2AuthenticationToken token) {
        String email = token.getPrincipal().getAttributes().get("email").toString();
        String username = email.substring(0, email.indexOf('@'));

        User user = userService.getUserByUsername(username);

        if(user != null) {
            return jwtUtil.generateToken(myUserDetailsService.loadUserByUsername(username));
        }

        user = new User();
        user.setAuthProvider(AuthenticationProvider.GOOGLE);
        user.setPhone(token.getPrincipal().getAttributes().get("sub").toString().substring(0, 10));
        user.setFullName(token.getPrincipal().getAttributes().get("name").toString());

        user.setUsername(username);
        user.setPassword(passwordEncoder().encode(username));
        user.setAvatar((String) token.getPrincipal().getAttributes().get("picture"));
        user.setEmail(email);
        user.setMark(0L);

        Role role = roleService.getRoleByName("ROLE_MEMBER");
        user.setRoles(Set.of(role));

        User newUser = userService.save(user);

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(newUser.getUsername());

        return jwtUtil.generateToken(userDetails);
    }

}
