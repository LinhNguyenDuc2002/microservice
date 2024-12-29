package com.example.orderservice.service.impl;

import com.example.orderservice.constant.I18nMessage;
import com.example.orderservice.dto.ReceiverDto;
import com.example.orderservice.dto.request.ReceiverRequest;
import com.example.orderservice.entity.Address;
import com.example.orderservice.entity.Receiver;
import com.example.orderservice.exception.InvalidationException;
import com.example.orderservice.exception.NotFoundException;
import com.example.orderservice.exception.UnauthorizedException;
import com.example.orderservice.mapper.ReceiverMapper;
import com.example.orderservice.repository.ReceiverRepository;
import com.example.orderservice.repository.predicate.ReceiverPredicate;
import com.example.orderservice.security.SecurityUtil;
import com.example.orderservice.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ReceiverDto add(ReceiverRequest receiverRequest) throws InvalidationException {
        boolean checkName = receiverRepository.existsByName(receiverRequest.getName());
        if (!checkName) {
            throw new InvalidationException(I18nMessage.ERROR_RECEIVER_NAME_EXIST);
        }

        boolean checkPhone = receiverRepository.existsByPhoneNumber(receiverRequest.getPhoneNumber());
        if (!checkPhone) {
            throw new InvalidationException(I18nMessage.ERROR_PHONE_NUMBER_EXIST);
        }

        Optional<String> userId = SecurityUtil.getLoggedInUserId();
        if (!userId.isPresent()) {
            throw new UnauthorizedException(I18nMessage.ERROR_UNAUTHORIZED);
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
    public ReceiverDto update(String id, ReceiverRequest receiverRequest) throws NotFoundException, InvalidationException {
        ReceiverPredicate receiverPredicate = new ReceiverPredicate()
                .withId(id)
                .withStatus(true);
        Optional<Receiver> check = receiverRepository.findOne(receiverPredicate.getCriteria());
        if (check.isPresent()) {
            throw new NotFoundException(I18nMessage.ERROR_RECEIVER_NOT_FOUND);
        }

        Receiver receiver = check.get();
        boolean checkName = receiverRepository.existsByName(receiverRequest.getName());
        boolean checkPhone = receiverRepository.existsByPhoneNumber(receiverRequest.getPhoneNumber());
        if (!receiver.getName().equals(receiverRequest.getName()) && checkName) {
            throw new InvalidationException(I18nMessage.ERROR_RECEIVER_NAME_EXIST);
        }
        if (!receiver.getPhoneNumber().equals(receiverRequest.getPhoneNumber()) && checkPhone) {
            throw new InvalidationException(I18nMessage.ERROR_PHONE_NUMBER_EXIST);
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
    public ReceiverDto get(String id) throws NotFoundException {
        ReceiverPredicate receiverPredicate = new ReceiverPredicate()
                .withId(id)
                .withStatus(true);
        Optional<Receiver> check = receiverRepository.findOne(receiverPredicate.getCriteria());
        if (check.isPresent()) {
            throw new NotFoundException(I18nMessage.ERROR_RECEIVER_NOT_FOUND);
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
    public void delete(String id) throws NotFoundException {
        Receiver receiver = receiverRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(I18nMessage.ERROR_RECEIVER_NOT_FOUND);
                });

        receiver.setStatus(false);
        receiverRepository.save(receiver);
    }
}
