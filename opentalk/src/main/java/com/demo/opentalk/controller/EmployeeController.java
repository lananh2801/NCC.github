package com.demo.opentalk.controller;

import com.demo.opentalk.config.ERole;
import com.demo.opentalk.config.SingUpRequest;
import com.demo.opentalk.dto.request.EmployeeRequestDTO;
import com.demo.opentalk.dto.request.EmployeesRequestDTO;
import com.demo.opentalk.dto.response.EmployeeResponseDTO;
import com.demo.opentalk.entity.Employee;
import com.demo.opentalk.entity.EmployeeRole;
import com.demo.opentalk.entity.Role;
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
//@RequestMapping("employee/")
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
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmployeeRoleRepository employeeRoleRepository;
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SingUpRequest singUpRequest) {
        if (employeeRepository.existsEmployeeByUserName(singUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Username is already taken!");
        }

        if (employeeRepository.existsEmployeeByEmail(singUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Email is already in use!");
        }

        // Create new user's account
        Employee employee = new Employee(singUpRequest.getUsername(),
                singUpRequest.getEmail(),
                passwordEncoder.encode(singUpRequest.getPassword()));

        Set<String> strRoles = singUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
//                    case "mod":
//                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(modRole);
//
//                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        employee = employeeRepository.save(employee);
        List<EmployeeRole> employeeRoles = new ArrayList<>();
        for (Role role : roles) {
            EmployeeRole employeeRole = new EmployeeRole();
            employeeRole.setEmployee(employeeRepository.getById(employee.getEmployeeNo()));
            employeeRole.setRole(role);
            employeeRoles.add(employeeRole);
        }
        employee.setEmployeeRoles(employeeRoles);
        employeeRoles = employeeRoleRepository.saveAll(employeeRoles);

        return ResponseEntity.ok("User registered successfully!");
    }
}
