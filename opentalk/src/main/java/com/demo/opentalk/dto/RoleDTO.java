package com.demo.opentalk.dto;

import com.demo.opentalk.config.ERole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RoleDTO {
    private int roleNo;
    private ERole roleName;
}
