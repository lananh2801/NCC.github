package com.demo.opentalk.repository;

import com.demo.opentalk.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query("SELECT r FROM Role r " +
            "WHERE r.roleNo IN (:roleNos)")
    public List<Role> getSetRoleByRoleNo(List<Integer> roleNos);
}
