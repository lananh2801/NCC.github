package com.demo.opentalk.service.Impl;
import com.demo.opentalk.constants.MessageConstant;
import com.demo.opentalk.dto.DataSyncDTO;
import com.demo.opentalk.dto.EmployeeDTO;
import com.demo.opentalk.dto.ResultDTO;
import com.demo.opentalk.dto.request.EmployeeRequestDTO;
import com.demo.opentalk.dto.request.EmployeesRequestDTO;
import com.demo.opentalk.dto.response.EmployeeResponseDTO;
import com.demo.opentalk.entity.CompanyBranch;
import com.demo.opentalk.entity.Employee;
import com.demo.opentalk.entity.Role;
import com.demo.opentalk.exception.NotFoundException;
import com.demo.opentalk.mapper.EmployeeRequestMapper;
import com.demo.opentalk.mapper.EmployeeResponseMapper;
import com.demo.opentalk.repository.CompanyBranchRepository;
import com.demo.opentalk.repository.EmployeeRepository;
import com.demo.opentalk.repository.OpenTalkTopicRepository;
import com.demo.opentalk.repository.RoleRepository;
import com.demo.opentalk.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
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

    @Override
    public List<EmployeeResponseDTO> syncData(EmployeesRequestDTO employeesRequestDTO) {

//        RestTemplate restTemplate = new RestTemplate();
//        ResultDTO resultDTO = restTemplate.getForObject("http://hrm-api.nccsoft.vn/api/services/app/Checkin/GetUserForCheckin",
//                ResultDTO.class);
        ResultDTO resultDTO = restTemplate.getForObject("/", ResultDTO.class);

        List<EmployeeDTO> employeeDTOList = resultDTO.getResult();
        if (!companyBranchRepository.findById(employeesRequestDTO.getBranchNo()).isPresent() ||
                !roleRepository.findById(employeesRequestDTO.getRoleNo()).isPresent()) {
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
        for (EmployeeDTO employeeDTO : employeeDTOList) {
            if (employeeListGet.isEmpty() || !emailListGet.contains(employeeDTO.getEmail())) {
                Employee employee = mapper.map(employeeDTO, Employee.class);
                employee.setUserName(employeeDTO.getEmail().substring(0, employeeDTO.getEmail().indexOf("@")));
                employee.setActive(employeesRequestDTO.isActive());
                employee.setPassword(employeesRequestDTO.getPassword());
                employee.setCompanyBranch(companyBranchRepository.getById(employeesRequestDTO.getBranchNo()));
                employee.setRole(roleRepository.getById(employeesRequestDTO.getRoleNo()));
                employeeList.add(employee);
            }
        }
        employeeRepository.saveAll(employeeList);
        return getEmployeeResponseDTOS(employeeList);
    }

    private List<EmployeeResponseDTO> getEmployeeResponseDTOS(List<Employee> employeeList) {
        List<EmployeeResponseDTO> employeeResponseDTOList = new ArrayList<>();
        for (Employee employee: employeeList) {
            EmployeeResponseDTO employeeResponseDTO = mapper.map(employee, EmployeeResponseDTO.class);
            employeeResponseDTO.setFullName(employee.getLastName() + " " + employee.getFirstName());
            employeeResponseDTO.setBranchName(employee.getCompanyBranch().getBranchName());
            employeeResponseDTO.setRoleName(employee.getRole().getRoleName());
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
        employee.setRole(roleRepository.getById(employeeRequestDTO.getRoleNo()));
        return getEmployeeResponseDTO(employee);
    }

    private EmployeeResponseDTO getEmployeeResponseDTO(Employee employee) {
        employeeRepository.save(employee);

        EmployeeResponseDTO employeeResponseDTO = employeeResponseMapper.toDTO(employee);
        employeeResponseDTO.setFullName(employee.getLastName() + " " + employee.getFirstName());
        employeeResponseDTO.setBranchName(employee.getCompanyBranch().getBranchName());
        employeeResponseDTO.setRoleName(employee.getRole().getRoleName());
        return employeeResponseDTO;
    }
    @Transactional
    @Override
    public String deleteEmployee(Integer id) {
        if (!employeeRepository.findById(id).isPresent()) {
            throw new NotFoundException(MessageConstant.EMPLOYEE_IS_NULL);
        }
        openTalkTopicRepository.deleteOpenTalkTopicsByEmployeeNo(id);
        employeeRepository.deleteById(id);
        return MessageConstant.DELETE_DONE;
    }

    @Override
    public EmployeeResponseDTO addEmployee(EmployeeRequestDTO employeeRequestDTO) {
        Optional<CompanyBranch> companyBranchOptional = companyBranchRepository.findById(employeeRequestDTO.getBranchNo());
        Optional<Role> roleOptional = roleRepository.findById(employeeRequestDTO.getRoleNo());
        if (!companyBranchOptional.isPresent() || !roleOptional.isPresent()) {
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
            employee.setUserName(employeeRequestDTO.getEmail().substring(0, employeeRequestDTO.getEmail().indexOf("@")));
            employee.setRole(roleOptional.get());
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
            employeeResponseDTO.setRoleName(employee.getRole().getRoleName());
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
            employeeResponseDTO.setRoleName(employee.getRole().getRoleName());
            employeeResponseDTOList.add(employeeResponseDTO);
        }
        return employeeResponseDTOList;
    }
}
