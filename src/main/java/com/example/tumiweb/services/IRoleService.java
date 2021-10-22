package com.example.tumiweb.services;

import com.example.tumiweb.dto.RoleDTO;
import com.example.tumiweb.dao.Role;

import java.util.Set;

public interface IRoleService {
    Set<Role> getAllRole();
    Role getRoleById(Long id);
    Role getRoleByName(String name);
    Role createRole(RoleDTO roleDTO);
    Role editRoleById(Long id, RoleDTO roleDTO);
    Role deleteRoleById(Long id);
    Role save(Role role);
}
