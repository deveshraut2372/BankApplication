package com.zplus.payload.response;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CollectionSearchingResponse {

    private Integer collectionId;

    private Long id;

    private String userBankAccountNumber;

    private Double dailyCollectionAmount;

    private String paymentMode;

    private Date collectionDate;
}
