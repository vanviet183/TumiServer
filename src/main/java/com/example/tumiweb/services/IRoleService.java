package com.example.tumiweb.services;

import com.example.tumiweb.dto.RoleDTO;
import com.example.tumiweb.model.Role;

import java.util.Set;

public interface IRoleService {
    Set<Role> getAllRole();
    Role getRoleById(Long id);
    Role createRole(RoleDTO roleDTO);
    Role editRoleById(Long id, RoleDTO roleDTO);
    Role deleteRoleById(Long id);
}
