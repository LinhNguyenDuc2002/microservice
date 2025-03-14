package com.example.productservice.service;

import com.example.productservice.dto.CommentDTO;
import com.example.productservice.dto.PageDTO;
import com.example.productservice.dto.request.CommentRequest;
import com.example.servicefoundation.exception.I18nException;

import java.io.IOException;
import java.util.List;

public interface CommentService {
    CommentDTO create(String id, CommentRequest commentRequest) throws Exception;

    PageDTO<CommentDTO> getAll(String id, Integer page, Integer size, List<String> sortColumns) throws Exception;

    PageDTO<CommentDTO> getAllChild(String id, Integer page, Integer size, List<String> sortColumns) throws Exception;

    CommentDTO update(String id, CommentRequest commentRequest) throws IOException, I18nException;

    void delete(String id) throws IOException, I18nException;
}
