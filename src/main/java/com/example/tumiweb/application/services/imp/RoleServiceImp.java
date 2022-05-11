package com.example.tumiweb.application.services.imp;

import com.example.tumiweb.application.constants.DevMessageConstant;
import com.example.tumiweb.application.constants.UserMessageConstant;
import com.example.tumiweb.application.dai.RoleRepository;
import com.example.tumiweb.application.mapper.RoleMapper;
import com.example.tumiweb.application.services.IRoleService;
import com.example.tumiweb.config.exception.VsException;
import com.example.tumiweb.domain.dto.RoleDTO;
import com.example.tumiweb.domain.entity.Role;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceImp implements IRoleService {
  private final RoleMapper roleMapper = Mappers.getMapper(RoleMapper.class);
  private final RoleRepository roleRepository;

  public RoleServiceImp(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @Override
  public Role findRoleById(Long id) {
    Optional<Role> role = roleRepository.findById(id);
    if (role.isEmpty()) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.NOT_FOUND_OBJECT_BY_ID, "role", id));
    }
    if (role.get().getDeleteFlag()) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.DATA_WAS_DELETE, id));
    }
    if (!role.get().getActiveFlag()) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.DATA_WAS_DISABLE, id));
    }
    return role.get();
  }

  @Override
  public Set<Role> getAllRole() {
    List<Role> roles = roleRepository.findAll();
    if (roles.isEmpty()) {
      throw new VsException(UserMessageConstant.ERR_NO_DATA_SELECT_RESULT, DevMessageConstant.Common.NO_DATA_SELECTED);
    }
    return new HashSet<>(roles);
  }

  @Override
  public Role getRoleByName(String name) {
    Role role = roleRepository.findByName(name);
    if (role == null) {
      throw new VsException(UserMessageConstant.ERR_NO_DATA_SELECT_RESULT,
          String.format(DevMessageConstant.Role.NOT_FOUND_ROLE_BY_NAME, name));
    }
    return role;
  }

  @Override
  public Role createRole(RoleDTO roleDTO) {
    Role role = roleRepository.findByName(roleDTO.getName());
    if (role != null) {
      throw new VsException(UserMessageConstant.ERR_EXCEPTION_GENERAL,
          String.format(DevMessageConstant.Common.DUPLICATE_NAME, role.getName()));
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
