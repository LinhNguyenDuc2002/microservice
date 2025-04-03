package com.example.userservice.service.impl;

import com.example.servicefoundation.exception.I18nException;
import com.example.servicefoundation.mail.message.EmailConstant;
import com.example.servicefoundation.mail.message.EmailMessage;
import com.example.servicefoundation.mail.service.EmailService;
import com.example.servicefoundation.util.OTPUtil;
import com.example.userservice.cache.UserCacheManager;
import com.example.userservice.config.ApplicationConfig;
import com.example.userservice.constant.I18nMessage;
import com.example.userservice.constant.RoleType;
import com.example.userservice.dto.BasicUserInfoDto;
import com.example.userservice.dto.UserAddressDTO;
import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.request.AddressRequest;
import com.example.userservice.dto.request.OTPAuthenticationRequest;
import com.example.userservice.dto.request.UpdateInfo;
import com.example.userservice.dto.request.UserRegistration;
import com.example.userservice.dto.request.UserRegistrationHasRole;
import com.example.userservice.dto.request.UserRequest;
import com.example.userservice.entity.Address;
import com.example.userservice.entity.Image;
import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import com.example.userservice.mapper.AddressMapper;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.redis.model.UserCache;
import com.example.userservice.repository.AddressRepository;
import com.example.userservice.repository.ImageRepository;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.util.SecurityUtils;
import com.example.userservice.service.CloudinaryService;
import com.example.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderMessagingServiceImpl messagingService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserCacheManager userCacheManager;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ApplicationConfig applicationConfig;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CloudinaryService cloudinaryService;

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public UserDto getLoggedInUser() throws I18nException {
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

        UserDto userDto = userMapper.toDto(user);
        Optional<Image> avatar = imageRepository.findById(user.getImageId());
        if(avatar.isPresent()) {
            userDto.setAvatarUrl(avatar.get().getSecureUrl());
        }
        return userDto;
    }

    @Override
    public Map<String, String> createTempUser(UserRegistration userRegistration) throws NoSuchAlgorithmException, InvalidKeyException, I18nException {
        if (userRepository.existsByEmail(userRegistration.getEmail())) {
            throw I18nException.builder()
                    .code(HttpStatus.BAD_REQUEST)
                    .message(I18nMessage.ERROR_EMAIL_EXISTED)
                    .object(userRegistration)
                    .build();
        }

        String otp = OTPUtil.generateOTP();
        UserCache userCache = convertToUserCache(userRegistration);
        userCache.setOtp(otp);

        Map<String, String> emailArgs = new HashMap<>();
        emailArgs.put(EmailConstant.ARG_LOGO_URI, "");
        emailArgs.put(EmailConstant.ARG_OTP_CODE, otp);
        emailArgs.put(EmailConstant.ARG_SUPPORT_EMAIL, applicationConfig.getSenderEmail());

        EmailMessage email = EmailMessage.builder()
                .template(EmailConstant.TEMPLATE_EMAIL_VERIFY_OTP)
                .receiver(userCache.getEmail())
                .sender(applicationConfig.getSenderEmail())
                .subject(EmailConstant.ARG_VERIFY_EMAIL_SUBJECT)
                .args(emailArgs)
                .locale(LocaleContextHolder.getLocale())
                .build();
        log.info("Sending OTP to authenticate ...");
        emailService.send(email);
        log.info("OTP code is sent successfully");

        userCacheManager.storeUserCache(userCache);
        return Map.of(
                "secret_key", userCache.getId()
        );
    }

    @Override
    public UserDto createUser(OTPAuthenticationRequest request) throws I18nException {
        UserCache userCache = userCacheManager.getUserCache(request.getSecret())
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_USER_CACHE_NOT_FOUND)
                            .build();
                });
        if (!userCache.getOtp().equals(request.getOtp())) {
            throw I18nException.builder()
                    .code(HttpStatus.BAD_REQUEST)
                    .message(I18nMessage.ERROR_INVALID_OTP)
                    .object(request.getOtp())
                    .build();
        }

        User user = convertToUser(userCache);
        user.setStatus(true);
        user.setFirstLogin(true);
        userCacheManager.clearUserCache(userCache.getId());

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(RoleType.CUSTOMER));

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    public UserDto createUser(UserRegistrationHasRole userRegistration) {
        User user = User.builder()
                .username(userRegistration.getUsername())
                .password(passwordEncoder.encode(userRegistration.getPassword()))
                .email(userRegistration.getEmail())
                .phone(userRegistration.getPhone())
                .firstLogin(true)
                .status(true)
                .build();

        Map<String, String> emailArgs = new HashMap<>();
        emailArgs.put(EmailConstant.ARG_LOGO_URI, "");
        emailArgs.put(EmailConstant.ARG_SUPPORT_EMAIL, applicationConfig.getSenderEmail());

        EmailMessage email = EmailMessage.builder()
                .template(EmailConstant.TEMPLATE_EMAIL_CREATE_ACCOUNT)
                .receiver(user.getEmail())
                .sender(applicationConfig.getSenderEmail())
                .subject(EmailConstant.ARG_CREATE_ACCOUNT_SUBJECT)
                .args(emailArgs)
                .locale(LocaleContextHolder.getLocale())
                .build();
//        messagingService.sendMessage(KafkaTopic.SEND_EMAIL, mapper.writeValueAsString(email));
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public List<BasicUserInfoDto> getUserInfo(List<String> ids) {
        List<User> users = userRepository.findAllById(ids);

        List<BasicUserInfoDto> response = new ArrayList<>();
        for (User user : users) {
            BasicUserInfoDto basicUserInfoDto = BasicUserInfoDto.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .status(user.isStatus())
                    .build();

            Optional<Image> image = imageRepository.findById(user.getImageId());
            if (image.isPresent()) {
                basicUserInfoDto.setAvatarUrl(image.get().getSecureUrl());
            }
            response.add(basicUserInfoDto);
        }

        return response;
    }

    @Override
    public void delete(String id) throws I18nException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_USER_NOT_FOUND)
                            .build();
                });

        user.setStatus(false);
        userRepository.save(user);
    }

    @Override
    public List<UserDto> getAll() {
        log.info("Get all users");
        return userMapper.toDtoList(userRepository.findAll());
    }

    @Override
    public UserDto get(String id) throws I18nException {
        log.info("Get user {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User {} don't exist", id);
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_USER_NOT_FOUND)
                            .build();
                });

        log.info("Got user {} successfully", id);
        return userMapper.toDto(user);
    }

    @Override
    public UserDto update(UserRequest userRequest) throws I18nException, IOException {
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

//        if (!user.getUsername().equals(userRequest.getUsername())) {
//            boolean checkUsername = userRepository.existsByUsername(userRequest.getUsername());
//            if (checkUsername) {
//                throw I18nException.builder()
//                        .code(HttpStatus.BAD_REQUEST)
//                        .message(I18nMessage.ERROR_USERNAME_EXISTED)
//                        .object(userRequest)
//                        .build();
//            }
//            user.setUsername(userRequest.getUsername());
//        }

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setDob(userRequest.getDob());
        user.setSex(userRequest.getSex());
        user.setPhone(userRequest.getPhone());

        if (userRequest.getAvatar() != null && !userRequest.getAvatar().isEmpty()) {
            String id = UUID.randomUUID().toString();
            Map<String, MultipartFile> images = new HashMap<>();

            String imageId = user.getImageId();
            if (StringUtils.hasText(imageId)) {
                id = imageId;
            }

            images.put(id, userRequest.getAvatar());
            user.setImageId(id);
            cloudinaryService.upload(images);
        }

        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    public UserAddressDTO update(String id, UpdateInfo updateInfo) throws I18nException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_USER_NOT_FOUND)
                            .build();
                });

        AddressRequest addressRequest = updateInfo.getAddressRequest();
        Address address = Address.builder()
                .detail(addressRequest.getDetail())
                .ward(addressRequest.getWard())
                .district(addressRequest.getDistrict())
                .city(addressRequest.getCity())
                .country(addressRequest.getCountry())
                .build();
        addressRepository.save(address);
        user.setAddress(address);

        user.setFirstName(updateInfo.getFirstName());
        user.setLastName(updateInfo.getLastName());
        user.setDob(updateInfo.getDob());
        user.setSex(updateInfo.getSex());
        user.setFirstLogin(false);
        userRepository.save(user);

        return UserAddressDTO.builder()
                .user(userMapper.toDto(user))
                .address(addressMapper.toDto(user.getAddress()))
                .build();
    }

    public User convertToUser(UserCache userCache) {
        return User.builder()
                .username(userCache.getUsername())
                .password(userCache.getPassword())
                .email(userCache.getEmail())
                .phone(userCache.getPhone())
                .build();
    }

    public UserCache convertToUserCache(UserRegistration userRegistration) {
        return UserCache.builder()
                .id(UUID.randomUUID().toString())
                .username(userRegistration.getUsername())
                .password(userRegistration.getPassword())
                .phone(userRegistration.getPhone())
                .email(userRegistration.getEmail())
                .build();
    }
}
