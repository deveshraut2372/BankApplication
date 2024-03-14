package com.zplus.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
 @Data
 @AllArgsConstructor
 @NoArgsConstructor
public class SignupRequest {

     private Long id;

    private String email;

    private String userMobNo;

    private String role;

    private String password;

     private String status;

 }

