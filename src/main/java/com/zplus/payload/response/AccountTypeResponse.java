package com.zplus.payload.response;

import com.zplus.models.User;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountTypeResponse {
    private Integer accountTypeId;

    private String accountTypeName;

    private Date date;

    private String status;
}
