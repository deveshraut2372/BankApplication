package com.zplus.services;

import com.zplus.payload.request.AccountTypeRequest;
import com.zplus.payload.response.AccountTypeResponse;

import java.util.List;

public interface AccountTypeService {
    AccountTypeResponse create(AccountTypeRequest accountTypeRequest);

    AccountTypeResponse update(AccountTypeRequest accountTypeRequest);

    AccountTypeResponse getById(Integer accountTypeId);

    List<AccountTypeResponse> getAll();

    List<AccountTypeResponse> getAllActive();
}
