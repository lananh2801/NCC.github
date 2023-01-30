package com.demo.opentalk.service.Impl;

import com.demo.opentalk.constants.MessageConstant;
import com.demo.opentalk.dto.request.CompanyBranchRequestDTO;
import com.demo.opentalk.dto.response.CompanyBranchResponseDTO;
import com.demo.opentalk.entity.CompanyBranch;
import com.demo.opentalk.exception.NotFoundException;
import com.demo.opentalk.repository.CompanyBranchRepository;
import com.demo.opentalk.repository.EmployeeRepository;
import com.demo.opentalk.service.CompanyBranchService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyBranchServiceImpl implements CompanyBranchService {
    private final ModelMapper mapper;
    private final CompanyBranchRepository companyBranchRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public CompanyBranchResponseDTO addCompanyBranch(CompanyBranchRequestDTO companyBranchRequestDTO) {
        CompanyBranch companyBranch = mapper.map(companyBranchRequestDTO, CompanyBranch.class);
        companyBranchRepository.save(companyBranch);

        CompanyBranchResponseDTO companyBranchResponseDTO = mapper.map(companyBranch, CompanyBranchResponseDTO.class);
        return companyBranchResponseDTO;
    }

    @Override
    public CompanyBranchResponseDTO updateCompanyBranch(CompanyBranchRequestDTO companyBranchRequestDTO) {
        if (!companyBranchRepository.findById(companyBranchRequestDTO.getBranchNo()).isPresent()) {
            throw new NotFoundException(MessageConstant.COMPANY_BRANCH_IS_NULL);
        }
        CompanyBranch companyBranch = mapper.map(companyBranchRequestDTO, CompanyBranch.class);
        companyBranchRepository.save(companyBranch);

        CompanyBranchResponseDTO companyBranchResponseDTO = mapper.map(companyBranch, CompanyBranchResponseDTO.class);
        return companyBranchResponseDTO;
    }
    @Transactional
    @Override
    public String deleteCompanyBranch(int id) {
        if (!companyBranchRepository.findById(id).isPresent()) {
            throw new NotFoundException(MessageConstant.COMPANY_BRANCH_IS_NULL);
        }
        employeeRepository.deleteEmployeesByBranchNo(id);
        companyBranchRepository.deleteById(id);
        return MessageConstant.DELETE_DONE;
    }

    @Override
    public CompanyBranch findCompanyById(Integer id) {
        CompanyBranch companyBranch = companyBranchRepository.findCompanyBranchByBranchNo(id);
//        CompanyBranchResponseDTO companyBranchResponseDTO = mapper.map(companyBranch, CompanyBranchResponseDTO.class);

        return companyBranch;
    }

    @Transactional
    @Override
    public CompanyBranchResponseDTO updateCompany(CompanyBranchRequestDTO companyBranchRequestDTO) {
        companyBranchRepository.updateCompany(companyBranchRequestDTO.getBranchNo(), companyBranchRequestDTO.getBranchName());
        CompanyBranchResponseDTO companyBranchResponseDTO = mapper.map(companyBranchRepository.getById(companyBranchRequestDTO.getBranchNo()),
                                                            CompanyBranchResponseDTO.class);
        return companyBranchResponseDTO;
    }

    @Transactional
    @Override
    public String deleteCompanyById(Integer id) {
        companyBranchRepository.removeCompanyBranchByBranchNo(id);
        return MessageConstant.DELETE_DONE;
    }

    @Override
    public List<CompanyBranch> getAllCompanyBranch() {
        List<CompanyBranch> companyBranchList = companyBranchRepository.findAll();
        return companyBranchList;
    }
}
