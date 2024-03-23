package com.zplus.config;

import com.zplus.repository.UserBankAccountRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

@Data
public class AccountNumberGeneration {

    @Autowired
    private UserBankAccountRepository userBankAccountRepository;

    static Random random = new Random();

    public static Integer bankAccountNumberGenerator(){
        Integer randomNumber= random.nextInt(999999999);
        Integer bankAccountNumber =randomNumber;
        return bankAccountNumber;
    }


}
