package com.demo.opentalk.service.Impl;

import com.demo.opentalk.dto.RoleDTO;
import com.demo.opentalk.entity.Role;
import com.demo.opentalk.mapper.RoleMapper;
import com.demo.opentalk.repository.RoleRepository;
import com.demo.opentalk.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;

    @Override
    public RoleDTO addRole(RoleDTO roleDTO) {
        Role role = roleMapper.toEntity(roleDTO);
        roleRepository.save(role);

        RoleDTO roleResponseDTO = roleMapper.toDTO(role);
        return roleResponseDTO;
    }
}
