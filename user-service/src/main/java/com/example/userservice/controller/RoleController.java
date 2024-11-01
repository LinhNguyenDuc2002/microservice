package com.example.userservice.controller;

import com.example.userservice.constant.I18nMessage;
import com.example.userservice.dto.RoleDto;
import com.example.userservice.dto.request.RoleRequest;
import com.example.userservice.dto.response.Response;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.i18n.I18nService;
import com.example.userservice.service.RoleService;
import com.example.userservice.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private I18nService i18nService;

    @GetMapping
    public ResponseEntity<Response<List<RoleDto>>> getAll() {
        return ResponseUtil.wrapResponse(
                roleService.getAll(),
                i18nService.getMessage(I18nMessage.INFO_GET_ALL_ROLE, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<RoleDto>> get(@PathVariable String id) throws NotFoundException {
        return ResponseUtil.wrapResponse(
                roleService.get(id),
                i18nService.getMessage(I18nMessage.INFO_GET_ROLE, LocaleContextHolder.getLocale())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<RoleDto>> update(@PathVariable String id, @Valid @RequestBody RoleRequest roleRequest) throws NotFoundException {
        return ResponseUtil.wrapResponse(
                roleService.update(id, roleRequest),
                i18nService.getMessage(I18nMessage.INFO_UPDATE_ROLE, LocaleContextHolder.getLocale())
        );
    }

    @PatchMapping("/mapping")
    public ResponseEntity<Response<Void>> assignRole(
            @RequestParam("role") String roleName,
            @RequestParam("user") String userId) throws NotFoundException {
        roleService.assignRole(roleName, userId);
        return ResponseUtil.wrapResponse(
                ""
        );
    }

    @DeleteMapping("/mapping")
    public ResponseEntity<Response<Void>> unassignRole(
            @RequestParam("role") String roleName,
            @RequestParam("user") String userId) throws NotFoundException {
        roleService.unassignRole(roleName, userId);
        return ResponseUtil.wrapResponse(null, "");
    }
}
