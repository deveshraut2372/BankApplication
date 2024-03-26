package com.zplus.services;

import com.zplus.models.UserBankAccountMaster;
import com.zplus.payload.request.CreateBankAccountRequest;

import java.util.List;

public interface BankAccountService {

    List<UserBankAccountMaster> getAllBankAccountsById(Long id);

    Boolean create(CreateBankAccountRequest createBankAccountRequest);
}
