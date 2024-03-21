package com.zplus.payload.response;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AcceptKYCResponse {

    private Long id;

    private String kycStatus;

    private Date kycAcceptedDate;

}
