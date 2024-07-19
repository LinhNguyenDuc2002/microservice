package com.example.orderservice.mapper;

import com.example.orderservice.dto.BillDTO;
import com.example.orderservice.entity.Bill;
import com.example.orderservice.util.StringFormatUtil;
import org.springframework.stereotype.Component;

@Component
public class BillMapper extends AbstractMapper<Bill, BillDTO> {
    @Override
    public BillDTO toDto(Bill bill) {
        BillDTO billDTO = super.toDto(bill);
        billDTO.setBillId(StringFormatUtil.formatBillId("B-", bill.getBillId()));
        return billDTO;
    }
    @Override
    public Class<BillDTO> getDtoClass() {
        return BillDTO.class;
    }

    @Override
    public Class<Bill> getEntityClass() {
        return Bill.class;
    }
}
