package com.example.productservice.controller;

import com.example.productservice.constant.I18nMessage;
import com.example.productservice.constant.ParameterConstant;
import com.example.productservice.dto.CommentDTO;
import com.example.productservice.dto.PageDTO;
import com.example.productservice.dto.request.CommentRequest;
import com.example.productservice.dto.response.Response;
import com.example.productservice.exception.InvalidationException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.i18n.I18nService;
import com.example.productservice.service.CommentService;
import com.example.productservice.util.ResponseUtil;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private I18nService i18nService;

    @PostMapping("/order-detail/{id}")
    public ResponseEntity<Response<CommentDTO>> create(
            @PathVariable String id,
            @Valid @RequestBody CommentRequest commentRequest) throws Exception {
        return ResponseUtil.wrapResponse(
                commentService.create(id, commentRequest),
                i18nService.getMessage(I18nMessage.INFO_CREATE_COMMENT, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Response<PageDTO<CommentDTO>>> getAll(
            @PathVariable String id,
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page,
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size,
            @RequestParam(name = "sort-columns") List<String> sortColumns) throws Exception {
        return ResponseUtil.wrapResponse(
                commentService.getAll(id, page, size, sortColumns),
                i18nService.getMessage(I18nMessage.INFO_GET_COMMENT, LocaleContextHolder.getLocale())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<PageDTO<CommentDTO>>> getAllChild(@PathVariable String id,
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page,
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size,
            @RequestParam(name = "sort-columns") List<String> sortColumns) throws Exception {
        return ResponseUtil.wrapResponse(
                commentService.getAllChild(id, page, size, sortColumns),
                i18nService.getMessage(I18nMessage.INFO_GET_COMMENT, LocaleContextHolder.getLocale())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<CommentDTO>> update(
            @PathVariable String id,
            @Valid @RequestBody CommentRequest commentRequest) throws NotFoundException, InvalidationException, IOException {
        return ResponseUtil.wrapResponse(
                commentService.update(id, commentRequest),
                i18nService.getMessage(I18nMessage.INFO_UPDATE_COMMENT, LocaleContextHolder.getLocale())
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> delete(@PathVariable String id) throws NotFoundException, InvalidationException, IOException {
        commentService.delete(id);
        return ResponseUtil.wrapResponse(
                i18nService.getMessage(I18nMessage.INFO_DELETE_COMMENT, LocaleContextHolder.getLocale())
        );
    }
}
