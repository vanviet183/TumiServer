package com.example.tumiweb.adapter.web.v1.controller;

import com.example.tumiweb.adapter.web.base.RestApiV1;
import com.example.tumiweb.adapter.web.base.VsResponseUtil;
import com.example.tumiweb.application.constants.UrlConstant;
import com.example.tumiweb.application.services.IRoleService;
import com.example.tumiweb.domain.dto.RoleDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiV1
public class RoleController {
  private final IRoleService roleService;

  public RoleController(IRoleService roleService) {
    this.roleService = roleService;
  }

  @GetMapping(UrlConstant.Role.DATA_ROLE)
  public ResponseEntity<?> getAllRole() {
    return VsResponseUtil.ok(roleService.getAllRole());
  }

  @GetMapping(UrlConstant.Role.DATA_ROLE_ID)
  public ResponseEntity<?> getRoleById(@PathVariable("id") Long id) {
    return VsResponseUtil.ok(roleService.findRoleById(id));
  }

  @PostMapping(UrlConstant.Role.DATA_ROLE)
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<?> createRole(@RequestBody RoleDTO roleDTO) {
    return VsResponseUtil.ok(roleService.createRole(roleDTO));
  }

}
