package com.zplus.payload.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OtpVerificationResponse {

    private Long id;

    private String message;

    private Integer responseCode;

    private Boolean flag;
}
