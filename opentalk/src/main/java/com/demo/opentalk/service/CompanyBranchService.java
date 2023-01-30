package com.demo.opentalk.service;

import com.demo.opentalk.dto.request.CompanyBranchRequestDTO;
import com.demo.opentalk.dto.response.CompanyBranchResponseDTO;
import com.demo.opentalk.entity.CompanyBranch;

import java.util.List;

public interface CompanyBranchService {
    CompanyBranchResponseDTO addCompanyBranch(CompanyBranchRequestDTO companyBranchRequestDTO);
    CompanyBranchResponseDTO updateCompanyBranch(CompanyBranchRequestDTO companyBranchRequestDTO);
    String deleteCompanyBranch(int id);
    CompanyBranch findCompanyById(Integer id);
    CompanyBranchResponseDTO updateCompany(CompanyBranchRequestDTO companyBranchRequestDTO);
    String deleteCompanyById(Integer id);
    List<CompanyBranch> getAllCompanyBranch();
}
