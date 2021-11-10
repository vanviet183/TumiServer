package com.example.tumiweb.controller;

import com.example.tumiweb.base.BaseController;
import com.example.tumiweb.dao.Role;
import com.example.tumiweb.dto.RoleDTO;
import com.example.tumiweb.services.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController extends BaseController<Role> {

    @Autowired
    private IRoleService roleService;

    @GetMapping("")
    public ResponseEntity<?> getAllRole() {
        return this.resSetSuccess(roleService.getAllRole());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable("id") Long id) {
        return this.resSuccess(roleService.getRoleById(id));
    }

    @PostMapping("")
//    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> createRole(@RequestBody RoleDTO roleDTO) {
        return this.resSuccess(roleService.createRole(roleDTO));
    }


}
