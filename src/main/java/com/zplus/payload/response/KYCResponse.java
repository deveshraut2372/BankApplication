package com.zplus.payload.response;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KYCResponse {
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

    private Date kycDate;

    private String aadhaarFront;

    private String aadhaarBack;

    private String panCardFront;

    private String passbookFront;

    private String photo;

    private String message;

    private Integer responseCode;

    private Boolean flag;

    public KYCResponse(Long id,String fullName, String gender, Date dateOfBirth, String nomineeFullName, Integer nomineeAge, String nomineeGender, String nomineeMobileNumber, Date nomineeDateOfBirth, String address, String otherAddress, String villageAddress, String occupation, String officeAddress, String panNo, String aadhaarCardNo, String propertyType, String referenceName, String referenceMobile, Date kycDate, String aadhaarFront, String aadhaarBack, String panCardFront, String passbookFront,String photo) {
        this.id = id;
        this.fullName = fullName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.nomineeFullName = nomineeFullName;
        this.nomineeAge = nomineeAge;
        this.nomineeGender = nomineeGender;
        this.nomineeMobileNumber = nomineeMobileNumber;
        this.nomineeDateOfBirth = nomineeDateOfBirth;
        this.address = address;
        this.otherAddress = otherAddress;
        this.villageAddress = villageAddress;
        this.occupation = occupation;
        this.officeAddress = officeAddress;
        this.panNo = panNo;
        this.aadhaarCardNo = aadhaarCardNo;
        this.propertyType = propertyType;
        this.referenceName = referenceName;
        this.referenceMobile = referenceMobile;
        this.kycDate = kycDate;
        this.aadhaarFront = aadhaarFront;
        this.aadhaarBack = aadhaarBack;
        this.panCardFront = panCardFront;
        this.passbookFront = passbookFront;
        this.photo = photo;
    }
}


//u.id,u.gender,u.dateOfBirth,u.nomineeFullName,u.nomineeAge,u.nomineeGender,u.nomineeMobileNumber,u.nomineeDateOfBirth,u.address,u.otherAddress,u.villageAddress,u.occupation,u.officeAddress,u.panNo,u.aadhaarCardNo,u.propertyType,u.cibilScore,u.referenceName,u.referenceMobile,u.kycDate,u.aadhaarFront,u.aadhaarBack,u.panCardFront,u.passbookFront,u.message,u.responseCode,u.flag