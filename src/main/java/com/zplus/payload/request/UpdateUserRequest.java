package com.zplus.payload.request;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateUserRequest {
    private Long id;

    private String email;

    private String userMobNo;

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

    private String address;

    private String otherAddress;

    private String villageAddress;

    private String occupation;

    private String officeAddress;

    private String panNo;

    private String aadhaarCardNo;

    private String propertyType;

    private Double cibilScore;

    private String referenceName;

    private String referenceMobile;

    private String status;
}
