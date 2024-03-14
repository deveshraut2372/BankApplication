package com.zplus.payload.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
public class LoginRequest {
	@NotBlank
	private String userMobNo;

	@NotBlank
	private String password;


}
