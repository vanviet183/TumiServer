package com.example.tumiweb.controller;

import com.example.tumiweb.base.BaseController;
import com.example.tumiweb.dto.UserDTO;
import com.example.tumiweb.dao.User;
import com.example.tumiweb.services.IUserService;
import com.example.tumiweb.constants.Constants;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/users")
@Api(value = "User APIs")
public class UserController extends BaseController<User> {
    @Autowired
    private IUserService userService;


    @ApiOperation(value = "Xem danh sách user", response = Set.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Thành công"),
            @ApiResponse(code = 401, message = "Chưa xác thực"),
            @ApiResponse(code = 403, message = "Truy cập bị cấm"),
            @ApiResponse(code = 404, message = "Không tìm thấy")
    })
    @GetMapping("")
//    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<?> getAllUser(
            @RequestParam(name = "page", required = false) Long page,
            @RequestParam(name = "status", required = false) boolean status,
            @RequestParam(name = "both", required = false) boolean both
            ){
        return this.resSetSuccess(userService.getAllUsers(page, Constants.SIZE_OFF_PAGE, status, both));
    }

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @ApiOperation(value = "Tìm một user với ID")
    public ResponseEntity<?> getUserById(
            @ApiParam(value = "Id của user cần tìm", required = true)
            @PathVariable("id") Long id
    ) {
        return this.resSuccess(userService.getUserById(id));
    }

//    @PostMapping("")
//    public ResponseEntity<?> createNewUser(@RequestBody UserDTO userDTO) {
//        return this.resSuccess(userService.createNewUser(userDTO));
//    }

    @PatchMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @ApiOperation(value = "Edit thông tin của một user")
    public ResponseEntity<?> editUserById(
            @ApiParam(value = "Id cuả user cần sửa thông tin", required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "Đối tượng DTO mang thông tin edit", required = true)
            @RequestBody UserDTO userDTO
        ) {
        return this.resSuccess(userService.editUserById(id, userDTO));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Delete user với id")
    public ResponseEntity<?> deleteUserById(
            @ApiParam(value = "Id của user cần xóa", required = true)
            @PathVariable("id") Long id
    ) {
        return this.resSuccess(userService.deleteUserById(id));
    }

    @PostMapping("/{id}/status")
//    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @ApiOperation(value = "Thay đổi trạng thái của user với id")
    public ResponseEntity<?> changeStatusById(
            @ApiParam(value = "Id của user cần thay đổi trạng thái", required = true)
            @PathVariable("id") Long id
    ) {
        return this.resSuccess(userService.changeStatusById(id));
    }

    @PostMapping("/{id}/avatar")
    @ApiOperation(value = "Thay đổi avatar của user")
    public String changeAvatarById(
            @ApiParam(value = "Id của user cần thay đổi avatar", required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "File ảnh truyền lên")
            @RequestParam(name = "avt", required = false) MultipartFile avt
        ) throws IOException {
        return userService.changeAvatarById(id, avt);
    }



}
