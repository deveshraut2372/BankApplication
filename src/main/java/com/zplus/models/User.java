package com.zplus.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Data
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(	name = "users", 
		uniqueConstraints = { 
						@UniqueConstraint(columnNames = "email")
		})
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Long managementMember;

	@NotBlank
	@Size(max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;

	@Column
	private String userMobNo;

	private String bAccountNumber;

	@Column
	private String status;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@Column
	private String fcmTokenString;

	@Column
	private Long parentId;

	@Column(length = 1000)
	private String token;

	@Column
	private  Integer RegisterOTP;

	@Column
	private Integer roleId;

	@Column
	private Integer otp;

	// REGISTRATION FIELDS --->
	private String fullName;

	private Integer age;

	private String gender;

	@Temporal(TemporalType.DATE)
	private Date dateOfBirth;

	private String nomineeFullName;

	private Integer nomineeAge;

	private String nomineeGender;

	private String nomineeMobileNumber;

	@Temporal(TemporalType.DATE)
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

	private String referenceName;

	private String referenceMobile;

	@Temporal(TemporalType.DATE)
	private Date registrationDate;

	private String kycStatus;

	@Temporal(TemporalType.DATE)
	private Date kycDate;

	@Temporal(TemporalType.DATE)
	private Date kycAcceptedDate;

	private String kycRejectReason;

	@Temporal(TemporalType.DATE)
	private Date kycRejectDate;

	private String aadhaarFront;

	private String aadhaarBack;

	private String panCardFront;

	private String passbookFront;

	public User(String username, String email, String password, String userMobNo, String address, String status, Set<Role> roles) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.userMobNo = userMobNo;
		this.address = address;
		this.status = status;
		this.roles = roles;
	}

}
