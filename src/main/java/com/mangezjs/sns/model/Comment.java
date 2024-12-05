package com.mangezjs.sns.model;

import com.mangezjs.sns.model.entity.CommentEntity;
import com.mangezjs.sns.model.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;


@Getter
@AllArgsConstructor
public class Comment {
    private Integer id;
    private String comment;
    private String userName;
    private Integer postId;
    private Timestamp removedAt;
    private Timestamp registeredAt;
    private Timestamp updatedAt;

    public static Comment fromEntity(CommentEntity  entity){
        return new Comment(
                entity.getId(),
                entity.getComment(),
                entity.getUser().getUserName(),
                entity.getPost().getId(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getRemovedAt()
        );
    }

}
