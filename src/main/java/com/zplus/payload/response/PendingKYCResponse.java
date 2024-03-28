package com.zplus.payload.response;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PendingKYCResponse {
    private Long id;

    private String email;

    private Date registrationDate;

    private String fullName;

    private String userMobNo;

    private String bAccountNumber;

    private String gender;

    private Date dateOfBirth;

    private String nomineeFullName;

    private String nomineeGender;

    private String nomineeMobileNumber;

    private Date nomineeDateOfBirth;

    private String address;

    private String otherAddress;

    private String villageAddress;

    private String occupation;

    private String officeAddress;

    private String panNo;

    private String aadhaarCardNo;

    private String propertyType;

    private String referenceName;

    private String referenceMobile;

    private String aadhaarFront;

    private String aadhaarBack;

    private String panCardFront;

    private String passbookFront;

    private String photo;
}

//    id,um.fullName,um.userMobNo,um.bAccountNumber,um.ifscCode,um.gender,um.dateOfBirth,um.nomineeFullName,um.nomineeGender,um.nomineeMobileNumber,um.nomineeDateOfBirth,um.address,um.otherAddress,um.villageAddress,um.occupation,um.officeAddress,um.panNo,um.aadhaarCardNo,um.propertyType,um.referenceName,um.referenceMobile,um.aadhaarFront,um.aadhaarBack,um.panCardFront,um.passbookFront,um.photo