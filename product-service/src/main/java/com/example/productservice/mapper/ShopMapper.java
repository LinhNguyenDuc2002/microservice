package com.example.productservice.mapper;

import com.example.productservice.dto.ShopDTO;
import com.example.productservice.entity.Shop;
import org.springframework.stereotype.Component;

@Component
public class ShopMapper extends AbstractMapper<Shop, ShopDTO> {
    @Override
    public Class<ShopDTO> getDtoClass() {
        return ShopDTO.class;
    }

    @Override
    public Class<Shop> getEntityClass() {
        return Shop.class;
    }
}
