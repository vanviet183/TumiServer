package com.example.tumiweb.adapter.web.v1.controller;

import com.example.tumiweb.adapter.web.base.RestApiV1;
import com.example.tumiweb.adapter.web.base.VsResponseUtil;
import com.example.tumiweb.adapter.web.v1.transfer.parameter.common.GetAllDataParameter;
import com.example.tumiweb.application.constants.CommonConstant;
import com.example.tumiweb.application.constants.UrlConstant;
import com.example.tumiweb.application.excel.ReadExcelFile;
import com.example.tumiweb.application.input.common.GetAllDataInput;
import com.example.tumiweb.application.output.common.GetAllDataOutput;
import com.example.tumiweb.application.services.IUserService;
import com.example.tumiweb.application.services.imp.BackupServiceImp;
import com.example.tumiweb.application.utils.UploadFile;
import com.example.tumiweb.domain.dto.UserDTO;
import com.example.tumiweb.domain.entity.User;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestApiV1
@Api(value = "User APIs")
public class UserController {
  private final IUserService userService;
  private final BackupServiceImp backupService;

  public UserController(IUserService userService, BackupServiceImp backupService) {
    this.userService = userService;
    this.backupService = backupService;
  }

  @ApiOperation(value = "Xem danh sách user", response = ResponseEntity.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Thành công"),
      @ApiResponse(code = 401, message = "Chưa xác thực"),
      @ApiResponse(code = 403, message = "Truy cập bị cấm"),
      @ApiResponse(code = 404, message = "Không tìm thấy")
  })
  @GetMapping(UrlConstant.User.DATA_USER)
//    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> getAllUser(@Valid GetAllDataParameter parameter) {
    GetAllDataInput input = new GetAllDataInput(parameter.getPage(), parameter.getActiveFlag(), parameter.getBoth(),
        CommonConstant.SIZE_OFF_PAGE);

    GetAllDataOutput<?> output = userService.getAllUsers(input);

    return VsResponseUtil.ok(output);
  }

  @GetMapping(UrlConstant.User.DATA_USER_ID)
//    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  @ApiOperation(value = "Tìm một user với ID", response = ResponseEntity.class)
  public ResponseEntity<?> getUserById(
      @ApiParam(value = "Id của user cần tìm", required = true)
      @PathVariable("id") Long id
  ) {
    return VsResponseUtil.ok(userService.getUserById(id));
  }

  @PostMapping(UrlConstant.User.DATA_USER)
  @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  public ResponseEntity<?> createNewUser(@RequestBody UserDTO userDTO) {
    return VsResponseUtil.ok(userService.createNewUser(userDTO));
  }

  @PatchMapping(UrlConstant.User.DATA_USER_ID)
//    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  @ApiOperation(value = "Edit thông tin của một user", response = ResponseEntity.class)
  public ResponseEntity<?> editUserById(
      @ApiParam(value = "Id cuả user cần sửa thông tin", required = true)
      @PathVariable("id") Long id,
      @ApiParam(value = "Đối tượng DTO mang thông tin edit", required = true)
      @RequestBody UserDTO userDTO
  ) {
    return VsResponseUtil.ok(userService.editUserById(id, userDTO));
  }

  @DeleteMapping(UrlConstant.User.DATA_USER_ID)
//    @PreAuthorize("hasRole('ADMIN')")
  @ApiOperation(value = "Delete user với id", response = ResponseEntity.class)
  public ResponseEntity<?> deleteUserById(
      @ApiParam(value = "Id của user cần xóa", required = true)
      @PathVariable("id") Long id
  ) {
    return VsResponseUtil.ok(userService.deleteUserById(id));
  }

  @PostMapping(UrlConstant.User.DATA_USER_STATUS)
//    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
  @ApiOperation(value = "Thay đổi trạng thái của user với id", response = ResponseEntity.class)
  public ResponseEntity<?> changeStatusById(
      @ApiParam(value = "Id của user cần thay đổi trạng thái", required = true)
      @PathVariable("id") Long id
  ) {
    return VsResponseUtil.ok(userService.changeDeleteFlagById(id));
  }

  @PostMapping(UrlConstant.User.DATA_USER_AVATAR)
  @ApiOperation(value = "Thay đổi avatar của user", response = ResponseEntity.class)
  public ResponseEntity<?> changeAvatarById(@ApiParam(value = "Id của user cần thay đổi avatar", required = true)
                                            @PathVariable("id") Long id,
                                            @ApiParam(value = "File ảnh truyền lên")
                                            @RequestParam(name = "avt", required = false) MultipartFile avt) throws IOException {
    return VsResponseUtil.ok(userService.changeAvatarById(id, avt));
  }

  @GetMapping(UrlConstant.User.DATA_USER_SEARCH)
  public ResponseEntity<?> searchUsersByKey(@RequestParam("q") String key) {
    return VsResponseUtil.ok(userService.searchUsersByKey(key));
  }

  // Excel file
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping(UrlConstant.User.DATA_USER_EXPORT)
  public ResponseEntity<?> exportToExcel(HttpServletResponse res) {
    if (!backupService.backupUser(res)) {
      //send notification to admin

      return ResponseEntity.ok("Export failed");
    }
    return ResponseEntity.ok("Export success");
  }

  @PostMapping(UrlConstant.User.DATA_USER_IMPORT)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> importExcelFile(@RequestParam(name = "file") MultipartFile file) throws IOException {
    List<User> users = ReadExcelFile.readFileUser(UploadFile.convertMultipartToFile(file));

    //Do something with users

    return ResponseEntity.status(200).body(users);
  }

}
