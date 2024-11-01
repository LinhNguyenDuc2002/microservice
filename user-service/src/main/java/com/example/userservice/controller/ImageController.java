package com.example.userservice.controller;

import com.example.userservice.constant.I18nMessage;
import com.example.userservice.dto.response.Response;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.i18n.I18nService;
import com.example.userservice.service.ImageService;
import com.example.userservice.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/avatar")
@RestController
public class ImageController {
    @Autowired
    private ImageService imageService;

    @Autowired
    private I18nService i18nService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Response<String>> setAvatar(@RequestParam("avatar") MultipartFile file) throws IOException {
        return ResponseUtil.wrapResponse(
                imageService.setAvatar(file),
                i18nService.getMessage(I18nMessage.INFO_SET_AVATAR, LocaleContextHolder.getLocale())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> deleteAvatar(@PathVariable String id) throws NotFoundException {
        imageService.deleteAvatar(id);
        return ResponseUtil.wrapResponse(
                i18nService.getMessage(I18nMessage.INFO_DELETE_AVATAR, LocaleContextHolder.getLocale())
        );
    }
}
