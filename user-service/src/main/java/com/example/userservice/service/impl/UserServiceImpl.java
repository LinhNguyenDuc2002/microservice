package com.example.userservice.service.impl;

import com.example.userservice.cache.UserCacheManager;
import com.example.userservice.config.ApplicationConfig;
import com.example.userservice.constant.ExceptionMessage;
import com.example.userservice.constant.KafkaTopic;
import com.example.userservice.constant.RoleType;
import com.example.userservice.dto.UserAddressDTO;
import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.request.AddressRequest;
import com.example.userservice.dto.request.OTPAuthenticationRequest;
import com.example.userservice.dto.request.UserRegistration;
import com.example.userservice.dto.request.UserRequest;
import com.example.userservice.entity.Address;
import com.example.userservice.entity.Role;
import com.example.userservice.entity.User;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.exception.UnauthorizedException;
import com.example.userservice.exception.ValidationException;
import com.example.userservice.mapper.AddressMapper;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.message.email.EmailConstant;
import com.example.userservice.message.email.EmailMessage;
import com.example.userservice.payload.CustomerRequest;
import com.example.userservice.redis.model.UserCache;
import com.example.userservice.repository.AddressRepository;
import com.example.userservice.repository.RoleRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.util.SecurityUtils;
import com.example.userservice.service.UserService;
import com.example.userservice.util.OTPUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public UserDto getLoggedInUser() {
        log.info("Get info of logged in user");

        Optional<String> userId = SecurityUtils.getLoggedInUserId();
        if (userId.isEmpty()) {
            throw new UnauthorizedException(ExceptionMessage.ERROR_USER_UNKNOWN);
        }

        User user = userRepository.findById(userId.get())
                .orElseThrow(() -> {
                    return new UnauthorizedException(ExceptionMessage.ERROR_USER_UNKNOWN);
                });

        log.info("Got info of logged in user successfully");
        return userMapper.toDto(user);
    }

    @Override
    public Map<String, String> createTempUser(UserRegistration newUserRegistration) throws ValidationException, NoSuchAlgorithmException, InvalidKeyException {
        log.info("Save registration information temporarily");

        if (!StringUtils.hasText(newUserRegistration.getEmail())) {
            log.error("Email is invalid");
            throw new ValidationException(newUserRegistration, ExceptionMessage.ERROR_EMAIL_INVALID);
        }
        if (userRepository.existsByEmail(newUserRegistration.getEmail())) {
            log.error("Email is already in use");
            throw new ValidationException(newUserRegistration, ExceptionMessage.ERROR_EMAIL_EXISTED);
        }

        String otp = OTPUtil.generateOTP();
        UserCache userCache = convertToUserCache(newUserRegistration);
        userCache.setOtp(otp);

        Map<String, String> emailArgs = new HashMap<>();
        emailArgs.put(EmailConstant.ARG_LOGO_URI, "");
        emailArgs.put(EmailConstant.ARG_OTP_CODE, otp);
        emailArgs.put(EmailConstant.ARG_RECEIVER_NAME, userCache.getFullname());
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
//        messagingService.sendMessage(KafkaTopic.SEND_EMAIL, mapper.writeValueAsString(email));
        log.info("OTP code is sent successfully");

        userCacheManager.storeUserCache(userCache);
        return Map.of(
                "secret_key", userCache.getId()
        );
    }

    @Override
    public UserDto createUser(OTPAuthenticationRequest request) throws ValidationException, NotFoundException, JsonProcessingException {
        UserCache userCache = userCacheManager.getUserCache(request.getSecret())
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_USER_CACHE_NOT_FOUND);
                });
        if (!StringUtils.hasText(request.getOtp()) || !userCache.getOtp().equals(request.getOtp())) {
            throw new ValidationException(request.getOtp(), ExceptionMessage.ERROR_INVALID_OTP);
        }

        User user = convertToUser(userCache);
        userCacheManager.clearUserCache(userCache.getId());

        Set<Role> roles = new HashSet<>();
        RoleType roleType = RoleType.valueOf(userCache.getRole());
        roles.add(roleRepository.findByRoleName(roleType));

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        CustomerRequest customerRequest = CustomerRequest.builder()
                .accountId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .fullname(user.getFullname())
                .phone(user.getPhone())
                .role(RoleType.CUSTOMER.name())
                .build();
        messagingService.sendMessage(KafkaTopic.CREATE_CUSTOMER, mapper.writeValueAsString(customerRequest));

        Map<String, String> emailArgs = new HashMap<>();
        emailArgs.put(EmailConstant.ARG_LOGO_URI, "");
        emailArgs.put(EmailConstant.ARG_RECEIVER_NAME, user.getFullname());
        emailArgs.put(EmailConstant.ARG_SUPPORT_EMAIL, applicationConfig.getSenderEmail());

        EmailMessage email = EmailMessage.builder()
                .template(EmailConstant.TEMPLATE_EMAIL_CREATE_ACCOUNT)
                .receiver(user.getEmail())
                .sender(applicationConfig.getSenderEmail())
                .subject(EmailConstant.ARG_CREATE_ACCOUNT_SUBJECT)
                .args(emailArgs)
                .locale(LocaleContextHolder.getLocale())
                .build();
        messagingService.sendMessage(KafkaTopic.SEND_EMAIL, mapper.writeValueAsString(email));
        return userMapper.toDto(user);
    }

    @Override
    public void delete(String id) throws NotFoundException {
        log.info("Delete user {}", id);

        boolean checkUser = userRepository.existsById(id);
        if (!checkUser) {
            log.error("User {} don't exist", id);
            throw NotFoundException.builder()
                    .message(ExceptionMessage.ERROR_USER_NOT_FOUND)
                    .build();
        }

        userRepository.deleteById(id);
        log.info("Deleted user with {}", id);
    }

    @Override
    public List<UserDto> getAll() {
        log.info("Get all users");
        return userMapper.toDtoList(userRepository.findAll());
    }

    @Override
    public UserDto get(String id) throws NotFoundException {
        log.info("Get user {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User {} don't exist", id);
                    return NotFoundException.builder()
                            .message(ExceptionMessage.ERROR_USER_NOT_FOUND)
                            .build();
                });

        log.info("Got user {} successfully", id);
        return userMapper.toDto(user);
    }

    @Override
    public UserAddressDTO update(String id, UserRequest userRequest) throws NotFoundException, ValidationException {
        log.info("Update user {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User {} don't exist", id);
                    return NotFoundException.builder()
                            .message(ExceptionMessage.ERROR_USER_NOT_FOUND)
                            .build();
                });

        if (!user.getUsername().equals(userRequest.getUsername())) {
            boolean checkUsername = userRepository.existsByUsername(userRequest.getUsername());
            if (checkUsername) {
                log.error("Username {} existed", userRequest.getUsername());
                throw new ValidationException(userRequest, ExceptionMessage.ERROR_USERNAME_EXISTED);
            }
            user.setUsername(userRequest.getUsername());
        }

        AddressRequest addressRequest = userRequest.getAddressRequest();
        Address address = user.getAddress();
        if (address == null) {
            address = Address.builder()
                    .detail(addressRequest.getSpecificAddress())
                    .ward(addressRequest.getWard())
                    .district(addressRequest.getDistrict())
                    .city(addressRequest.getCity())
                    .country(addressRequest.getCountry())
                    .build();
            addressRepository.save(address);
            user.setAddress(address);
        } else {
            address.setDetail(addressRequest.getSpecificAddress());
            address.setWard(addressRequest.getWard());
            address.setDistrict(addressRequest.getDistrict());
            address.setCity(addressRequest.getCity());
            address.setCountry(addressRequest.getCountry());
        }
        user.setFullname(userRequest.getFullname());
        user.setDob(userRequest.getDob());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());

        userRepository.save(user);

        log.info("Updated user {} successfully", id);
        return UserAddressDTO.builder()
                .user(userMapper.toDto(user))
                .address(addressMapper.toDto(user.getAddress()))
                .build();
    }

    public User convertToUser(UserCache userCache) {
        return User.builder()
                .username(userCache.getUsername())
                .nickname(userCache.getNickname())
                .password(userCache.getPassword())
                .fullname(userCache.getFullname())
                .email(userCache.getEmail())
                .phone(userCache.getPhone())
                .sex(userCache.getSex())
                .dob(userCache.getDob())
                .build();
    }

    public UserCache convertToUserCache(UserRegistration newUserRegistration) {
        return UserCache.builder()
                .id(UUID.randomUUID().toString())
                .nickname(newUserRegistration.getNickname())
                .username(newUserRegistration.getUsername())
                .password(newUserRegistration.getPassword())
                .fullname(newUserRegistration.getFullname())
                .dob(newUserRegistration.getDob())
                .phone(newUserRegistration.getPhone())
                .email(newUserRegistration.getEmail())
                .sex(newUserRegistration.getSex())
                .role(newUserRegistration.getRole())
                .build();
    }
}
