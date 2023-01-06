package com.demo.opentalk.service;

import com.demo.opentalk.dto.request.CompanyBranchRequestDTO;
import com.demo.opentalk.dto.response.CompanyBranchResponseDTO;

public interface CompanyBranchService {
    CompanyBranchResponseDTO addCompanyBranch(CompanyBranchRequestDTO companyBranchRequestDTO);
    CompanyBranchResponseDTO updateCompanyBranch(CompanyBranchRequestDTO companyBranchRequestDTO);
    String deleteCompanyBranch(int id);
}
