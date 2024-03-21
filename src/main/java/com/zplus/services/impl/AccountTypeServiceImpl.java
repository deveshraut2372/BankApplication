package com.zplus.services.impl;

import com.zplus.models.AccountTypeMaster;
import com.zplus.payload.request.AccountTypeRequest;
import com.zplus.payload.response.AccountTypeResponse;
import com.zplus.repository.AccountTypeRepository;
import com.zplus.services.AccountTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AccountTypeServiceImpl implements AccountTypeService {
    @Autowired
    private AccountTypeRepository  accountTypeRepository;

    @Override
    public AccountTypeResponse create(AccountTypeRequest accountTypeRequest) {
        AccountTypeResponse accountTypeResponse = new AccountTypeResponse();
        AccountTypeMaster accountTypeMaster = new AccountTypeMaster();
        BeanUtils.copyProperties(accountTypeRequest,accountTypeMaster);
        try {
            accountTypeMaster.setDate(new Date());
            AccountTypeMaster accountTypeMaster1 = this.accountTypeRepository.save(accountTypeMaster);
            BeanUtils.copyProperties(accountTypeMaster1,accountTypeResponse);
            return accountTypeResponse;
        }catch (Exception e){
            e.printStackTrace();
            return accountTypeResponse;
        }
    }

    @Override
    public AccountTypeResponse update(AccountTypeRequest accountTypeRequest) {
        AccountTypeResponse accountTypeResponse = new AccountTypeResponse();
        AccountTypeMaster accountTypeMaster = this.accountTypeRepository.findById(accountTypeRequest.getAccountTypeId()).get();
        BeanUtils.copyProperties(accountTypeRequest,accountTypeMaster);
        try {
            AccountTypeMaster accountTypeMaster1 = this.accountTypeRepository.save(accountTypeMaster);
            BeanUtils.copyProperties(accountTypeMaster1,accountTypeResponse);
            return accountTypeResponse;
        }catch (Exception e){
            e.printStackTrace();
            return accountTypeResponse;
        }
    }

    @Override
    public AccountTypeResponse getById(Integer accountTypeId) {
        AccountTypeResponse accountTypeResponse = this.accountTypeRepository.getByAccountTypeId(accountTypeId);

        if (accountTypeResponse!=null){
            return accountTypeResponse;
        }else {
            return new AccountTypeResponse(null,null,null,null);
        }
    }

    @Override
    public List<AccountTypeResponse> getAll() {
        List<AccountTypeResponse> accountTypeResponses = this.accountTypeRepository.getAll();
        return accountTypeResponses;
    }

    @Override
    public List<AccountTypeResponse> getAllActive() {
        List<AccountTypeResponse> accountTypeResponses = this.accountTypeRepository.getAllActive();
        return accountTypeResponses;
    }
}
