package com.example.orderservice.controller;

import com.example.orderservice.constant.I18nMessage;
import com.example.orderservice.dto.ReceiverDto;
import com.example.orderservice.dto.request.ReceiverRequest;
import com.example.orderservice.dto.response.Response;
import com.example.orderservice.exception.InvalidationException;
import com.example.orderservice.exception.NotFoundException;
import com.example.orderservice.i18n.I18nService;
import com.example.orderservice.service.ReceiverService;
import com.example.orderservice.util.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/receiver")
public class ReceiverController {
    @Autowired
    private ReceiverService receiverService;

    @Autowired
    private I18nService i18nService;

    @PostMapping
    public ResponseEntity<Response<ReceiverDto>> add(@Valid @RequestBody ReceiverRequest receiverRequest) throws InvalidationException {
        return ResponseUtil.wrapResponse(
                receiverService.add(receiverRequest),
                i18nService.getMessage(I18nMessage.INFO_ADD_RECEIVER, LocaleContextHolder.getLocale())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<ReceiverDto>> update(
            @PathVariable String id,
            @Valid @RequestBody ReceiverRequest receiverRequest) throws InvalidationException, NotFoundException {
        return ResponseUtil.wrapResponse(
                receiverService.update(id, receiverRequest),
                i18nService.getMessage(I18nMessage.INFO_UPDATE_RECEIVER, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<ReceiverDto>> get(@PathVariable String id) throws InvalidationException, NotFoundException {
        return ResponseUtil.wrapResponse(
                receiverService.get(id),
                i18nService.getMessage(I18nMessage.INFO_GET_RECEIVER, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<Response<List<ReceiverDto>>> getAll(@PathVariable String id) throws InvalidationException, NotFoundException {
        return ResponseUtil.wrapResponse(
                receiverService.getAll(id),
                i18nService.getMessage(I18nMessage.INFO_GET_RECEIVER, LocaleContextHolder.getLocale())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable String id) throws InvalidationException, NotFoundException {
        receiverService.delete(id);
        return ResponseUtil.wrapResponse(
                i18nService.getMessage(I18nMessage.INFO_DELETE_RECEIVER, LocaleContextHolder.getLocale())
        );
    }
}
