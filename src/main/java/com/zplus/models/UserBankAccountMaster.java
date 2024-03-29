package com.zplus.models;

import lombok.Data;

import javax.persistence.*;

import java.util.Date;

@Data
@Entity
public class UserBankAccountMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userBankAccountId;

    private String userBankAccountNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accountTypeId")
    private AccountTypeMaster accountTypeMaster;

    private String status;

    private Date date;

}
