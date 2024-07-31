package com.example.orderservice.service.impl;

import com.example.orderservice.constant.GeneralConstant;
import com.example.orderservice.service.HouseKeepingService;
import org.springframework.stereotype.Service;

@Service
public class HouseKeepingServiceImpl implements HouseKeepingService {
    @Override
    public void resetBillCode() {
        GeneralConstant.billCode = 1;
    }
}
