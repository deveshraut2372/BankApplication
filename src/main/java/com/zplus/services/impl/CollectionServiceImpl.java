package com.zplus.services.impl;

import com.zplus.models.AccountTypeMaster;
import com.zplus.models.CollectionMaster;
import com.zplus.models.User;
import com.zplus.models.UserBankAccountMaster;
import com.zplus.payload.request.CollectionAmountRequest;
import com.zplus.payload.request.CollectionSearchingRequest;
import com.zplus.payload.response.CollectionResponse;
import com.zplus.payload.response.CollectionSearchingResponse;
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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
        MainResDto mainResDto = new MainResDto();
        User user = this.userRepository.findById(collectionAmountRequest.getId()).get();
        User user1 = this.userRepository.findById(collectionAmountRequest.getManagementId()).get();
        String status = user.getKycStatus();
        status = status.toLowerCase();

        if (status.equalsIgnoreCase("Accepted")){
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
                collectionMaster.setManagementMember(user1);
                collectionMaster.setDailyCollectionAmount(collectionAmountRequest.getDailyCollectionAmount());
                collectionMaster.setTotalAmount(collectionAmountRequest.getDailyCollectionAmount() + totalCollectionAmount);
                this.collectionRepository.save(collectionMaster);
                mainResDto.setMessage("daily collection amount added by "+user1.getRoles().stream().findFirst().get().getName().name());
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
        }else {
            mainResDto.setMessage("Please update your KYC First.KYC doesn't accepted");
            mainResDto.setFlag(false);
            mainResDto.setResponseCode(HttpStatus.BAD_REQUEST.value());
            return mainResDto;
        }
    }

    @Override
    public MainResDto updateDailyCollectionAmount(CollectionAmountRequest collectionAmountRequest) {
        MainResDto mainResDto = new MainResDto();
        CollectionMaster collectionMaster = this.collectionRepository.findById(collectionAmountRequest.getCollectionId()).get();
        User user = this.userRepository.findById(collectionAmountRequest.getId()).get();
        User user1 = this.userRepository.findById(collectionAmountRequest.getManagementId()).get();
        UserBankAccountMaster userBankAccountMaster = this.userBankAccountRepository.getByUserBankAccountNumber(collectionAmountRequest.getUserBankAccountNumber());
        AccountTypeMaster accountTypeMaster = this.accountTypeRepository.getByAccountTypeIdWise(userBankAccountMaster.getAccountTypeMaster().getAccountTypeId());
        Double totalCollectionAmount = this.collectionRepository.getTotalCollectionAmount(collectionAmountRequest.getId());
        System.out.println("totalCollectionAmount = "+totalCollectionAmount);
        if (totalCollectionAmount==null){
            totalCollectionAmount = 0.0;
        }

        if (collectionMaster!=null){
            collectionMaster.setUser(user);
            collectionMaster.setAccountTypeMaster(accountTypeMaster);
            collectionMaster.setUserBankAccountMaster(userBankAccountMaster);
            collectionMaster.setCollectionDate(collectionAmountRequest.getCollectionDate());
            collectionMaster.setManagementMember(user1);
            collectionMaster.setCollectionDate(new Date());
            collectionMaster.setTotalAmount(collectionAmountRequest.getDailyCollectionAmount() + totalCollectionAmount);
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
    public CollectionSearchingResponse serachCollection(CollectionSearchingRequest collectionSearchingRequest) {
        CollectionSearchingResponse collectionSearchingResponse = new CollectionSearchingResponse();
            String format = collectionSearchingRequest.getFormat();
            format = format.toLowerCase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        LocalDate date = LocalDate.now();
        System.out.println("date = "+date);
        LocalDate previousDay = date.minusDays(1);
        System.out.println("previousDay = = ="+previousDay);

            User user = this.userRepository.findById(collectionSearchingRequest.getId()).get();

            if (user!=null){
                UserBankAccountMaster userBankAccountMaster = this.userBankAccountRepository.getByUserBankAccountNumber(collectionSearchingRequest.getUserBankAccountNumber());
                if (format.equalsIgnoreCase("previousday")){
                    CollectionResponse collectionResponse  = this.collectionRepository.getPreviousDayCollection(previousDay);
                }
            }
        return collectionSearchingResponse;
    }
}
