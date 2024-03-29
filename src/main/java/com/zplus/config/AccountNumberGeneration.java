package com.zplus.config;

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

    static Random random = new Random();

    public  String bankAccountNumberGenerator(){

        Boolean flag=false;
        String bankAccountNumber="";
        do
        {
            Integer randomNumber= random.nextInt(9999999);
            Integer secondNumber =random.nextInt(9999999);
           bankAccountNumber=String.valueOf(randomNumber)+secondNumber;
            System.out.println("  bank ="+bankAccountNumber);
            UserBankAccountMaster userBankAccountMaster=new UserBankAccountMaster();
           Integer count=0;

            List<UserBankAccountMaster> bankAccountMaster = this.userBankAccountRepository.findAll();
            System.out.println("HIIII  = "+bankAccountMaster);

           if (userBankAccountRepository.existsByUserBankAccountNumber(bankAccountNumber)){
               System.out.println("Hi");
           }else {
               System.out.println("hi2");
           }

//           userBankAccountMaster1.ifPresent( userBankAccountMaster2 -> BeanUtils.copyProperties(userBankAccountMaster2,userBankAccountMaster));

            System.out.println("  bank ="+bankAccountNumber);
//           Integer count =0;
//           count=userBankAccountRepository.getcountByUserBankAccountNumber(bankAccountNumber);
            System.out.println("  bank1 ="+bankAccountNumber);

            if(count==0 && bankAccountNumber.length()==14)
            {
                System.out.println("  ban ="+bankAccountNumber);
                return bankAccountNumber;
            }else {
                flag=true;
            }
        }while(flag);
        return bankAccountNumber;
    }


}
