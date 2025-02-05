package com.example.productservice.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.productservice.constant.CloudinaryConstant;
import com.example.productservice.entity.Image;
import com.example.productservice.repository.ImageRepository;
import com.example.productservice.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    private final String FOLDER = "product-service";

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public void upload(MultipartFile file, Map<String, String> args) throws IOException {
        Map<String, String> result = cloudinary.uploader().upload(file.getBytes(), args);
        Image image = Image.builder()
                .id(result.get(CloudinaryConstant.PUBLIC_ID))
                .format(result.get(CloudinaryConstant.FORMAT))
                .resourceType(result.get(CloudinaryConstant.RESOURCE_TYPE))
                .secureUrl(result.get(CloudinaryConstant.SECURE_URL))
                .build();

        imageRepository.save(image);
    }

    @Override
    public void destroy(String id) throws IOException {
        if (StringUtils.hasText(id)) {
            cloudinary.uploader().destroy(
                    id,
                    ObjectUtils.asMap(CloudinaryConstant.INVALIDATE, true));
        }
    }

    @Override
    public void upload(Map<String, MultipartFile> images) {
        List<Image> imageEntities = new ArrayList<>();

        images.entrySet().stream().forEach(image -> {
            Map<String, String> args = ObjectUtils.asMap();
            args.put(CloudinaryConstant.FOLDER, FOLDER);
            args.put(CloudinaryConstant.PUBLIC_ID, image.getKey());

            Map<String, String> result = null;
            try {
                result = cloudinary.uploader().upload(image.getValue().getBytes(), args);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            imageEntities.add(
                    Image.builder()
                            .id(result.get(CloudinaryConstant.PUBLIC_ID))
                            .format(result.get(CloudinaryConstant.FORMAT))
                            .resourceType(result.get(CloudinaryConstant.RESOURCE_TYPE))
                            .secureUrl(result.get(CloudinaryConstant.SECURE_URL))
                            .build()
            );
        });

        imageRepository.saveAll(imageEntities);
    }

    @Override
    public void destroy(List<String> ids) {
        ids.stream().forEach(id -> {
            if (StringUtils.hasText(id)) {
                try {
                    cloudinary.uploader().destroy(
                            id,
                            ObjectUtils.asMap(CloudinaryConstant.INVALIDATE, true));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
