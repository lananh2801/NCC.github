package com.demo.opentalk.repository;

import com.demo.opentalk.entity.CompanyBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;

@Repository
public interface CompanyBranchRepository extends JpaRepository<CompanyBranch, Integer> {
    CompanyBranch findCompanyBranchByBranchNo(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE CompanyBranch cb " +
            "SET cb.branchName = :branchName " +
            "WHERE cb.branchNo = :branchNo")
    void updateCompany(@Param("branchNo") Integer branchNo,
                                @Param("branchName") String branchName);

    @Modifying
    void removeCompanyBranchByBranchNo(@Param("branchNo") Integer branchNo);
}
