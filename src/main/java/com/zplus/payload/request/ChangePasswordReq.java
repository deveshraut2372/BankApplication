package com.zplus.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordReq {
    private Long id;

    private String newPassword;

    private String confirmPassword;
}
