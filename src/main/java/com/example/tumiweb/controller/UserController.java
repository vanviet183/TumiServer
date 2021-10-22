package com.example.tumiweb.controller;

import com.example.tumiweb.base.BaseController;
import com.example.tumiweb.dto.UserDTO;
import com.example.tumiweb.dao.User;
import com.example.tumiweb.services.IUserService;
import com.example.tumiweb.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users")
public class UserController extends BaseController<User> {
    @Autowired
    private IUserService userService;

    @GetMapping("")
    public ResponseEntity<?> getAllUser(
            @RequestParam(name = "page", required = false) Long page,
            @RequestParam(name = "status", required = false) boolean status,
            @RequestParam(name = "both", required = false) boolean both
            ){
        return this.resSetSuccess(userService.getAllUsers(page, Constants.SIZE_OFF_PAGE, status, both));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        return this.resSuccess(userService.getUserById(id));
    }

//    @PostMapping("")
//    public ResponseEntity<?> createNewUser(@RequestBody UserDTO userDTO) {
//        return this.resSuccess(userService.createNewUser(userDTO));
//    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> editUserById(
            @PathVariable("id") Long id,
            @RequestBody UserDTO userDTO
        ) {
        return this.resSuccess(userService.editUserById(id, userDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id) {
        return this.resSuccess(userService.deleteUserById(id));
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<?> changeStatusById(@PathVariable("id") Long id) {
        return this.resSuccess(userService.changeStatusById(id));
    }

    @PostMapping("/{id}/avatar")
    public String changeAvatarById(
            @PathVariable("id") Long id,
            @RequestParam(name = "avt", required = false) MultipartFile avt
        ) throws IOException {
        return userService.changeAvatarById(id, avt);
    }



}
