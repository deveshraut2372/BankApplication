package com.zplus.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class CollectionMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer collectionId;

    private Double dailyCollectionAmount;

    @ManyToOne
    @JoinColumn(name = "userBankAccountId")
    private UserBankAccountMaster userBankAccountMaster;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "mid")
    private User managementMember;

    @ManyToOne
    @JoinColumn(name = "accountTypeId")
    private AccountTypeMaster accountTypeMaster;

    @Temporal(TemporalType.DATE)
    private Date collectionDate;

    private Double totalAmount;

    private String paymentMode;

}
