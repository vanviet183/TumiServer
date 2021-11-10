package com.example.tumiweb.controller;

import com.example.tumiweb.constants.Constants;
import com.example.tumiweb.dao.User;
import com.example.tumiweb.exception.InvalidException;
import com.example.tumiweb.exception.NotFoundException;
import com.example.tumiweb.repository.UserRepository;
import com.example.tumiweb.services.ISendMailService;
import com.example.tumiweb.services.IUserService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Pattern;

@RestController
public class ForgotPasswordController {
    private final Bucket bucket;

    @Autowired
    private IUserService userService;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    private ISendMailService sendMailService;

    public ForgotPasswordController() {
        Bandwidth limit = Bandwidth.classic(2, Refill.greedy(2, Duration.ofMinutes(1)));
        this.bucket = Bucket4j.builder().addLimit(limit).build();
    }

    @PostMapping("/forgetPassword/{email}")
    public ResponseEntity<?> generate(@PathVariable String email) {
        final String regexEmail = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (bucket.tryConsume(1)) {
            if (Pattern.matches(regexEmail, email)) {
                User user = userService.getByEmail(email);

                if (user != null) {

                    String tokenResetPass = (String) (UUID.randomUUID().toString()).subSequence(0, 8);
                    user.setTokenResetPass(tokenResetPass);
                    userService.save(user);

                    String mess = "Quên mật khẩu Tumi\nĐây mã bí mật đổi mật khẩu của bạn: " + tokenResetPass;

                    sendMailService.sendMailWithText(Constants.SUBJECT_FORGOT_PASS, mess, user.getEmail());

                    return ResponseEntity.status(HttpStatus.OK)
                            .body("Đường link đổi mật khẩu đã được gửi vào email của bạn");
                } else {
                    throw new InvalidException("Email không tồn tại");
                }
            } else {
                throw new InvalidException("Không đúng định dạng email");
            }
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("TOO_MANY_REQUESTS");
    }

    @PostMapping("/check-token")
    public ResponseEntity<?> checkToken(@RequestParam(name = "token") String token){
        if (bucket.tryConsume(1)) {
            HashMap<String, Boolean> response = new HashMap<>();
            User user = userService.getUserByTokenResetPass(token);
            if (user != null) {
                response.put("exist", true);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else{
                response.put("exist", false);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("TOO_MANY_REQUESTS");
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> forgetPassword(@RequestParam String token, @RequestBody HashMap<String, String> password) {
        if (bucket.tryConsume(1)) {
            User user = userService.getUserByTokenResetPass(token);

            if (user != null) {
                user.setPassword(passwordEncoder.encode(password.get("password")));
                user.setTokenResetPass("");
                userService.save(user);
                return ResponseEntity.status(HttpStatus.OK).body("Đổi mật khẩu thành công");
            } else {
                throw new InvalidException("Token không hợp lệ");
            }
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("TOO_MANY_REQUESTS");

    }
}
