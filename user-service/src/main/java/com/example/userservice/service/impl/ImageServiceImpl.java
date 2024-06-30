package com.example.userservice.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.userservice.constant.ExceptionMessage;
import com.example.userservice.constant.ResponseMessage;
import com.example.userservice.entity.Image;
import com.example.userservice.entity.User;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.exception.UnauthorizedException;
import com.example.userservice.repository.ImageRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.ImageService;
import com.example.userservice.util.DateUtil;
import com.example.userservice.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String setAvatar(MultipartFile file) throws IOException {
        log.info("Get info of logged in user");

        Optional<String> userId = SecurityUtil.getLoggedInUserId();
        if (userId.isEmpty()) {
            throw new UnauthorizedException(ExceptionMessage.ERROR_USER_UNKNOWN);
        }

        User user = userRepository.findById(userId.get())
                .orElseThrow(() -> {
                    return new UnauthorizedException(ExceptionMessage.ERROR_USER_UNKNOWN);
                });

        Image avatar = user.getAvatar();
        if (avatar == null) {
            avatar = Image.builder().user(user).build();
        }

        Map<String, String> data = uploadFile(file);

        avatar.setFormat(data.get("format"));
        avatar.setResourceType(data.get("resource_type"));
        avatar.setUrl(data.get("url"));
        avatar.setSecureUrl(data.get("secure_url"));
        avatar.setCreatedAt(DateUtil.convertStringToDate(data.get("created_at")));
        avatar.setPublicId(data.get("public_id"));
        imageRepository.save(avatar);

        log.info("Set avatar successfully");
        return avatar.getUrl();
    }

    @Override
    public void deleteAvatar(String id) throws NotFoundException {
        log.info("Delete avatar");

        boolean check = imageRepository.existsById(id);
        if (!check) {
            throw new NotFoundException(ResponseMessage.DELETE_AVATAR_SUCCESS.getMessage());
        }

        imageRepository.deleteById(id);
    }

    public Map<String, String> uploadFile(MultipartFile file) throws IOException {
        List<Image> images = new ArrayList<>();
        Map data = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

        return data;
    }

    private void destroyFile(String publicId, Map map) throws IOException {
        cloudinary.uploader().destroy(publicId, map);
    }
}
