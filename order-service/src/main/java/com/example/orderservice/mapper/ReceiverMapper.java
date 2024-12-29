package com.example.orderservice.mapper;

import com.example.orderservice.dto.ReceiverDto;
import com.example.orderservice.entity.Receiver;
import org.springframework.stereotype.Component;

@Component
public class ReceiverMapper extends AbstractMapper<Receiver, ReceiverDto> {
    @Override
    public Class<ReceiverDto> getDtoClass() {
        return ReceiverDto.class;
    }

    @Override
    public Class<Receiver> getEntityClass() {
        return Receiver.class;
    }
}
