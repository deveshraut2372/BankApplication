package com.zplus.config;


import lombok.Data;

import java.util.Random;

@Data
public class PasswordGeneration {

    public static Integer passwordGeneration(){
        Random random = new Random();
        Integer password = random.nextInt(9999);
        return password;
    }
}
