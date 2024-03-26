package com.zplus.services.impl;

import com.zplus.models.AccountTypeMaster;
import com.zplus.models.CollectionMaster;
import com.zplus.models.User;
import com.zplus.models.UserBankAccountMaster;
import com.zplus.payload.request.CollectionAmountRequest;
import com.zplus.payload.response.CollectionResponse;
import com.zplus.payload.response.MainResDto;
import com.zplus.repository.AccountTypeRepository;
import com.zplus.repository.CollectionRepository;
import com.zplus.repository.UserBankAccountRepository;
import com.zplus.repository.UserRepository;
import com.zplus.services.CollectionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBankAccountRepository userBankAccountRepository;

    @Autowired
    private AccountTypeRepository accountTypeRepository;

    @Override
    public MainResDto addDailyCollection(CollectionAmountRequest collectionAmountRequest) {
        User user = this.userRepository.findById(collectionAmountRequest.getId()).get();
        MainResDto mainResDto = new MainResDto();
        UserBankAccountMaster userBankAccountMaster = this.userBankAccountRepository.getByUserBankAccountNumber(collectionAmountRequest.getUserBankAccountNumber());
        AccountTypeMaster accountTypeMaster = accountTypeRepository.findById(userBankAccountMaster.getAccountTypeMaster().getAccountTypeId()).get();
        Double totalCollectionAmount = this.collectionRepository.getTotalCollectionAmount(collectionAmountRequest.getId());
        System.out.println("totalCollectionAmount = "+totalCollectionAmount);
        if (totalCollectionAmount==null){
            totalCollectionAmount = 0.0;
        }
        CollectionMaster collectionMaster = new CollectionMaster();
        BeanUtils.copyProperties(collectionAmountRequest,collectionMaster);
        try {
            collectionMaster.setUser(user);
            collectionMaster.setUserBankAccountMaster(userBankAccountMaster);
            collectionMaster.setAccountTypeMaster(accountTypeMaster);
            collectionMaster.setCollectionDate(new Date());
            collectionMaster.setDailyCollectionAmount(collectionAmountRequest.getDailyCollectionAmount());
            collectionMaster.setTotalAmount(collectionAmountRequest.getDailyCollectionAmount() + totalCollectionAmount);
            this.collectionRepository.save(collectionMaster);
            mainResDto.setMessage("daily collection amount added");
            mainResDto.setFlag(true);
            mainResDto.setResponseCode(HttpStatus.OK.value());
            return mainResDto;
        }catch (Exception e){
            e.printStackTrace();
            mainResDto.setMessage("daily collection amount not added");
            mainResDto.setFlag(true);
            mainResDto.setResponseCode(HttpStatus.OK.value());
            return mainResDto;
        }
    }

    @Override
    public MainResDto updateDailyCollectionAmount(CollectionAmountRequest collectionAmountRequest) {
        MainResDto mainResDto = new MainResDto();
        CollectionMaster collectionMaster = this.collectionRepository.findById(collectionAmountRequest.getCollectionId()).get();
        User user = this.userRepository.findById(collectionAmountRequest.getId()).get();
        UserBankAccountMaster userBankAccountMaster = this.userBankAccountRepository.getByUserBankAccountNumber(collectionAmountRequest.getUserBankAccountNumber());
        AccountTypeMaster accountTypeMaster = this.accountTypeRepository.getByAccountTypeIdWise(userBankAccountMaster.getAccountTypeMaster().getAccountTypeId());

        if (collectionMaster!=null){
            collectionMaster.setUser(user);
            collectionMaster.setAccountTypeMaster(accountTypeMaster);
            collectionMaster.setUserBankAccountMaster(userBankAccountMaster);
            collectionMaster.setCollectionDate(collectionAmountRequest.getCollectionDate());
            BeanUtils.copyProperties(collectionAmountRequest,collectionMaster);
            this.collectionRepository.save(collectionMaster);

            mainResDto.setMessage("Collection updated successfully");
            mainResDto.setResponseCode(HttpStatus.OK.value());
            mainResDto.setFlag(true);
            return mainResDto;
        }else {
            mainResDto.setMessage("Collection didn't update");
            mainResDto.setResponseCode(HttpStatus.BAD_REQUEST.value());
            mainResDto.setFlag(false);
            return mainResDto;
        }
    }

    @Override
    public List<CollectionResponse> getAllCollection() {
        return null;
    }
}
