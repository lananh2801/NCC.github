package com.demo.opentalk.repository;

import com.demo.opentalk.entity.Employee;
import com.demo.opentalk.model.projection.EmployeeProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.Date;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Modifying
    @Query("DELETE FROM Employee " +
            "WHERE companyBranch.branchNo = :branchNo")
    public void deleteEmployeesByBranchNo(Integer branchNo);

    @Query("SELECT em FROM Employee em  " +
            "WHERE (:branchNo IS NULL OR em.companyBranch.branchNo = :branchNo) " +
            "AND (:firstName IS NULL OR em.firstName = :firstName) " +
            "AND (:active IS NULL OR em.active = :active)")
    public List<Employee> getEmployeeByCriteria(@Param("branchNo") Integer branchNo,
                                                @Param("firstName") String firstName,
                                                @Param("active") Boolean active);

    @Query("SELECT em FROM Employee em " +
            "WHERE em.employeeNo NOT IN " +
                "(SELECT opt.employee.employeeNo FROM OpenTalkTopic opt " +
                "WHERE em.employeeNo = opt.employee.employeeNo " +
                "AND ((:startDate IS NULL OR :endDate IS NULL) " +
                "OR (opt.date BETWEEN :startDate AND :endDate))) " +
            "AND (:active IS NULL OR em.active = :active) " +
            "AND (:branchNo IS NULL OR em.companyBranch.branchNo = :branchNo) "  +
            "AND (:firstName IS NULL OR em.firstName = :firstName)")
    public Page<Employee> getEmployeeNoHostForOpenTalk(Pageable pageable,
                                                       @Param("active") Boolean active,
                                                       @Param("branchNo") Integer branchNo,
                                                       @Param("firstName") String firstName,
                                                       @Param("startDate") Date startDate,
                                                       @Param("endDate") Date endDate);
    @Query("SELECT em FROM Employee em " +
            "WHERE em.companyBranch.branchNo = :branchNo")
    public List<Employee> getEmployeesByBranchNo(@Param("branchNo") Integer branchNo);

}
