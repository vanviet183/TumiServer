package com.example.tumiweb.services.imp;

import com.example.tumiweb.dto.RoleDTO;
import com.example.tumiweb.exception.DuplicateException;
import com.example.tumiweb.exception.NotFoundException;
import com.example.tumiweb.dao.Role;
import com.example.tumiweb.repository.RoleRepository;
import com.example.tumiweb.services.IRoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleServiceImp implements IRoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    private Role findRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if(role.isEmpty()) {
            return null;
        }
        return role.get();
    }

    @Override
    public Set<Role> getAllRole() {
        List<Role> roles = roleRepository.findAll();
        if(roles.isEmpty()) {
            throw new NotFoundException("Role list is empty");
        }
        return new HashSet<>(roles);
    }

    @Override
    public Role getRoleById(Long id) {
        Role role = findRoleById(id);
        if(role == null) {
            throw new NotFoundException("Can not find role by id: " + id);
        }
        return role;
    }

    @Override
    public Role getRoleByName(String name) {
        Role role = roleRepository.findByName(name);
        if(role == null) {
            throw new NotFoundException("Can not find role");
        }
        return role;
    }

    @Override
    public Role createRole(RoleDTO roleDTO) {
        if(roleRepository.findByName(roleDTO.getName()) != null) {
            throw new DuplicateException("This role is contain");
        }
        return roleRepository.save(modelMapper.map(roleDTO, Role.class));
    }

    @Override
    public Role editRoleById(Long id, RoleDTO roleDTO) {
        Role role = findRoleById(id);
        if(role == null) {
            throw new NotFoundException("Can not find role by id: " + id);
        }
        return roleRepository.save(modelMapper.map(roleDTO, Role.class));
    }

    @Override
    public Role deleteRoleById(Long id) {
        Role role = findRoleById(id);
        if(role == null) {
            throw new NotFoundException("Can not find role by id: " + id);
        }
        roleRepository.delete(role);
        return role;
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
