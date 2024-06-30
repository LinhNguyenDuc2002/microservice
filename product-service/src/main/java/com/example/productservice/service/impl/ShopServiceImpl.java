package com.example.productservice.service.impl;

import com.example.productservice.constant.ExceptionMessage;
import com.example.productservice.dto.ShopDTO;
import com.example.productservice.entity.Address;
import com.example.productservice.entity.Customer;
import com.example.productservice.entity.Shop;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mapper.ShopMapper;
import com.example.productservice.payload.AddressRequest;
import com.example.productservice.payload.ShopRequest;
import com.example.productservice.repository.CustomerRepository;
import com.example.productservice.repository.ShopRepository;
import com.example.productservice.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopMapper shopMapper;
    @Override
    public ShopDTO create(String id, ShopRequest shopRequest) throws InvalidException, NotFoundException {
        Optional<Customer> check = customerRepository.findById(id);

        if (!check.isPresent()) {
            throw new NotFoundException(ExceptionMessage.ERROR_CUSTOMER_NOT_FOUND);
        }

        Customer customer = check.get();
        Shop shop = Shop.builder()
                .name(shopRequest.getName())
                .hotline(shopRequest.getHotline())
                .email(shopRequest.getEmail())
                .address(Address.builder().build())
                .customer(customer)
                .build();

        shopRepository.save(shop);
        return shopMapper.toDto(shop);
    }

    @Override
    public ShopDTO get(String id) throws NotFoundException {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_SHOP_NOT_FOUND);
                });

        return shopMapper.toDto(shop);
    }

    @Override
    public ShopDTO update(String id, ShopRequest shopRequest) throws NotFoundException {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_SHOP_NOT_FOUND);
                });

        shop.setName(shopRequest.getName());
        shop.setEmail(shopRequest.getEmail());
        shop.setHotline(shopRequest.getHotline());

        Address address = shop.getAddress();
        AddressRequest addressRequest = shopRequest.getAddress();
        address.setDetail(addressRequest.getDetail());
        address.setWard(addressRequest.getWard());
        address.setDistrict(addressRequest.getDistrict());
        address.setCity(addressRequest.getCity());
        address.setCountry(addressRequest.getCountry());

        return shopMapper.toDto(shopRepository.save(shop));
    }
}
