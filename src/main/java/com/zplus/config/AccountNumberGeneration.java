package com.zplus.config;

import com.stripe.util.StringUtils;
import com.zplus.models.UserBankAccountMaster;
import com.zplus.repository.UserBankAccountRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Random;

@Data
public class AccountNumberGeneration {

    @Autowired
    private  UserBankAccountRepository userBankAccountRepository;


    public  String bankAccountNumberGenerator(){

        Boolean flag=false;
        String bankAccountNumber="";
        do
        {
            Random random=new Random();
            Integer randomNumber= random.nextInt(9999999);
            Integer secondNumber =random.nextInt(9999999);
           bankAccountNumber=String.valueOf(randomNumber)+secondNumber;
            System.out.println("  bank ="+bankAccountNumber);

            // check condition for bankaccount number exit or not on if condition
            if(bankAccountNumber.length()==14)
            {
                return bankAccountNumber;
            }else {
                flag=true;
            }

        }while(flag);
        return bankAccountNumber;
    }


}
