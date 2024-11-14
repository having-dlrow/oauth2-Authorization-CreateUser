package com.example.gateway.controller;

import com.example.gateway.model.Role;
import com.example.gateway.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/role")
    public List<Role> getAllRoles() {
        return roleService.findAllRoles();
    }

    // CREATE
    @PostMapping("/role")
    @ResponseStatus(HttpStatus.CREATED)
    public Integer createRole(@RequestBody Role role) {
        return roleService.createRole(role);
    }
}
