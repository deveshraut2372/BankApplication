package com.zplus.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRes {

    private Long id;

    private String email;

    private String password;

    private String userMobNo;

    private String address;

    private String status;

    private String fullName;

    private Integer age;

    private String gender;

    private Date dateOfBirth;

    private String nomineeFullName;

    private Integer nomineeAge;

    private String nomineeGender;

    private String nomineeMobileNumber;

    private Date nomineeDateOfBirth;

    private String photo;

    private String otherAddress;

    private String villageAddress;

    private String occupation;

    private String officeAddress;

    private String panNo;

    private String aadhaarCardNo;

    private String propertyType;

    private String referenceName;

    private String referenceMobile;

    private Date registrationDate;

}

//um.fullName,um.age,um.gender,um.dateOfBirth,um.nomineeFullName,um.nomineeAge,um.nomineeGender,um.nomineeMobileNumber,um.nomineeDateOfBirth,um.photo,um.otherAddress,um.villageAddress,um.occupation,um.officeAddress,um.panNo,um.aadhaarCardNo,um.propertyType,um.cibilScore,um.referenceName,um.referenceMobile

