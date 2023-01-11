package com.demo.opentalk.dto.request;

import com.demo.opentalk.dto.EmployeeDTO;
import com.demo.opentalk.dto.ResultDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeesRequestDTO {
    private List<EmployeeDTO> employeeDTOList;
    private String password;
    private int branchNo;
    private boolean active;
    private List<Integer> roleNos;
}
