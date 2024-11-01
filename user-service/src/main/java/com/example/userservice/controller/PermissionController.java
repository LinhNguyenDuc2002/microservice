package com.example.userservice.controller;

import com.example.userservice.constant.I18nMessage;
import com.example.userservice.dto.PermissionDto;
import com.example.userservice.dto.request.PermissionRequest;
import com.example.userservice.dto.response.Response;
import com.example.userservice.exception.InvalidationException;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.i18n.I18nService;
import com.example.userservice.service.PermissionService;
import com.example.userservice.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private I18nService i18nService;

    @Autowired
    private PermissionService permissionService;

    @GetMapping
    public ResponseEntity<Response<List<PermissionDto>>> getAll() {
        return ResponseUtil.wrapResponse(
                permissionService.getAll(),
                i18nService.getMessage(I18nMessage.INFO_GET_ALL_PERMISSION, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<PermissionDto>> get(@PathVariable String id) throws NotFoundException {
        return ResponseUtil.wrapResponse(
                permissionService.get(id),
                i18nService.getMessage(I18nMessage.INFO_GET_PERMISSION, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping("/role/{id}")
    public ResponseEntity<Response<List<PermissionDto>>> getByRoleId(@PathVariable String id) throws NotFoundException {
        return ResponseUtil.wrapResponse(
                permissionService.getByRoleId(id),
                i18nService.getMessage(I18nMessage.INFO_GET_ALL_PERMISSION, LocaleContextHolder.getLocale())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<PermissionDto>> update(@PathVariable String id, @Valid @RequestBody PermissionRequest permissionRequest) throws NotFoundException, InvalidationException {
        return ResponseUtil.wrapResponse(
                permissionService.update(id, permissionRequest),
                i18nService.getMessage(I18nMessage.INFO_UPDATE_PERMISSION, LocaleContextHolder.getLocale())
        );
    }
}
