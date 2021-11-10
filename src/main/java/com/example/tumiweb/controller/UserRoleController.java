package com.example.tumiweb.controller;

import com.example.tumiweb.base.BaseController;
import com.example.tumiweb.dao.Role;
import com.example.tumiweb.dao.User;
import com.example.tumiweb.services.IRoleService;
import com.example.tumiweb.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/user-role")
public class UserRoleController extends BaseController<String> {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getAllRoleByUserId(@PathVariable("userId") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(id).getRoles());
    }

    @PostMapping("/{userId}/{roleId}/add")
//    @PreAuthorize("@appAuthorizer.authorize(authentication, 'ADMIN', this)")
    public ResponseEntity<?> addRoleToUser(
            @PathVariable("userId") Long userId,
            @PathVariable("roleId") Long roleId
    ) {
        Role role = roleService.getRoleById(roleId);
        User user = userService.getUserById(userId);
        if(role == null || user == null) {
            return this.resFailed();
        }

        Set<Role> roles = user.getRoles();
        if(roles.contains(role)) {
            return this.resSuccess("This user already has this role");
        }
        roles.add(role);
        user.setRoles(roles);
        user = userService.save(user);

        Set<User> users = role.getUsers();
        users.add(user);
        role.setUsers(users);
        roleService.save(role);

        return this.resSuccess("Added success");
    }

    @PostMapping("/{userId}/{roleId}/remove")
    @PreAuthorize("@appAuthorizer.authorize(authentication, 'ADMIN', this)")
    public ResponseEntity<?> removeRoleToUser(
            @PathVariable("userId") Long userId,
            @PathVariable("roleId") Long roleId
    ) {
        Role role = roleService.getRoleById(roleId);
        User user = userService.getUserById(userId);
        if(role == null || user == null) {
            return this.resFailed();
        }

        Set<Role> roles = user.getRoles();
        if(!roles.contains(role)) {
            return this.resSuccess("This user does not have this role");
        }
        roles.remove(role);
        user.setRoles(roles);
        user = userService.save(user);

        Set<User> users = role.getUsers();
        users.remove(user);
        role.setUsers(users);
        roleService.save(role);

        return this.resSuccess("Removed success");
    }

}
