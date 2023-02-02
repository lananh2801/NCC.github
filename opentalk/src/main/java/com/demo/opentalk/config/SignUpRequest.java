package com.demo.opentalk.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class SignUpRequest {
    private String userName;
    private String password;
    private String email;
    private Integer branchNo;
    private Set<String> role;
}
