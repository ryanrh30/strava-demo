package com.uct.stravademo.OAuth;

import lombok.Data;

@Data
public class StravaTokenResponse {
    private String access_token;
    private String token_type;
    private Integer expires_at;
    private Integer expires_in;
    private String refresh_token;
}
