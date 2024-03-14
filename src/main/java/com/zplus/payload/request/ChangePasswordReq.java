package com.zplus.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordReq {

    private String oldPassword;

    private String newPassword;

    private Long id;
}
