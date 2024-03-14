package com.zplus.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRes {

    private Long id;

    private String username;

    private String email;

    private String password;

    private String userMobNo;

    private String address;

    private String status;

//    private Set<Role> roles = new HashSet<>();



}


