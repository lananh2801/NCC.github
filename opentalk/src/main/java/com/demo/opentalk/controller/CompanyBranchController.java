package com.demo.opentalk.controller;

import com.demo.opentalk.dto.request.CompanyBranchRequestDTO;
import com.demo.opentalk.dto.response.CompanyBranchResponseDTO;
import com.demo.opentalk.service.CompanyBranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CompanyBranchController {
    private final CompanyBranchService companyBranchService;

    @PostMapping("add-company-branch")
    public CompanyBranchResponseDTO addCompanyBranch(@RequestBody CompanyBranchRequestDTO companyBranchRequestDTO) {
        return companyBranchService.addCompanyBranch(companyBranchRequestDTO);
    }

    @PutMapping("update-company-branch")
    public CompanyBranchResponseDTO updateCompanyBranch(@RequestBody CompanyBranchRequestDTO companyBranchRequestDTO) {
        return companyBranchService.updateCompanyBranch(companyBranchRequestDTO);
    }

    @DeleteMapping("delete-company-branch")
    public String deleteCompanyBranch(@RequestParam int id) {
        return companyBranchService.deleteCompanyBranch(id);
    }
}
