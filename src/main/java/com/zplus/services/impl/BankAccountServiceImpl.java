package com.zplus.services.impl;

import com.zplus.models.AccountTypeMaster;
import com.zplus.models.UserBankAccountMaster;
import com.zplus.payload.request.AccountTypeIdsRequest;
import com.zplus.payload.request.CreateBankAccountRequest;
import com.zplus.repository.UserBankAccountRepository;
import com.zplus.services.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    private UserBankAccountRepository userBankAccountRepository;


    @Override
    public List<UserBankAccountMaster> getAllBankAccountsById(Long id) {
        List<UserBankAccountMaster> userBankAccountMaster = this.userBankAccountRepository.getAllBankAccountsById(id);
        return userBankAccountMaster;
    }

    @Override
    public Boolean create(CreateBankAccountRequest createBankAccountRequest) {
        Boolean flag = false;

        List<UserBankAccountMaster> userBankAccountMasters = this.userBankAccountRepository.getAllBankAccountsById(createBankAccountRequest.getId());

        UserBankAccountMaster bankAccountMaster = this.userBankAccountRepository.getByUserAccountTypeIdAndIdWise(createBankAccountRequest.getAccountTypeIds().stream().findFirst().get().getAccountTypeId(),createBankAccountRequest.getId());

        if (bankAccountMaster!=null){
            System.out.println("Already have an account");
            flag = false;
        }else {
            System.out.println("Create an account");
            flag = true;
        }


        for (UserBankAccountMaster userBankAccountMaster : userBankAccountMasters) {
            System.out.println("userBankAccountMaster = "+userBankAccountMaster);
        }
        return flag;
    }
}