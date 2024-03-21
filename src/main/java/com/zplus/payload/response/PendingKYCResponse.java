package com.zplus.payload.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PendingKYCResponse {
    private Long id;

    private String kycStatus;
}
