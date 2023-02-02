package com.demo.opentalk.service.Impl;

import com.demo.opentalk.config.ERole;
import com.demo.opentalk.config.JwtResponse;
import com.demo.opentalk.config.LoginRequest;
import com.demo.opentalk.config.SignUpRequest;
import com.demo.opentalk.entity.Employee;
import com.demo.opentalk.entity.EmployeeRole;
import com.demo.opentalk.entity.Role;
import com.demo.opentalk.repository.CompanyBranchRepository;
import com.demo.opentalk.repository.EmployeeRepository;
import com.demo.opentalk.repository.EmployeeRoleRepository;
import com.demo.opentalk.repository.RoleRepository;
import com.demo.opentalk.security.UserDetailsImpl;
import com.demo.opentalk.service.AuthService;
import com.demo.opentalk.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyBranchRepository companyBranchRepository;
    private final RoleRepository roleRepository;
    private final EmployeeRoleRepository employeeRoleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public ResponseEntity<?> registerUser(SignUpRequest signUpRequest) {
        if (employeeRepository.existsEmployeeByUserName(signUpRequest.getUserName())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Username is already taken!");
        }

        if (employeeRepository.existsEmployeeByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Email is already in use!");
        }

        Employee employee = new Employee();
        employee.setUserName(signUpRequest.getUserName());
        employee.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        employee.setEmail(signUpRequest.getEmail());
        employee.setCompanyBranch(companyBranchRepository.findCompanyBranchByBranchNo(signUpRequest.getBranchNo()));
        employee = employeeRepository.save(employee);

        Set<String> strRoles = signUpRequest.getRole();
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

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
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
