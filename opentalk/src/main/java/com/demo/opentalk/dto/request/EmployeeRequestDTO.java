package com.demo.opentalk.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class EmployeeRequestDTO {
    private int employeeNo;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean active;
    private int branchNo;
    private int roleNo;
}
