package com.example.productservice.mock;

import com.example.productservice.entity.Comment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMock {
    public static Comment mockComment(boolean allowEdit) {
        return Comment.builder()
                .message("good")
                .allowEdit(allowEdit)
                .build();
    }
}
