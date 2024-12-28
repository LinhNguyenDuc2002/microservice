package com.example.productservice.service;

import com.example.productservice.dto.CommentDTO;
import com.example.productservice.dto.PageDTO;
import com.example.productservice.dto.request.CommentRequest;
import com.example.productservice.exception.InvalidationException;
import com.example.productservice.exception.NotFoundException;

import java.io.IOException;
import java.util.List;

public interface CommentService {
    CommentDTO create(String id, CommentRequest commentRequest) throws Exception;

    PageDTO<CommentDTO> getAll(String id, Integer page, Integer size, List<String> sortColumns) throws NotFoundException;

    CommentDTO update(String id, CommentRequest commentRequest) throws NotFoundException, InvalidationException, IOException;

    void delete(String id) throws NotFoundException, InvalidationException;
}
