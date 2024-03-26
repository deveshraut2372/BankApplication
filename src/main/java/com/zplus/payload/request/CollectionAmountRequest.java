package com.zplus.payload.request;

import lombok.Data;

import java.util.Date;

@Data
public class CollectionAmountRequest {

    private Integer collectionId;

    private Long id;

    private String userBankAccountNumber;

    private Double dailyCollectionAmount;

    private String paymentMode;

    private Date collectionDate;

}
