package com.example.tumiweb.adapter.web.v1.controller;

import com.example.tumiweb.adapter.web.base.RestApiV1;
import com.example.tumiweb.adapter.web.base.VsResponseUtil;
import com.example.tumiweb.application.constants.UrlConstant;
import com.example.tumiweb.application.services.IRoleService;
import com.example.tumiweb.application.services.IUserService;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.entity.Role;
import com.example.tumiweb.domain.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Set;

@RestApiV1
public class UserRoleController {
  private final IUserService userService;
  private final IRoleService roleService;

  public UserRoleController(IUserService userService, IRoleService roleService) {
    this.roleService = roleService;
    this.userService = userService;
  }

  @GetMapping(UrlConstant.UserRole.DATA_USER_ROLE_UID)
  public ResponseEntity<?> getAllRoleByUserId(@PathVariable("userId") long id) {
    return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id).getRoles());
  }

  @PostMapping(UrlConstant.UserRole.DATA_USER_ROLE_ADD_ROLE)
//    @PreAuthorize("@appAuthorizer.authorize(authentication, 'ADMIN', this)")
  public ResponseEntity<?> addRoleToUser(@PathVariable("userId") Long userId, @PathVariable("roleId") Long roleId) {
    Role role = roleService.getRoleById(roleId);
    User user = userService.getUserById(userId);
    if (role == null || user == null) {
      throw new VsException("");
    }

    Set<Role> roles = user.getRoles();
    if (roles.contains(role)) {
      return VsResponseUtil.error(HttpStatus.BAD_REQUEST, "This user already has this role");
    }
    roles.add(role);
    user.setRoles(roles);
    user = userService.save(user);

    Set<User> users = role.getUsers();
    users.add(user);
    role.setUsers(users);
    roleService.save(role);

    return VsResponseUtil.ok("Added success");
  }

  @PostMapping(UrlConstant.UserRole.DATA_USER_ROLE_REMOVE_ROLE)
  @PreAuthorize("@appAuthorizer.authorize(authentication, 'ADMIN', this)")
  public ResponseEntity<?> removeRoleToUser(@PathVariable("userId") Long userId, @PathVariable("roleId") Long roleId) {
    Role role = roleService.getRoleById(roleId);
    User user = userService.getUserById(userId);
    if (role == null || user == null) {
      throw new VsException("");
    }

    Set<Role> roles = user.getRoles();
    if (!roles.contains(role)) {
      return VsResponseUtil.error(HttpStatus.BAD_REQUEST, "This user does not have this role");
    }
    roles.remove(role);
    user.setRoles(roles);
    user = userService.save(user);

    Set<User> users = role.getUsers();
    users.remove(user);
    role.setUsers(users);
    roleService.save(role);

    return VsResponseUtil.ok("Removed success");
  }

}
