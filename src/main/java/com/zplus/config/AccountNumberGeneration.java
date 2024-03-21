package com.zplus.config;

import lombok.Data;

import java.util.Random;

@Data
public class AccountNumberGeneration {

    static Random random = new Random();

    public static Integer pigmyAccountNumberGeneration(){
        Integer randomNumber= random.nextInt(999999999);
        Integer pigmy =randomNumber;
        return pigmy;
    }

    public static Integer fixedDepositAccountNumberGeneration(){
        Integer randomNumber= random.nextInt(999999999);
        Integer fixed = randomNumber;
        return fixed;
    }

    public static Integer recurringDepositAccount(){
        Integer randomNumber= random.nextInt(99999999);
        Integer recurring = randomNumber;
        return recurring;
    }


}
