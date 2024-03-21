package com.zplus.payload.request;

import lombok.Data;
@Data
public class AccountTypeRequest {
    private Integer accountTypeId;

    private String accountTypeName;

    private String status;
}
