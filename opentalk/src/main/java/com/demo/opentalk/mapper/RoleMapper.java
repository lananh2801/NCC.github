package com.demo.opentalk.mapper;

import com.demo.opentalk.common.EntityMapper;
import com.demo.opentalk.dto.RoleDTO;
import com.demo.opentalk.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {
}
