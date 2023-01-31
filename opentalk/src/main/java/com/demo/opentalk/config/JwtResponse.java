package com.demo.opentalk.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtResponse {
    private String jwt;
    private Integer id;
    private String username;
    private String email;
    private List<String> roles;
}
