package com.example.userservice.service;

import com.example.servicefoundation.exception.I18nException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String setAvatar(MultipartFile file) throws IOException, I18nException;

    void deleteAvatar() throws IOException, I18nException;
}
