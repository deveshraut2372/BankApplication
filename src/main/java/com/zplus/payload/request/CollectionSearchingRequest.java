package com.zplus.payload.request;

import lombok.Data;

import java.util.Date;

@Data
public class CollectionSearchingRequest {
    private Long id;

    private Long managementMember;

    private String userBankAccountNumber;

    private String format;

    private Date date;

    private Date toDate;
}
