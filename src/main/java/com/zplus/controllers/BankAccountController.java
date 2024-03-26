package com.zplus.controllers;

import com.zplus.models.UserBankAccountMaster;
import com.zplus.payload.request.CreateBankAccountRequest;
import com.zplus.services.impl.BankAccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bankaccount")
@CrossOrigin(origins = "*")
public class BankAccountController {

    @Autowired
    private BankAccountServiceImpl bankAccountService;

    @GetMapping("/getallbankaccountsbyid/{id}")
    public ResponseEntity getAllBankAccountById(@PathVariable("id") Long id){
        List<UserBankAccountMaster> userBankAccountMasters = this.bankAccountService.getAllBankAccountsById(id);

        if (userBankAccountMasters!=null){
            return new ResponseEntity(userBankAccountMasters, HttpStatus.OK);
        }else {
            return new ResponseEntity(userBankAccountMasters, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createBankAccount")
    public ResponseEntity createBankAccount(@RequestBody CreateBankAccountRequest createBankAccountRequest){
        Boolean flag = this.bankAccountService.create(createBankAccountRequest);

        if (Boolean.TRUE.equals(flag)){
            return new ResponseEntity<>(flag, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(flag, HttpStatus.BAD_REQUEST);
        }
    }
}
