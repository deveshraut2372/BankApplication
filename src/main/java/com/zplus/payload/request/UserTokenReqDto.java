package com.zplus.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class UserTokenReqDto {

    private Long id;

    private String token;

    public UserTokenReqDto(Long id, String token) {
        this.id = id;
        this.token = token;
    }
}
