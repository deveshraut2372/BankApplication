package com.zplus.payload.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateBankAccountRequest {

    private Long id;

    private List<AccountTypeIdsRequest> accountTypeIds;
}
