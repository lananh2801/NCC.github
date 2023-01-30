package com.demo.opentalk.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "COMPANY_BRANCH")
public class CompanyBranch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BRANCH_NO")
    private int branchNo;

    @Column(name = "BRANCH_NAME")
    private String branchName;

    @OneToMany(mappedBy = "companyBranch", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Employee> employeeList;
}
