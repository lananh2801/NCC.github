package com.demo.opentalk.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class EmployeeResponseDTO {
    private int employeeNo;
    private String fullName;
    private String userName;
    private String email;
    private boolean active;
    private String branchName;
    private String roleName;
}
