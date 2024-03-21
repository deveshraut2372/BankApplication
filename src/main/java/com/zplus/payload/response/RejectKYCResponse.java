package com.zplus.payload.response;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RejectKYCResponse {
    private Long id;

    private String kycRejectReason;

    private String kycStatus;

    private Date kycRejectDate;
}
