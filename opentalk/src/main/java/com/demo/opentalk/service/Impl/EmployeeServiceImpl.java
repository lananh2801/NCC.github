package com.demo.opentalk.service.Impl;
import com.demo.opentalk.config.ERole;
import com.demo.opentalk.constants.MessageConstant;
import com.demo.opentalk.dto.EmployeeDTO;
import com.demo.opentalk.dto.ResultDTO;
import com.demo.opentalk.dto.request.EmployeeRequestDTO;
import com.demo.opentalk.dto.request.EmployeesRequestDTO;
import com.demo.opentalk.dto.response.EmployeeResponseDTO;
import com.demo.opentalk.entity.CompanyBranch;
import com.demo.opentalk.entity.Employee;
import com.demo.opentalk.entity.EmployeeRole;
import com.demo.opentalk.entity.Role;
import com.demo.opentalk.exception.NotFoundException;
import com.demo.opentalk.mapper.EmployeeRequestMapper;
import com.demo.opentalk.mapper.EmployeeResponseMapper;
import com.demo.opentalk.repository.*;
import com.demo.opentalk.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper mapper;
    private final CompanyBranchRepository companyBranchRepository;
    private final EmployeeResponseMapper employeeResponseMapper;
    private final EmployeeRequestMapper employeeRequestMapper;
    private final RoleRepository roleRepository;
    private final OpenTalkTopicRepository openTalkTopicRepository;
    private final RestTemplate restTemplate;
    private final EmployeeRoleRepository employeeRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<EmployeeResponseDTO> syncData(EmployeesRequestDTO employeesRequestDTO) {

//        RestTemplate restTemplate = new RestTemplate();
//        ResultDTO resultDTO = restTemplate.getForObject("http://hrm-api.nccsoft.vn/api/services/app/Checkin/GetUserForCheckin",
//                ResultDTO.class);
        ResultDTO resultDTO = restTemplate.getForObject("/", ResultDTO.class);

        List<EmployeeDTO> employeeDTOList = resultDTO.getResult();
        if (!companyBranchRepository.findById(employeesRequestDTO.getBranchNo()).isPresent() ||
            roleRepository.getSetRoleByRoleNo(employeesRequestDTO.getRoleNos()).isEmpty()) {
            throw new NotFoundException(MessageConstant.COMPANY_BRANCH_OR_ROLE_IS_NULL);
        }
        List<Employee> employeeListGet = employeeRepository.findAll();
        List<String> emailListGet = new ArrayList<>();
        List<Employee> employeeList = new ArrayList<>();
        if (!employeeListGet.isEmpty()) {
            emailListGet = employeeListGet.stream()
                    .map(Employee::getEmail)
                    .collect(Collectors.toList());
        }
        List<String> emailDTOList = employeeDTOList.stream()
                .map(EmployeeDTO::getEmail)
                .collect(Collectors.toList());
        for (EmployeeDTO employeeDTO : employeeDTOList) {
            if (employeeListGet.isEmpty() || !emailListGet.contains(employeeDTO.getEmail())) {
                Employee employee = mapper.map(employeeDTO, Employee.class);
                employee.setUserName(employeeDTO.getEmail().substring(0, employeeDTO.getEmail().indexOf("@")));
                employee.setActive(employeesRequestDTO.isActive());
                employee.setPassword(passwordEncoder.encode(employeesRequestDTO.getPassword()));
                employee.setCompanyBranch(companyBranchRepository.getById(employeesRequestDTO.getBranchNo()));
                employee = employeeRepository.save(employee);
                List<EmployeeRole> employeeRoles = new ArrayList<>();
                for (int roleNo : employeesRequestDTO.getRoleNos()) {
                    EmployeeRole employeeRole = new EmployeeRole();
                    employeeRole.setEmployee(employeeRepository.getById(employee.getEmployeeNo()));
                    employeeRole.setRole(roleRepository.getById(roleNo));
                    employeeRoles.add(employeeRole);
                }
                employee.setEmployeeRoles(employeeRoles);
                employeeList.add(employee);
                employeeRoles = employeeRoleRepository.saveAll(employeeRoles);
            }
        }
        employeeRepository.saveAll(employeeList);
        for (Employee employee: employeeListGet) {
            if (!emailDTOList.contains(employee.getEmail())) {
                employee.setActive(false);
                employeeRepository.save(employee);
            }
        }
        return getEmployeeResponseDTOS(employeeList);
    }

    private List<EmployeeResponseDTO> getEmployeeResponseDTOS(List<Employee> employeeList) {
        List<EmployeeResponseDTO> employeeResponseDTOList = new ArrayList<>();
        for (Employee employee: employeeList) {
            EmployeeResponseDTO employeeResponseDTO = mapper.map(employee, EmployeeResponseDTO.class);
            employeeResponseDTO.setFullName(employee.getLastName() + " " + employee.getFirstName());
            employeeResponseDTO.setBranchName(employee.getCompanyBranch().getBranchName());
            List<ERole> roleNames = new ArrayList<>();
            for (EmployeeRole employeeRole: employee.getEmployeeRoles()) {
                ERole roleName = employeeRole.getRole().getRoleName();
                roleNames.add(roleName);
            }
            employeeResponseDTO.setRoleNames(roleNames);
            employeeResponseDTOList.add(employeeResponseDTO);
        }
        return employeeResponseDTOList;
    }


    @Override
    public EmployeeResponseDTO updateEmployee(EmployeeRequestDTO employeeRequestDTO) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeRequestDTO.getEmployeeNo());
        if (!employeeOptional.isPresent()) {
            throw new NotFoundException(MessageConstant.EMPLOYEE_IS_NULL);
        }
        Employee employee = employeeRequestMapper.toEntity(employeeRequestDTO);
        employee.setCompanyBranch(companyBranchRepository.getById(employeeRequestDTO.getBranchNo()));
        employee.setUserName(employeeRequestDTO.getEmail().substring(0, employeeRequestDTO.getEmail().indexOf("@")));
        employee = employeeRepository.save(employee);
        List<EmployeeRole> employeeRoles = new ArrayList<>();
        for (int roleNo : employeeRequestDTO.getRoleNos()) {
            EmployeeRole employeeRole = new EmployeeRole();
            employeeRole.setEmployee(employeeRepository.getById(employee.getEmployeeNo()));
            employeeRole.setRole(roleRepository.getById(roleNo));
            employeeRoles.add(employeeRole);
        }
        employee.setEmployeeRoles(employeeRoles);
        employeeRoles = employeeRoleRepository.saveAll(employeeRoles);
        return getEmployeeResponseDTO(employee);
    }

    private EmployeeResponseDTO getEmployeeResponseDTO(Employee employee) {
        employeeRepository.save(employee);
        EmployeeResponseDTO employeeResponseDTO = employeeResponseMapper.toDTO(employee);
        employeeResponseDTO.setFullName(employee.getLastName() + " " + employee.getFirstName());
        employeeResponseDTO.setBranchName(employee.getCompanyBranch().getBranchName());
        List<ERole> roleNames = new ArrayList<>();
        for (EmployeeRole employeeRole: employee.getEmployeeRoles()) {
            ERole roleName = employeeRole.getRole().getRoleName();
            roleNames.add(roleName);
        }
        employeeResponseDTO.setRoleNames(roleNames);
        return employeeResponseDTO;
    }
    @Transactional
    @Override
    public String deleteEmployee(Integer id) {
        if (!employeeRepository.findById(id).isPresent()) {
            throw new NotFoundException(MessageConstant.EMPLOYEE_IS_NULL);
        }
        employeeRoleRepository.findEmployeeRolesByEmployeeNo(id);
        openTalkTopicRepository.deleteOpenTalkTopicsByEmployeeNo(id);
        employeeRepository.deleteById(id);
        return MessageConstant.DELETE_DONE;
    }

    @Override
    public EmployeeResponseDTO addEmployee(EmployeeRequestDTO employeeRequestDTO) {
        Optional<CompanyBranch> companyBranchOptional = companyBranchRepository.findById(employeeRequestDTO.getBranchNo());
        List<Role> roles = roleRepository.getSetRoleByRoleNo(employeeRequestDTO.getRoleNos());
        if (!companyBranchOptional.isPresent() || roles.isEmpty()) {
            throw new NotFoundException(MessageConstant.COMPANY_BRANCH_OR_ROLE_IS_NULL);
        }
        List<Employee> employeeListGet = employeeRepository.findAll();
        List<String> emailListGet = new ArrayList<>();
        if (!employeeListGet.isEmpty()) {
           emailListGet = employeeListGet.stream()
                    .map(Employee::getEmail)
                    .collect(Collectors.toList());
        }
        if (employeeListGet.isEmpty() || !emailListGet.contains(employeeRequestDTO.getEmail())) {
            Employee employee = employeeRequestMapper.toEntity(employeeRequestDTO);
            employee.setCompanyBranch(companyBranchOptional.get());
            employee.setPassword(passwordEncoder.encode(employeeRequestDTO.getPassword()));
            employee.setUserName(employeeRequestDTO.getEmail().substring(0, employeeRequestDTO.getEmail().indexOf("@")));
            employee = employeeRepository.save(employee);
            List<EmployeeRole> employeeRoles = new ArrayList<>();
            for (int roleNo : employeeRequestDTO.getRoleNos()) {
                EmployeeRole employeeRole = new EmployeeRole();
                employeeRole.setEmployee(employeeRepository.getById(employee.getEmployeeNo()));
                employeeRole.setRole(roleRepository.getById(roleNo));
                employeeRoles.add(employeeRole);
            }
            employee.setEmployeeRoles(employeeRoles);
            employeeRoles = employeeRoleRepository.saveAll(employeeRoles);
            return getEmployeeResponseDTO(employee);
        }
        return null;
    }

    @Override
    public List<EmployeeResponseDTO> getEmployeeByCriteria(Integer branchNo, String firstName, Boolean active) {
        List<Employee> employeeList = employeeRepository.getEmployeeByCriteria(branchNo, firstName, active);
        if (employeeList.isEmpty()) {
            throw new NotFoundException(MessageConstant.LIST_EMPLOYEE_IS_EMPTY);
        }
        List<EmployeeResponseDTO> employeeResponseDTOList = new ArrayList<>();
        for (Employee employee: employeeList) {
            EmployeeResponseDTO employeeResponseDTO = employeeResponseMapper.toDTO(employee);
            employeeResponseDTO.setFullName(employee.getLastName() + " " + employee.getFirstName());
            employeeResponseDTO.setBranchName(employee.getCompanyBranch().getBranchName());
            List<ERole> roleNames = new ArrayList<>();
            for (EmployeeRole employeeRole: employee.getEmployeeRoles()) {
                ERole roleName = employeeRole.getRole().getRoleName();
                roleNames.add(roleName);
            }
            employeeResponseDTO.setRoleNames(roleNames);
            employeeResponseDTOList.add(employeeResponseDTO);
        }
        return employeeResponseDTOList;
    }

    @Override
    public List<EmployeeResponseDTO> getEmployeeNoHostForOpenTalk(Pageable pageable, Boolean active, Integer branchNo, String firstName, Date startDate, Date endDate) {
        Page<Employee> employeePage = employeeRepository.getEmployeeNoHostForOpenTalk(pageable, active, branchNo, firstName, startDate, endDate);
        List<EmployeeResponseDTO> employeeResponseDTOList = new ArrayList<>();
        for (Employee employee: employeePage.getContent()) {
            EmployeeResponseDTO employeeResponseDTO = employeeResponseMapper.toDTO(employee);
            employeeResponseDTO.setFullName(employee.getLastName() + " " + employee.getFirstName());
            employeeResponseDTO.setBranchName(employee.getCompanyBranch().getBranchName());
            List<ERole> roleNames = new ArrayList<>();
            for (EmployeeRole employeeRole: employee.getEmployeeRoles()) {
                ERole roleName = employeeRole.getRole().getRoleName();
                roleNames.add(roleName);
            }
            employeeResponseDTO.setRoleNames(roleNames);
            employeeResponseDTOList.add(employeeResponseDTO);
        }
        return employeeResponseDTOList;
    }
}
