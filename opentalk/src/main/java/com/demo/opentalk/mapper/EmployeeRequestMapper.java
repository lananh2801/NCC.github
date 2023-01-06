package com.demo.opentalk.mapper;

import com.demo.opentalk.common.EntityMapper;
import com.demo.opentalk.dto.request.EmployeeRequestDTO;
import com.demo.opentalk.entity.Employee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeRequestMapper extends EntityMapper<EmployeeRequestDTO, Employee> {
}
