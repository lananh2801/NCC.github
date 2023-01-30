package com.demo.opentalk.service;

import com.demo.opentalk.entity.CompanyBranch;
import com.demo.opentalk.repository.CompanyBranchRepository;
import com.demo.opentalk.service.Impl.CompanyBranchServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sun.javaws.JnlpxArgs.verify;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyBranchServiceTest {
//    @Mock
//    CompanyBranchService companyBranchService;

    @Mock
    CompanyBranchRepository companyBranchRepository;

    @InjectMocks
    CompanyBranchServiceImpl companyBranchService;

    @Test
    void test_getAllCompanyBranch() {
        List<CompanyBranch> mockCompanyBranchs = new ArrayList<>();
        for(int i = 1 ; i <= 5; i++) {
            mockCompanyBranchs.add(new CompanyBranch(i, "branch" + i, null));
        }
        when(companyBranchRepository.findAll()).thenReturn(mockCompanyBranchs);
        List<CompanyBranch> companyBranchs = companyBranchService.getAllCompanyBranch();
        assertThat(companyBranchs.size()).isEqualTo(mockCompanyBranchs.size());
        Mockito.verify(companyBranchRepository).findAll();
    }

    @Test
    void test_getCompanyById() {
        CompanyBranch mockCompanyBranch = new CompanyBranch(2, "branch2", null);
        mockCompanyBranch = companyBranchRepository.save(mockCompanyBranch);

        Optional<CompanyBranch> companyBranchOptional = companyBranchRepository.findById(mockCompanyBranch.getBranchNo());
        assertThat(companyBranchOptional.get().getBranchNo()).isEqualTo(mockCompanyBranch.getBranchNo());
        assertThat(companyBranchOptional.get().getBranchName()).isEqualTo(mockCompanyBranch.getBranchName());

    }
}
