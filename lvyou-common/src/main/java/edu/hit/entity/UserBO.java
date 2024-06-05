package edu.hit.entity;


import lombok.Data;

@Data
public class UserBO {
    private Long uid;
    private String username;
    private String email;
    private String coverSrc;
    private String avatarSrc;
    private String accessToken;
}
