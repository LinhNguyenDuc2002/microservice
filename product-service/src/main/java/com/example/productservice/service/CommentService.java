package com.example.productservice.service;

import com.example.productservice.dto.CommentDTO;
import com.example.productservice.dto.MyCommentDTO;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.CommentRequest;
import com.example.productservice.dto.PageDTO;

import java.util.List;

public interface CommentService {
    List<MyCommentDTO> getMyComment() throws InvalidException;

    CommentDTO create(String id, CommentRequest commentRequest) throws Exception;

    PageDTO<CommentDTO> getAll(String id, Integer page, Integer size, List<String> sortColumns) throws NotFoundException;

    CommentDTO update(String id, CommentRequest commentRequest) throws NotFoundException, InvalidException;

    void delete(String id) throws NotFoundException, InvalidException;
}
