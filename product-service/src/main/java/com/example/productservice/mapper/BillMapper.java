package com.example.productservice.mapper;

import com.example.productservice.dto.BillDTO;
import com.example.productservice.entity.Bill;
import org.springframework.stereotype.Component;

@Component
public class BillMapper extends AbstractMapper<Bill, BillDTO> {
    @Override
    public Class<BillDTO> getDtoClass() {
        return BillDTO.class;
    }

    @Override
    public Class<Bill> getEntityClass() {
        return Bill.class;
    }
}
