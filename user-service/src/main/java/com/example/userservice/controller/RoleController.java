package com.example.userservice.controller;

import com.example.userservice.dto.response.CommonResponse;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.service.RoleService;
import com.example.userservice.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PatchMapping("/mapping")
    public ResponseEntity<CommonResponse<Void>> assignRole(
            @RequestParam("role") String roleName,
            @RequestParam("user") String userId) throws NotFoundException {
        roleService.assignRole(roleName, userId);
        return ResponseUtil.wrapResponse(null, "");
    }

    @DeleteMapping("/mapping")
    public ResponseEntity<CommonResponse<Void>> unassignRole(
            @RequestParam("role") String roleName,
            @RequestParam("user") String userId) throws NotFoundException {
        roleService.unassignRole(roleName, userId);
        return ResponseUtil.wrapResponse(null, "");
    }
}
