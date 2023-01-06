package com.demo.opentalk.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CompanyBranchResponseDTO {
    private int branchNo;
    private String branchName;
    private List<EmployeeResponseDTO> employeeResponseDTOList;
}

