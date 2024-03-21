package com.zplus.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
 @AllArgsConstructor
 @NoArgsConstructor
public class SignupRequest {

    private Long id;

    private List<AccountTypeIdsRequest> accountTypeIds;

    private String email;

    private String userMobNo;

    private String role;

    private String fullName;

    private String status;
 }

