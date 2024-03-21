package com.zplus.payload.request;

import lombok.Data;

@Data
public class RejectKYCRequest {

    private Long id;

    private String kycRejectReason;


}
