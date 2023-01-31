package com.demo.opentalk.security;

import com.demo.opentalk.constants.MessageConstant;
import com.demo.opentalk.entity.Employee;
import com.demo.opentalk.exception.NotFoundException;
import com.demo.opentalk.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findEmployeeByUserName(username);
        if (employee == null) {
            throw new NotFoundException(MessageConstant.EMPLOYEE_IS_NULL);
        }
        return UserDetailsImpl.build(employee);
    }
}
