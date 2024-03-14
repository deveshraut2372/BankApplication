package com.zplus.payload.response;

public class JwtResponse {
	private String token;
	private String refreshToken;
	private String type = "Bearer";
	private Long id;
	private String email;

	private String userMobNo;

	private String status;
	private String roles;

	public JwtResponse(String token, String refreshToken, Long id, String email,String userMobNo,String status, String roles) {
		this.token = token;
		this.refreshToken = refreshToken;
		this.id = id;
		this.email = email;
		this.userMobNo = userMobNo;
		this.status = status;
		this.roles = roles;
	}




	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserMobNo() {
		return userMobNo;
	}


	public void setUserMobNo(String userMobNo) {
		this.userMobNo = userMobNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRoles() {
		return roles;
	}


}
