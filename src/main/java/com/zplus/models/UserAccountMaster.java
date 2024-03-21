package com.zplus.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "user_Account_Master")
public class UserAccountMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer  userAccountId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private  User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountTypeId")
    private AccountTypeMaster accountTypeMaster;

}
