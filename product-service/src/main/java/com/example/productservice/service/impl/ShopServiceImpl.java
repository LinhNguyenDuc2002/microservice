package com.example.productservice.service.impl;

import com.example.productservice.constant.ExceptionMessage;
import com.example.productservice.constant.RoleType;
import com.example.productservice.dto.ShopDTO;
import com.example.productservice.entity.Address;
import com.example.productservice.entity.Customer;
import com.example.productservice.entity.Shop;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.mapper.ShopMapper;
import com.example.productservice.payload.AddressRequest;
import com.example.productservice.payload.ShopRequest;
import com.example.productservice.repository.AddressRepository;
import com.example.productservice.repository.CommentRepository;
import com.example.productservice.repository.CustomerRepository;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.ShopRepository;
import com.example.productservice.repository.predicate.CommentPredicate;
import com.example.productservice.repository.predicate.ProductPredicate;
import com.example.productservice.repository.predicate.ShopPredicate;
import com.example.productservice.security.SecurityUtils;
import com.example.productservice.service.ShopService;
import com.example.productservice.service.UserService;
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
    private AddressRepository addressRepository;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public ShopDTO getMyShop() throws InvalidException, NotFoundException {
        Optional<String> userId = SecurityUtils.getLoggedInUserId();
        if (userId.isEmpty()) {
            throw new InvalidException("");
        }

        ShopPredicate shopPredicate = new ShopPredicate().withAccountId(userId.get());
        Shop shop = shopRepository.findOne(shopPredicate.getCriteria())
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_SHOP_NOT_FOUND);
                });

        return shopMapper.toDto(shop);
    }

    @Override
    public ShopDTO create(String id, ShopRequest shopRequest) throws Exception {
        ShopPredicate shopPredicate = new ShopPredicate().withCustomerId(id);
        Optional<Shop> checkShop = shopRepository.findOne(shopPredicate.getCriteria());
        if (checkShop.isPresent()) {
            throw new InvalidException(ExceptionMessage.ERROR_SHOP_EXIST);
        }

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_CUSTOMER_NOT_FOUND);
                });

        AddressRequest addressRequest = shopRequest.getAddress();
        Address address = Address.builder()
                .detail(addressRequest.getDetail())
                .ward(addressRequest.getWard())
                .district(addressRequest.getDistrict())
                .city(addressRequest.getCity())
                .country(addressRequest.getCountry())
                .build();
        addressRepository.save(address);

        Shop shop = Shop.builder()
                .name(shopRequest.getName())
                .hotline(shopRequest.getHotline())
                .email(shopRequest.getEmail())
                .address(address)
                .customer(customer)
                .build();
        shopRepository.saveAndFlush(shop);

        // Assign role
        userService.assignRole(String.valueOf(RoleType.SELLER), customer.getAccountId());
        return shopMapper.toDto(shop);
    }

    @Override
    public ShopDTO get(String id) throws NotFoundException {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_SHOP_NOT_FOUND);
                });

        ProductPredicate productPredicate = new ProductPredicate().withShopId(id);
        long numberOfProducts = productRepository.count(productPredicate.getCriteria());

        CommentPredicate commentPredicate = new CommentPredicate().withShopId(id);
        long numberOfReviews = commentRepository.count(commentPredicate.getCriteria());

        ShopDTO shopDTO = shopMapper.toDto(shop);
        shopDTO.setNumberOfProducts(numberOfProducts);
        shopDTO.setNumberOfReviews(numberOfReviews);
        return shopDTO;
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

    @Override
    public void delete(String id) throws Exception {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> {
                    return new NotFoundException(ExceptionMessage.ERROR_SHOP_NOT_FOUND);
                });

        Customer customer = shop.getCustomer();
        shopRepository.deleteById(id);

        // Unassign role
        userService.unassignRole(String.valueOf(RoleType.SELLER), customer.getAccountId());
    }

    @Override
    public Boolean checkShopExist(String id) throws NotFoundException {
        boolean checkShop = shopRepository.existsById(id);
        if (!checkShop) {
            throw new NotFoundException(ExceptionMessage.ERROR_SHOP_NOT_FOUND);
        }

        return true;
    }
}
