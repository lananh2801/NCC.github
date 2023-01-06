package com.demo.opentalk.service;

import com.demo.opentalk.dto.EmployeeDTO;
import com.demo.opentalk.dto.request.EmployeeRequestDTO;
import com.demo.opentalk.dto.request.EmployeesRequestDTO;
import com.demo.opentalk.dto.response.EmployeeResponseDTO;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.util.List;

public interface EmployeeService {
    List<EmployeeResponseDTO> syncData(EmployeesRequestDTO employeesRequestDTO);

    EmployeeResponseDTO updateEmployee(EmployeeRequestDTO employeeRequestDTO);

    String deleteEmployee(Integer id);

    EmployeeResponseDTO addEmployee(EmployeeRequestDTO employeeRequestDTO);

    List<EmployeeResponseDTO> getEmployeeByCriteria(Integer branchNo, String firstName, Boolean active);

    List<EmployeeResponseDTO> getEmployeeNoHostForOpenTalk(Pageable pageable,
                                                           Boolean active,
                                                           Integer branchNo,
                                                           String firstName,
                                                           Date startDate,
                                                           Date endDate);
}