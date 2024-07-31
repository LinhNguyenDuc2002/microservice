package com.example.productservice.controller;

import com.example.productservice.constant.ParameterConstant;
import com.example.productservice.constant.ResponseMessage;
import com.example.productservice.dto.CommentDTO;
import com.example.productservice.dto.MyCommentDTO;
import com.example.productservice.exception.InvalidException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.payload.CommentRequest;
import com.example.productservice.payload.response.CommonResponse;
import com.example.productservice.dto.PageDTO;
import com.example.productservice.service.CommentService;
import com.example.productservice.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/comment/detail/{id}")
    public ResponseEntity<CommonResponse<CommentDTO>> create(
            @PathVariable String id,
            @RequestBody CommentRequest commentRequest) throws Exception {
        return ResponseUtil.wrapResponse(commentService.create(id, commentRequest), ResponseMessage.CREATE_COMMENT_SUCCESS);
    }

    @GetMapping("/comment/product/{id}")
    public ResponseEntity<CommonResponse<PageDTO<CommentDTO>>> getAll(
            @PathVariable String id,
            @RequestParam(name = ParameterConstant.Page.PAGE, defaultValue = ParameterConstant.Page.DEFAULT_PAGE) Integer page,
            @RequestParam(name = ParameterConstant.Page.SIZE, defaultValue = ParameterConstant.Page.DEFAULT_SIZE) Integer size,
            @RequestParam(name = "sort-columns") List<String> sortColumns) throws NotFoundException {
        return ResponseUtil.wrapResponse(commentService.getAll(id, page, size, sortColumns), "");
    }

    @GetMapping("/my-comment")
    public ResponseEntity<CommonResponse<List<MyCommentDTO>>> getMyComment() throws InvalidException {
        return ResponseUtil.wrapResponse(commentService.getMyComment(), ResponseMessage.GET_COMMENT_SUCCESS);
    }

    @PutMapping("/comment/{id}")
    public ResponseEntity<CommonResponse<CommentDTO>> update(
            @PathVariable String id,
            @RequestBody CommentRequest commentRequest) throws NotFoundException, InvalidException {
        return ResponseUtil.wrapResponse(commentService.update(id, commentRequest), ResponseMessage.UPDATE_COMMENT_SUCCESS);
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable String id) throws NotFoundException, InvalidException {
        commentService.delete(id);
        return ResponseUtil.wrapResponse(null, ResponseMessage.DELETE_COMMENT_SUCCESS);
    }
}
