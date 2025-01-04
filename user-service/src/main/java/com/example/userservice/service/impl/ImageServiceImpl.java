package com.example.userservice.service.impl;

import com.example.userservice.constant.I18nMessage;
import com.example.userservice.entity.Image;
import com.example.userservice.entity.User;
import com.example.userservice.exception.InvalidationException;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.exception.UnauthorizedException;
import com.example.userservice.repository.ImageRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.util.SecurityUtils;
import com.example.userservice.service.CloudinaryService;
import com.example.userservice.service.ImageService;
import com.example.userservice.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String setAvatar(MultipartFile file) throws IOException, InvalidationException {
        Optional<String> userId = SecurityUtils.getLoggedInUserId();
        if (userId.isEmpty()) {
            throw new UnauthorizedException(I18nMessage.ERROR_USER_UNKNOWN);
        }

        User user = userRepository.findById(userId.get())
                .orElseThrow(() -> {
                    return new UnauthorizedException(I18nMessage.ERROR_USER_UNKNOWN);
                });

        if(file == null || file.isEmpty()) {
            throw new InvalidationException("");
        }

        String id = UUID.randomUUID().toString();
        Map<String, MultipartFile> images = new HashMap<>();
        images.put(id, file);

        String imageId = user.getImageId();
        if(StringUtils.hasText(imageId)) {
            imageRepository.deleteById(imageId);
            cloudinaryService.destroy(imageId);
        }
        user.setImageId(id);
        userRepository.save(user);
        cloudinaryService.upload(images);

        return "";
    }

    @Override
    public void deleteAvatar() throws NotFoundException, IOException {
        Optional<String> userId = SecurityUtils.getLoggedInUserId();
        if (userId.isEmpty()) {
            throw new UnauthorizedException(I18nMessage.ERROR_USER_UNKNOWN);
        }

        User user = userRepository.findById(userId.get())
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_USER_NOT_FOUND);
                });
        String imageId = user.getImageId();
        if(StringUtils.hasText(imageId)) {
            imageRepository.deleteById(imageId);
            cloudinaryService.destroy(imageId);
            user.setImageId(null);
            userRepository.save(user);
        }
    }
}
