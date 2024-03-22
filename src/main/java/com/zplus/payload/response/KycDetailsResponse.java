package com.zplus.payload.response;

import lombok.*;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KycDetailsResponse {
    private Long id;

    private String fullName;

    private String gender;

    private Date dateOfBirth;

    private String nomineeFullName;

    private Integer nomineeAge;

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

    private String kycStatus;

    private Date kycDate;

    private Date kycAcceptedDate;

    private String kycRejectReason;

    private Date kycRejectDate;

    private String photo;
}

//u.id,u.gender,u.dateOfBirth,u.nomineeFullName,u.nomineeAge,u.nomineeGender,u.nomineeMobileNumber,u.nomineeDateOfBirth,u.address,u.otherAddress,u.villageAddress,u.occupation,u.officeAddress,u.panNo,u.aadhaarCardNo,u.propertyType,u.cibilScore,u.referenceName,u.referenceMobile,u.aadhaarFront,u.aadhaarBack,u.panCardFront,u.passbookFront,u.kycStatus,u.Date,u.kycDate,u.kycAcceptedDate,u.kycRejectReason,u.kycRejectDate