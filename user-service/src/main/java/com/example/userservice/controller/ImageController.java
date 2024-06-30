package com.example.userservice.controller;

import com.example.userservice.constant.ResponseMessage;
import com.example.userservice.dto.response.CommonResponse;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.service.ImageService;
import com.example.userservice.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<CommonResponse<String>> setAvatar(@RequestParam("avatar") MultipartFile file) throws IOException {
        return ResponseUtil.wrapResponse(imageService.setAvatar(file), ResponseMessage.SET_AVATAR_SUCCESS.getMessage());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<String>> deleteAvatar(@PathVariable String id) throws NotFoundException {
        imageService.deleteAvatar(id);
        return ResponseUtil.wrapResponse(null, ResponseMessage.DELETE_AVATAR_SUCCESS.getMessage());
    }
}
