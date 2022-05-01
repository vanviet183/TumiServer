package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.dai.RoleRepository;
import com.example.tumiweb.application.mapper.RoleMapper;
import com.example.tumiweb.application.services.IRoleService;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.RoleDTO;
import com.example.tumiweb.domain.entity.Role;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceImp implements IRoleService {
  private final RoleMapper roleMapper = Mappers.getMapper(RoleMapper.class);
  @Autowired
  private RoleRepository roleRepository;

  @Override
  public Role findRoleById(Long id) {
    Optional<Role> role = roleRepository.findById(id);
    if (role.isEmpty()) {
      throw new VsException("Can not find role by id: " + id);
    }
    if (role.get().getDeleteFlag()) {
      throw new VsException("This role was delete");
    }
    if (!role.get().getActiveFlag()) {
      throw new VsException("This role was disable");
    }
    return role.get();
  }

  @Override
  public Set<Role> getAllRole() {
    List<Role> roles = roleRepository.findAll();
    if (roles.isEmpty()) {
      throw new VsException("Role list is empty");
    }
    return new HashSet<>(roles);
  }

  @Override
  public Role getRoleByName(String name) {
    Role role = roleRepository.findByName(name);
    if (role == null) {
      throw new VsException("Can not find role by name: " + name);
    }
    return role;
  }

  @Override
  public Role createRole(RoleDTO roleDTO) {
    Role role = roleRepository.findByName(roleDTO.getName());
    if (role != null) {
      throw new VsException("This role is contain");
    }
    return roleRepository.save(roleMapper.toRole(roleDTO));
  }

  @Override
  public Role editRoleById(Long id, RoleDTO roleDTO) {
    Role role = findRoleById(id);

    role.setName(roleDTO.getName());
    role.setDescription(roleDTO.getDescription());

    return roleRepository.save(role);
  }

  @Override
  public Role deleteRoleById(Long id) {
    Role role = findRoleById(id);

    role.setDeleteFlag(true);

    return roleRepository.save(role);
  }

  @Override
  public Role save(Role role) {
    return roleRepository.save(role);
  }

}
