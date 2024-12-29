package com.example.productservice.service;

import com.example.productservice.exception.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CloudinaryService {
    void upload(MultipartFile file, Map<String, String> args) throws IOException;

    void destroy(String id) throws NotFoundException, IOException;

    void upload(Map<String, MultipartFile> images) throws IOException;

    void destroy(List<String> ids) throws NotFoundException, IOException;
}
