package com.zplus.payload.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApplyingKYCResponse {

    private Long id;

    private String kycStatus;
}
