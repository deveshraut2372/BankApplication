package com.zplus.payload.response;

import com.zplus.models.AccountTypeMaster;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegistrationResponse {
//    private Long id;
//
//    private String email;
//
//    private String userMobNo;
//
//    private String role;
//
//    private String password;
//
//    private String fullName;
//
//    private String status;
//
//    private Date registrationDate;

    private String message;

    private Integer responseCode;

    private Boolean flag;

//    private String kycStatus;
//
//    private List<AccountTypeMaster> accountTypeMasterList;

//    public RegistrationResponse(Long id, String email, String userMobNo, String role, String password, String fullName, String photo, String status, Date registrationDate, String message, Integer responseCode, Boolean flag, String kycStatus) {
//        this.id = id;
//        this.email = email;
//        this.userMobNo = userMobNo;
//        this.role = role;
//        this.password = password;
//        this.fullName = fullName;
//        this.status = status;
//        this.registrationDate = registrationDate;
//        this.message = message;
//        this.responseCode = responseCode;
//        this.flag = flag;
//        this.kycStatus = kycStatus;
//    }
}
