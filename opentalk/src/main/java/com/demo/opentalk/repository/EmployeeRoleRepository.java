package com.demo.opentalk.repository;

import com.demo.opentalk.entity.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRoleRepository extends JpaRepository<EmployeeRole, Integer> {
    @Modifying
    @Query("DELETE FROM EmployeeRole " +
            "WHERE employee.employeeNo = :employeeNo")
    public void findEmployeeRolesByEmployeeNo(@Param("employeeNo") Integer employeeNo);
}
