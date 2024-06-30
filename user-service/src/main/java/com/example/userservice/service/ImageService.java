package com.example.userservice.service;

import com.example.userservice.exception.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String setAvatar(MultipartFile file) throws IOException;

    void deleteAvatar(String id) throws NotFoundException;
}
