package com.demo.opentalk.controller;

import com.demo.opentalk.config.ERole;
import com.demo.opentalk.config.SingUpRequest;
import com.demo.opentalk.dto.request.EmployeeRequestDTO;
import com.demo.opentalk.dto.request.EmployeesRequestDTO;
import com.demo.opentalk.dto.response.EmployeeResponseDTO;
import com.demo.opentalk.entity.Employee;
import com.demo.opentalk.entity.EmployeeRole;
import com.demo.opentalk.entity.Role;
import com.demo.opentalk.repository.CompanyBranchRepository;
import com.demo.opentalk.repository.EmployeeRepository;
import com.demo.opentalk.repository.EmployeeRoleRepository;
import com.demo.opentalk.repository.RoleRepository;
import com.demo.opentalk.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("employee/")
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping("add-employee-sync-data")
    public List<EmployeeResponseDTO> syncData(@RequestBody EmployeesRequestDTO employeesRequestDTO) {
        return employeeService.syncData(employeesRequestDTO);
    }

    @PutMapping("update-employee")
    public EmployeeResponseDTO updateEmployee(@RequestBody EmployeeRequestDTO employeeRequestDTO) {
        return employeeService.updateEmployee(employeeRequestDTO);
    }

    @DeleteMapping("delete-employee/{id}")
    public String deleteEmployee(@PathVariable int id) {
        return employeeService.deleteEmployee(id);
    }

    @PostMapping("add-employee")
    public EmployeeResponseDTO addEmployee(@RequestBody EmployeeRequestDTO employeeRequestDTO) {
        return employeeService.addEmployee(employeeRequestDTO);
    }

    @GetMapping("get-list-employee-by-criteria")
    public List<EmployeeResponseDTO> getListEmployeeByCriteria(
            @RequestParam(required = false) Integer branchNo,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) Boolean active) {
        return employeeService.getEmployeeByCriteria(branchNo, firstName, active);
    }

    @GetMapping("get-employee-no-host-for-open-talk")
    public List<EmployeeResponseDTO> getEmployeeNoHostForOpenTalk(
            Pageable pageable,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Integer branchNo,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate) {
        return employeeService.getEmployeeNoHostForOpenTalk(pageable, active, branchNo, firstName, startDate, endDate);
    }
}
