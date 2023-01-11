package com.demo.opentalk.dto.response;

import com.demo.opentalk.config.ERole;
import lombok.*;

import java.util.List;

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
    private List<ERole> roleNames;
}
