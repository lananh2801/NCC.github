package com.demo.opentalk.controller;

import com.demo.opentalk.config.ERole;
import com.demo.opentalk.config.JwtResponse;
import com.demo.opentalk.config.LoginRequest;
import com.demo.opentalk.config.SingUpRequest;
import com.demo.opentalk.entity.Employee;
import com.demo.opentalk.entity.EmployeeRole;
import com.demo.opentalk.entity.Role;
import com.demo.opentalk.repository.CompanyBranchRepository;
import com.demo.opentalk.repository.EmployeeRepository;
import com.demo.opentalk.repository.EmployeeRoleRepository;
import com.demo.opentalk.repository.RoleRepository;
import com.demo.opentalk.security.UserDetailsImpl;
import com.demo.opentalk.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmployeeRoleRepository employeeRoleRepository;
    private final CompanyBranchRepository companyBranchRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SingUpRequest singUpRequest) {
        if (employeeRepository.existsEmployeeByUserName(singUpRequest.getUserName())) {
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
//        Employee employee = new Employee(singUpRequest.getUserName(),
//                singUpRequest.getEmail(),
//                passwordEncoder.encode(singUpRequest.getPassword()));
        Employee employee =new Employee();
        employee.setUserName(singUpRequest.getUserName());
        employee.setPassword(passwordEncoder.encode(singUpRequest.getPassword()));
        employee.setEmail(singUpRequest.getEmail());
        employee.setCompanyBranch(companyBranchRepository.findCompanyBranchByBranchNo(singUpRequest.getBranchNo()));
        employee = employeeRepository.save(employee);

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

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }
}
