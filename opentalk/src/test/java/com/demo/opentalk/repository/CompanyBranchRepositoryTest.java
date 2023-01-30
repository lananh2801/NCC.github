package com.demo.opentalk.repository;

import com.demo.opentalk.entity.CompanyBranch;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
public class CompanyBranchRepositoryTest {
    @Autowired
    private CompanyBranchRepository companyBranchRepository;
    private CompanyBranch companyBranch;

    @Test
    void test_getCompanyById() {
        CompanyBranch mockCompanyBranch = new CompanyBranch(2, "branch2", null);
        mockCompanyBranch = companyBranchRepository.save(mockCompanyBranch);

        Optional<CompanyBranch> companyBranchOptional = companyBranchRepository.findById(mockCompanyBranch.getBranchNo());
        assertThat(companyBranchOptional.get().getBranchNo()).isEqualTo(mockCompanyBranch.getBranchNo());
        assertThat(companyBranchOptional.get().getBranchName()).isEqualTo(mockCompanyBranch.getBranchName());

    }
}
