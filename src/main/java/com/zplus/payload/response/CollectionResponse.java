package com.zplus.payload.response;

import com.zplus.models.AccountTypeMaster;
import com.zplus.models.User;
import com.zplus.models.UserBankAccountMaster;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CollectionResponse {

    private Integer collectionId;

    private Double dailyCollectionAmount;

    private Integer userBankAccountId;

    private Long id;

    private Integer accountTypeId;

    private Date collectionDate;

    private Double totalAmount;

}
