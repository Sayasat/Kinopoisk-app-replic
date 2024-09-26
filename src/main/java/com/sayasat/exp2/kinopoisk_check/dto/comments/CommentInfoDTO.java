package com.sayasat.exp2.kinopoisk_check.dto.comments;

import com.sayasat.exp2.kinopoisk_check.dto.users.UserShorInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentInfoDTO {

    private UserShorInfoDTO user;
    private String comment;
    private Integer likes;
    private Integer dislikes;
    private LocalDateTime commentDate;

}
