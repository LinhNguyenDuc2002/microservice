package com.example.orderservice.service.impl;

import com.example.orderservice.constant.I18nMessage;
import com.example.orderservice.dto.ReceiverDto;
import com.example.orderservice.dto.request.ReceiverRequest;
import com.example.orderservice.entity.Address;
import com.example.orderservice.entity.Receiver;
import com.example.orderservice.mapper.ReceiverMapper;
import com.example.orderservice.repository.ReceiverRepository;
import com.example.orderservice.repository.predicate.ReceiverPredicate;
import com.example.orderservice.security.SecurityUtil;
import com.example.orderservice.service.ReceiverService;
import com.example.servicefoundation.exception.I18nException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReceiverServiceImpl implements ReceiverService {
    @Autowired
    private ReceiverRepository receiverRepository;

    @Autowired
    private ReceiverMapper receiverMapper;

    @Override
    public ReceiverDto add(ReceiverRequest receiverRequest) throws I18nException {
        ReceiverPredicate receiverPredicate = new ReceiverPredicate()
                .withStatus(true)
                .withNameOrPhoneNumber(receiverRequest.getName(), receiverRequest.getPhoneNumber());
        boolean check = receiverRepository.exists(receiverPredicate.getCriteria());
        if (check) {
            throw I18nException.builder()
                    .code(HttpStatus.BAD_REQUEST)
                    .message(I18nMessage.ERROR_RECEIVER_INFO_EXIST)
                    .build();
        }

        Optional<String> userId = SecurityUtil.getLoggedInUserId();
        if (!userId.isPresent()) {
            throw I18nException.builder()
                    .code(HttpStatus.UNAUTHORIZED)
                    .message(I18nMessage.ERROR_UNAUTHORIZED)
                    .build();
        }

        Address address = Address.builder()
                .country(receiverRequest.getAddress().getCountry())
                .city(receiverRequest.getAddress().getCity())
                .district(receiverRequest.getAddress().getDistrict())
                .ward(receiverRequest.getAddress().getWard())
                .detail(receiverRequest.getAddress().getDetail())
                .build();
        Receiver receiver = Receiver.builder()
                .name(receiverRequest.getName())
                .phoneNumber(receiverRequest.getPhoneNumber())
                .accountId(userId.get())
                .status(true)
                .address(address)
                .build();
        receiverRepository.save(receiver);

        return receiverMapper.toDto(receiver);
    }

    @Override
    public ReceiverDto update(String id, ReceiverRequest receiverRequest) throws I18nException {
        ReceiverPredicate receiverPredicate = new ReceiverPredicate()
                .withId(id)
                .withStatus(true);
        Receiver receiver = receiverRepository.findOne(receiverPredicate.getCriteria())
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_RECEIVER_NOT_FOUND)
                            .build();
                });

        receiverPredicate = new ReceiverPredicate()
                .withStatus(true)
                .withNameOrPhoneNumber(receiverRequest.getName(), receiverRequest.getPhoneNumber());
        boolean check = receiverRepository.exists(receiverPredicate.getCriteria());
        if (check) {
            throw I18nException.builder()
                    .code(HttpStatus.BAD_REQUEST)
                    .message(I18nMessage.ERROR_RECEIVER_INFO_EXIST)
                    .build();
        }

        Address address = receiver.getAddress();
        if (address == null) {
            address = new Address();
        }
        address.setCountry(receiverRequest.getAddress().getCountry());
        address.setCity(receiverRequest.getAddress().getCity());
        address.setDistrict(receiverRequest.getAddress().getDistrict());
        address.setWard(receiverRequest.getAddress().getWard());
        address.setDetail(receiverRequest.getAddress().getDetail());

        receiver.setName(receiverRequest.getName());
        receiver.setPhoneNumber(receiverRequest.getPhoneNumber());
        receiver.setAddress(address);
        receiverRepository.save(receiver);

        return receiverMapper.toDto(receiver);
    }

    @Override
    public ReceiverDto get(String id) throws I18nException {
        ReceiverPredicate receiverPredicate = new ReceiverPredicate()
                .withId(id)
                .withStatus(true);
        Optional<Receiver> check = receiverRepository.findOne(receiverPredicate.getCriteria());
        if (check.isPresent()) {
            throw I18nException.builder()
                    .code(HttpStatus.NOT_FOUND)
                    .message(I18nMessage.ERROR_RECEIVER_NOT_FOUND)
                    .build();
        }

        return receiverMapper.toDto(check.get());
    }

    @Override
    public List<ReceiverDto> getAll(String id) {
        ReceiverPredicate receiverPredicate = new ReceiverPredicate()
                .withAccountId(id)
                .withStatus(true);
        List<Receiver> receivers = receiverRepository.findAll(receiverPredicate.getCriteria());

        return receiverMapper.toDtoList(receivers);
    }

    @Override
    public void delete(String id) throws I18nException {
        Receiver receiver = receiverRepository.findById(id)
                .orElseThrow(() -> {
                    return I18nException.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message(I18nMessage.ERROR_RECEIVER_NOT_FOUND)
                            .build();
                });

        receiver.setStatus(false);
        receiverRepository.save(receiver);
    }
}
