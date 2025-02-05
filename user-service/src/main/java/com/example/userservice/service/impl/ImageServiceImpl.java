package com.example.userservice.service.impl;

import com.example.servicefoundation.exception.I18nException;
import com.example.userservice.constant.I18nMessage;
import com.example.userservice.entity.User;
import com.example.userservice.repository.ImageRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.util.SecurityUtils;
import com.example.userservice.service.CloudinaryService;
import com.example.userservice.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public String setAvatar(MultipartFile file) throws IOException, I18nException {
        Optional<String> userId = SecurityUtils.getLoggedInUserId();
        if (userId.isEmpty()) {
            throw I18nException.builder()
                    .code(HttpStatus.UNAUTHORIZED)
                    .message(I18nMessage.ERROR_USER_UNKNOWN)
                    .build();
        }

        User user = userRepository.findById(userId.get())
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.UNAUTHORIZED)
                            .message(I18nMessage.ERROR_USER_UNKNOWN)
                            .build();
                });

        if (file == null || file.isEmpty()) {
            throw I18nException.builder()
                    .message("")
                    .build();
        }

        String id = UUID.randomUUID().toString();
        Map<String, MultipartFile> images = new HashMap<>();
        images.put(id, file);

        String imageId = user.getImageId();
        if (StringUtils.hasText(imageId)) {
            imageRepository.deleteById(imageId);
            cloudinaryService.destroy(imageId);
        }
        user.setImageId(id);
        userRepository.save(user);
        cloudinaryService.upload(images);

        return "";
    }

    @Override
    public void deleteAvatar() throws IOException, I18nException {
        Optional<String> userId = SecurityUtils.getLoggedInUserId();
        if (userId.isEmpty()) {
            throw I18nException.builder()
                    .code(HttpStatus.UNAUTHORIZED)
                    .message(I18nMessage.ERROR_USER_UNKNOWN)
                    .build();
        }

        User user = userRepository.findById(userId.get())
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_USER_NOT_FOUND)
                            .build();
                });
        String imageId = user.getImageId();
        if (StringUtils.hasText(imageId)) {
            imageRepository.deleteById(imageId);
            cloudinaryService.destroy(imageId);
            user.setImageId(null);
            userRepository.save(user);
        }
    }
}
