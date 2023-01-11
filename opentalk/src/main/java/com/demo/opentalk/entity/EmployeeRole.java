package com.demo.opentalk.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "EMPLOYEE_ROLE")
public class EmployeeRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @ManyToOne()
    @JoinColumn(name = "EMPLOYEE_NO", nullable = false)
    private Employee employee;

    @ManyToOne()
    @JoinColumn(name = "ROLE_NO", nullable = false)
    private Role role;
}
