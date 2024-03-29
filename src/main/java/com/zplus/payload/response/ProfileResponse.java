package com.zplus.payload.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfileResponse {

    private Long id;

    private String email;

    private String userMobNo;

    private String fullName;
}
