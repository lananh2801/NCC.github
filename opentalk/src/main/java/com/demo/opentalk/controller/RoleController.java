package com.demo.opentalk.controller;

import com.demo.opentalk.dto.RoleDTO;
import com.demo.opentalk.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("add-role")
    public RoleDTO addRole(@RequestBody RoleDTO roleDTO) {
        return roleService.addRole(roleDTO);
    }
}
