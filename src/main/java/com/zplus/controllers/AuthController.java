package com.zplus.controllers;

import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.zplus.config.AccountNumberGeneration;
import com.zplus.config.PasswordGeneration;
import com.zplus.models.*;
import com.zplus.payload.request.*;
import com.zplus.payload.response.*;
import com.zplus.repository.*;
import com.zplus.services.UserMasterService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.zplus.security.exception.TokenRefreshException;
import com.zplus.security.jwt.JwtUtils;
import com.zplus.services.impl.RefreshTokenService;
import com.zplus.services.impl.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  private UserMasterService userMasterService;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  private AccountTypeRepository accountTypeRepository;

  @Autowired
  private UserAccountMasterDao userAccountMasterDao;

  @Autowired
  private UserBankAccountRepository userBankAccountRepository;


   AccountNumberGeneration accountNumberGeneration=new AccountNumberGeneration();

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  JavaMailSender mailSender;

  @Autowired
  RefreshTokenService refreshTokenService;
  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    MainResDto mainResDto = new MainResDto();
    User user = new User();
    String dPassword;
    if (!userRepository.existsByUserMobNo(loginRequest.getUserMobNo())) {
      mainResDto.setResponseCode(HttpStatus.BAD_REQUEST.value());
      mainResDto.setFlag(false);
      mainResDto.setMessage("Error: Mobile number is Invalid!");
      return ResponseEntity.badRequest().body(mainResDto);

    } else {
      user = this.userRepository.findByUserMobNo(loginRequest.getUserMobNo()).get();
      dPassword = user.getPassword();
      if (!encoder.matches(loginRequest.getPassword(), dPassword)) {
        mainResDto.setResponseCode(HttpStatus.BAD_REQUEST.value());
        mainResDto.setFlag(false);
        mainResDto.setMessage("Error: password is Invalid!");
        return ResponseEntity.badRequest().body(mainResDto);
      }
    }

    String message;
    Integer responseCode;
    Boolean flag;

    if (userRepository.existsByUserMobNo(loginRequest.getUserMobNo())) {
      if (encoder.matches(loginRequest.getPassword(), dPassword)) {
        System.out.println("login successfully");
        message = "login successfully";
        responseCode = HttpStatus.OK.value();
        flag = true;
      } else {
        message = "Invalid password ";
        responseCode = HttpStatus.BAD_REQUEST.value();
        flag = false;
      }
    } else {
      message = "mobile no. not found";
      responseCode = HttpStatus.BAD_REQUEST.value();
      flag = false;
    }

    Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserMobNo(), loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String jwt = jwtUtils.generateJwtToken(userDetails);
    List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
            .collect(Collectors.toList());
    String role = roles.get(0);

    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
    return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
            userDetails.getEmail(), userDetails.getUserMobNo(), userDetails.getStatus(), role, message, responseCode, flag));
  }

  @PostMapping("/registration")
  public ResponseEntity registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    MainResDto mainResDto = new MainResDto();
    RegistrationResponse registrationResponse = new RegistrationResponse();

    User managementMember = this.userRepository.findById(signUpRequest.getManagementId()).get();

    if (userRepository.existsByUserMobNo(signUpRequest.getUserMobNo())) {
      mainResDto.setMessage("Error: mobileNo is already taken!");
      mainResDto.setResponseCode(HttpStatus.BAD_REQUEST.value());
      mainResDto.setFlag(false);
      return new ResponseEntity(mainResDto, HttpStatus.BAD_REQUEST);
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      mainResDto.setMessage("Error: email is already taken!");
      mainResDto.setResponseCode(HttpStatus.BAD_REQUEST.value());
      mainResDto.setFlag(false);
      return new ResponseEntity(mainResDto, HttpStatus.BAD_REQUEST);
    }

    String message;
    Integer responseCode;
    Boolean flag;

    User user = new User();
    user.setEmail(signUpRequest.getEmail());
    user.setUserMobNo(signUpRequest.getUserMobNo());
    user.setStatus("Active");
    BeanUtils.copyProperties(signUpRequest, user);
    String strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      user.setRoleId(2);
      roles.add(userRole);
      user.setRoles(roles);
      user.setManagementMember(managementMember.getId());
      message = "user registered successfully";
      responseCode = HttpStatus.OK.value();
      flag = true;
      registrationResponse.setMessage(message);
      registrationResponse.setResponseCode(responseCode);
      registrationResponse.setFlag(flag);
      this.userRepository.save(user);
      return new ResponseEntity(mainResDto, HttpStatus.OK);
    } else {
      switch (strRoles) {
        case "ROLE_ADMIN":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);
          user.setRoleId(1);

          message = "admin registered successfully";
          responseCode = HttpStatus.OK.value();
          flag = true;
          registrationResponse.setMessage(message);
          registrationResponse.setResponseCode(responseCode);
          registrationResponse.setFlag(flag);
          break;

        case "ROLE_USER":
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
          user.setRoleId(2);
          user.setManagementMember(managementMember.getId());

          message = "user registered successfully";
          responseCode = HttpStatus.OK.value();
          flag = true;
          registrationResponse.setMessage(message);
          registrationResponse.setResponseCode(responseCode);
          registrationResponse.setFlag(flag);
          break;

        case "ROLE_AGENT":
          Role agentRole = roleRepository.findByName(ERole.ROLE_AGENT)
                  .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(agentRole);
          user.setRoleId(3);

          message = "agent registered successfully";
          responseCode = HttpStatus.OK.value();
          flag = true;
          registrationResponse.setMessage(message);
          registrationResponse.setResponseCode(responseCode);
          registrationResponse.setFlag(flag);
      }
    }

    // if condition if role is not admin kyc create

    System.out.println(roles);
    user.setRoles(roles);
    user.setRegistrationDate(new Date());
    user.setKycStatus("Pending");
    String name = signUpRequest.getFullName();
    name = name.substring(0, 4);
    name = name.toUpperCase();

    Integer pass = PasswordGeneration.passwordGeneration();
    System.out.println("USER AUTO PASS = " + name + pass);
    user.setPassword(encoder.encode(name + pass));
    System.out.println("User Password = " + user.getPassword());
    String tempPassword = name+pass;
    User user1 = userRepository.save(user);

    if (user1.getRoles().stream().findFirst().get().getName().name().equalsIgnoreCase("User")){
      List<AccountTypeIdsRequest> list = signUpRequest.getAccountTypeIds();
      System.out.println(list);
      if (list != null) {
        for (AccountTypeIdsRequest accountTypeId : signUpRequest.getAccountTypeIds()) {
          AccountTypeMaster accountTypeMaster = this.accountTypeRepository.findById(accountTypeId.getAccountTypeId()).get();
          UserAccountMaster userAccountMaster = new UserAccountMaster();
          userAccountMaster.setUser(user1);
          userAccountMaster.setAccountTypeMaster(accountTypeMaster);
          userAccountMasterDao.save(userAccountMaster);

          AccountTypeMaster accountTypeMaster1 = this.accountTypeRepository.findById(accountTypeId.getAccountTypeId()).get();
          if (accountTypeMaster1 != null) {
            System.out.println("HI HI");
            UserBankAccountMaster userBankAccountMaster = new UserBankAccountMaster();
            String userBankNumber = accountNumberGeneration.bankAccountNumberGenerator();
            String userBankAccountNumber = userBankNumber;

            if (userBankAccountNumber.length() == 1) {
              userBankAccountNumber = "AUNLT0000000" + userBankAccountNumber;
              System.out.println("userBankAccountNumber1 1 = " + userBankAccountNumber);
              UserBankAccountMaster userBankAccountMaster1 = this.userBankAccountRepository.getByUserBankAccountNumber(userBankAccountNumber);
              if (userBankAccountMaster1 != null) {
                userBankNumber = accountNumberGeneration.bankAccountNumberGenerator();
                userBankAccountNumber = String.valueOf(userBankNumber);
                userBankAccountNumber = "AUNLT0000000" + userBankAccountNumber;
              }
            } else if (userBankAccountNumber.length() == 2) {
              System.out.println("HI2");
              userBankAccountNumber = "AUNLT000000" + userBankAccountNumber;
              System.out.println("userBankAccountNumber 2 2 = " + userBankAccountNumber);
              UserBankAccountMaster userBankAccountMaster1 = this.userBankAccountRepository.getByUserBankAccountNumber(userBankAccountNumber);
              if (userBankAccountMaster1 != null) {
                userBankNumber = accountNumberGeneration.bankAccountNumberGenerator();
                userBankAccountNumber = String.valueOf(userBankNumber);
                userBankAccountNumber = "AUNLT000000" + userBankAccountNumber;
              }
            } else if (userBankAccountNumber.length() == 3) {
              System.out.println("HI3");
              userBankAccountNumber = "AUNLT00000" + userBankAccountNumber;
              System.out.println("userBankAccountNumber3 3  = " + userBankAccountNumber);
              userBankAccountNumber = "AUNLT0000" + userBankAccountNumber;
              System.out.println("userBankAccountNumber4 4  = " + userBankAccountNumber);
              UserBankAccountMaster userBankAccountMaster1 = this.userBankAccountRepository.getByUserBankAccountNumber(userBankAccountNumber);
              if (userBankAccountMaster1 != null) {
                userBankNumber = accountNumberGeneration.bankAccountNumberGenerator();
                userBankAccountNumber = String.valueOf(userBankNumber);
                userBankAccountNumber = "AUNLT0000" + userBankAccountNumber;
              }
            } else if (userBankAccountNumber.length() == 4) {
              System.out.println("HI4");
              userBankAccountNumber = "AUNLT0000" + userBankAccountNumber;
              System.out.println("userBankAccountNumber4 4  = " + userBankAccountNumber);
              UserBankAccountMaster userBankAccountMaster1 = this.userBankAccountRepository.getByUserBankAccountNumber(userBankAccountNumber);
              if (userBankAccountMaster1 != null) {
                userBankNumber = accountNumberGeneration.bankAccountNumberGenerator();
                userBankAccountNumber = String.valueOf(userBankNumber);
                userBankAccountNumber = "AUNLT0000" + userBankAccountNumber;
              }
            } else if (userBankAccountNumber.length() == 5) {
              System.out.println("HI5");
              userBankAccountNumber = "AUNLT000" + userBankAccountNumber;
              System.out.println("userBankAccountNumber5 5  = " + userBankAccountNumber);
              UserBankAccountMaster userBankAccountMaster1 = this.userBankAccountRepository.getByUserBankAccountNumber(userBankAccountNumber);
              if (userBankAccountMaster1 != null) {
                userBankNumber = accountNumberGeneration.bankAccountNumberGenerator();
                userBankAccountNumber = String.valueOf(userBankNumber);
                userBankAccountNumber = "AUNLT000" + userBankAccountNumber;
              }
            } else if (userBankAccountNumber.length() == 6) {
              System.out.println("HI6");
              userBankAccountNumber = "AUNLT00" + userBankAccountNumber;
              System.out.println("userBankAccountNumber6 6  = " + userBankAccountNumber);
              UserBankAccountMaster userBankAccountMaster1 = this.userBankAccountRepository.getByUserBankAccountNumber(userBankAccountNumber);
              if (userBankAccountMaster1 != null) {
                userBankNumber = accountNumberGeneration.bankAccountNumberGenerator();
                userBankAccountNumber = String.valueOf(userBankNumber);
                userBankAccountNumber = "AUNLT00" + userBankAccountNumber;
              }
            } else if (userBankAccountNumber.length() == 7) {
              System.out.println("HI7");
              userBankAccountNumber = "AUNLT0" + userBankAccountNumber;
              System.out.println("userBankAccountNumber7 7 = " + userBankAccountNumber);
              UserBankAccountMaster userBankAccountMaster1 = this.userBankAccountRepository.getByUserBankAccountNumber(userBankAccountNumber);
              if (userBankAccountMaster1 != null) {
                userBankNumber = accountNumberGeneration.bankAccountNumberGenerator();
                userBankAccountNumber = String.valueOf(userBankNumber);
                userBankAccountNumber = "AUNLT0" + userBankAccountNumber;
              }
            } else if (userBankAccountNumber.length() == 8) {
              System.out.println("HI8");
              userBankAccountNumber = "AUNLT" + userBankAccountNumber;
              System.out.println("userBankAccountNumber 8 8 = " + userBankAccountNumber);
              UserBankAccountMaster userBankAccountMaster1 = this.userBankAccountRepository.getByUserBankAccountNumber(userBankAccountNumber);
              if (userBankAccountMaster1 != null) {
                userBankNumber = accountNumberGeneration.bankAccountNumberGenerator();
                userBankAccountNumber = String.valueOf(userBankNumber);
                userBankAccountNumber = "AUNLT" + userBankAccountNumber;
              }
            } else if (userBankAccountNumber.length() == 9) {
              System.out.println("HI9");
              userBankAccountNumber = "AUNLT" + userBankAccountNumber;
              System.out.println("userBankAccountNumber 9 9= " + userBankAccountNumber);

              UserBankAccountMaster userBankAccountMaster1 = this.userBankAccountRepository.getByUserBankAccountNumber(userBankAccountNumber);
              System.out.println("userBankAccountMaster1 = =  8 8 8 8 " + userBankAccountMaster1);

              if (userBankAccountMaster1 != null) {
                userBankNumber = accountNumberGeneration.bankAccountNumberGenerator();
                userBankAccountNumber = String.valueOf(userBankNumber);
                userBankAccountNumber = "AUNLT" + userBankAccountNumber;
              }
            }
            userBankAccountMaster.setAccountTypeMaster(accountTypeMaster);
            userBankAccountMaster.setUser(user1);
            userBankAccountMaster.setUserBankAccountNumber(userBankAccountNumber);
            System.out.println("USER BANK ACCOUNT NUMBER ==" + userBankAccountMaster.getUserBankAccountNumber());
            userBankAccountMaster.setDate(new Date());
            userBankAccountMaster.setStatus("Active");
            UserBankAccountMaster userBankAccountMaster1 = this.userBankAccountRepository.save(userBankAccountMaster);
            System.out.println("userBankAccountMaster1 = "+userBankAccountMaster1);
          }
        }

        List<UserBankAccountMaster> userBankAccountMasters = this.userBankAccountRepository.findByIdUserId(user1.getId());
        String accountNumber = null;
        String accountTypeName = null;

        for (UserBankAccountMaster bankAccountMaster : userBankAccountMasters) {
          accountNumber = bankAccountMaster.getUserBankAccountNumber();
          accountTypeName = bankAccountMaster.getAccountTypeMaster().getAccountTypeName();

        }

        String Message ="Welcome to Arthagam Urban Nidhi. As requested, here are your account credentials:"
                +"Username : "+user1.getUserMobNo()+"Password : "+tempPassword+" \n" +
                getAccounts(user1.getId())+
                " \n Please use the provided credentials to log in to your account. Upon your first login, you will be prompted to change your password for security purposes." +
                "Your "+accountTypeName+" account number is : "+accountNumber+ "Please keep this information secure and do not share it with anyone else." +
                " If you have any questions or need assistance, feel free to contact our support team";

        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setTo(user1.getEmail());
        simpleMailMessage.setFrom("kunalpawar9970@gmail.com");
        simpleMailMessage.setSubject("Account Information And Credentials");
        simpleMailMessage.setText(Message);
        simpleMailMessage.setSentDate(new Date());
        mailSender.send(simpleMailMessage);
        System.out.println("mail send");
      }
    }

    return new ResponseEntity(registrationResponse, HttpStatus.OK);
  }

  private String getAccounts(Long id) {
    List<UserBankAccountMaster> userBankAccountMasters = this.userBankAccountRepository.findByIdUserId(id);
    String firstAccount =" ";
    String secondAccount =" ";
    String thiredAccount =" " ;


    System.out.println("userBankAccountMasters ="+userBankAccountMasters.toString());

    System.out.println("  size  of list ="+userBankAccountMasters.size());
    for (int i=0;i<=userBankAccountMasters.size()-1;i++) {
      if(userBankAccountMasters.get(i).getAccountTypeMaster().getAccountTypeId()==1)
      {
        firstAccount="\n Account Type = "+userBankAccountMasters.get(i).getAccountTypeMaster().getAccountTypeName()+" Account Number ="+userBankAccountMasters.get(i).getUserBankAccountNumber();
      }
      if(userBankAccountMasters.get(i).getAccountTypeMaster().getAccountTypeId()==2)
      {
        secondAccount="\n Account Type ="+userBankAccountMasters.get(i).getAccountTypeMaster().getAccountTypeName()+" Account Number ="+userBankAccountMasters.get(i).getUserBankAccountNumber();
      }
      if(userBankAccountMasters.get(i).getAccountTypeMaster().getAccountTypeId()==3)
      {
        thiredAccount="\n Account Type = "+userBankAccountMasters.get(i).getAccountTypeMaster().getAccountTypeName()+" Account Number ="+userBankAccountMasters.get(i).getUserBankAccountNumber();
      }
    }


    return firstAccount+secondAccount+thiredAccount;
  }

  @PostMapping(value = "/forgotPassword")
  public ResponseEntity forgotPassword(@RequestBody ForgotPasswordReq forgotPasswordReq) {
    MainResDto mainResDto = userMasterService.forgotPassword1(forgotPasswordReq);

    if (mainResDto != null) {
      return new ResponseEntity(mainResDto, HttpStatus.OK);
    } else {
      return new ResponseEntity(mainResDto, HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping(value = "/VerificationOtpReq")
  public ResponseEntity VerificationOtpReq(@RequestBody VerificationOtpReq verificationOtpReq) {
    OtpVerificationResponse otpVerificationResponse = userMasterService.VerificationOtp(verificationOtpReq);
    if (otpVerificationResponse != null) {
      return new ResponseEntity(otpVerificationResponse, HttpStatus.OK);
    } else
      return new ResponseEntity(otpVerificationResponse, HttpStatus.BAD_REQUEST);
  }

  @PostMapping(value = "/newpassword")
  public ResponseEntity ChangePassword(@RequestBody ChangePasswordReq changePasswordReq) {
    ChangePasswordRes changePasswordRes = userMasterService.ChangePassword(changePasswordReq);

    if (changePasswordRes != null) {
      return new ResponseEntity(changePasswordRes, HttpStatus.OK);
    } else {
      return new ResponseEntity(changePasswordRes, HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/updateUser")
  public ResponseEntity updateUser(@RequestBody UpdateUserRequest updateUserRequest) {
    UpdateUserResponse updateUserResponse = userMasterService.updateUser(updateUserRequest);
    if (updateUserResponse != null) {
      return new ResponseEntity(updateUserResponse, HttpStatus.OK);
    } else {
      return new ResponseEntity(updateUserResponse, HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(value = "/getuserbyid/{id}")
  public ResponseEntity getUserByUserId(@PathVariable("id") Long id) {
    ProfileResponse profileResponse = new ProfileResponse();
    profileResponse = userMasterService.getUserByUserId(id);

    if (profileResponse != null) {
      return new ResponseEntity(profileResponse, HttpStatus.OK);
    } else {
      return new ResponseEntity(profileResponse, HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(value = "/getalluserbyrole/{role}")
//  @PreAuthorize("hasRole('Admin')")
  public ResponseEntity getAllUser(@PathVariable String role) {
    List<UserRes> list = userMasterService.getAllUser(role);

    if (list != null) {
      return new ResponseEntity(list, HttpStatus.OK);
    } else {
      return new ResponseEntity(list, HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/getallusers")
  public ResponseEntity getAAllUsers() {
    List<UserRes> list = userMasterService.getAllUsers();
    return new ResponseEntity(list, HttpStatus.OK);
  }

  @GetMapping(value = "/getallonlyactiveusers")
  public ResponseEntity getActiveUser() {
    UserRes userRes = new UserRes();
    List<UserRes> userResList = userMasterService.getActiveUser();
    return new ResponseEntity(userResList, HttpStatus.OK);
  }

  @GetMapping("/getallagents")
  public ResponseEntity getAllAgents() {
    List<UserRes> list = this.userMasterService.getAllAgents();

    if (list != null) {
      return new ResponseEntity(list, HttpStatus.OK);
    } else {
      return new ResponseEntity(list, HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/getallactiveagents")
  public ResponseEntity getAllActiveAgents() {
    List<UserRes> list = this.userMasterService.getAllActiveAgentsList();

    if (list != null) {
      return new ResponseEntity(list, HttpStatus.OK);
    } else {
      return new ResponseEntity(list, HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::getUser)
            .map(user -> {
              String token = jwtUtils.generateTokenFromUsername(user.getEmail());
              return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
            })
            .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                    "Refresh token is not in database!"));
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = userDetails.getId();
    refreshTokenService.deleteByUserId(userId);
    return ResponseEntity.ok(new MessageResponse("Log out successful!"));
  }

  @GetMapping(value = "/editByUserId/{id}")
  public ResponseEntity editByUserId(@PathVariable("id") Long id) {
    User user = new User();
    user = userRepository.findById(id).get();
    return new ResponseEntity(user, HttpStatus.OK);
  }

  @PostMapping("/createkyc")
  public ResponseEntity create(@RequestBody KYCRequest kycRequest) {
    MainResDto mainResDto = this.userMasterService.createKyc(kycRequest);

    if (mainResDto != null) {
      return new ResponseEntity(mainResDto, HttpStatus.OK);
    } else {
      return new ResponseEntity(mainResDto, HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/changepassword")
  public ResponseEntity changePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
    Boolean flag = this.userMasterService.updatePassword(updatePasswordRequest);

    if (Boolean.TRUE.equals(flag)) {
      return new ResponseEntity(flag, HttpStatus.OK);
    } else {
      return new ResponseEntity(flag, HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/getallpendingkyc")
  public ResponseEntity getAllPendingKyc() {
    List<PendingKYCResponse> pendingKYCResponses = this.userMasterService.getAllPendingKYC();

    if (pendingKYCResponses != null) {
      return new ResponseEntity(pendingKYCResponses, HttpStatus.OK);
    } else {
      return new ResponseEntity(pendingKYCResponses, HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/applyingkyclist")
  public ResponseEntity getApplyingKYC() {
    List<ApplyingKYCResponse> applyingKYCResponses = this.userMasterService.getApplyingKyc();
    if (applyingKYCResponses != null) {
      return new ResponseEntity(applyingKYCResponses, HttpStatus.OK);
    } else {
      return new ResponseEntity(applyingKYCResponses, HttpStatus.BAD_REQUEST);
    }

  }

  @PutMapping("/updatekyc")
  public ResponseEntity update(@RequestBody KYCRequest kycRequest) {
    KYCResponse kycResponse = this.userMasterService.updateKYC(kycRequest);

    if (kycResponse != null) {
      return new ResponseEntity(kycResponse, HttpStatus.OK);
    } else {
      return new ResponseEntity(kycResponse, HttpStatus.BAD_REQUEST);
    }
  }


  @GetMapping("/acceptkyc/{id}/{agentId}")
  public ResponseEntity acceptKYC(@PathVariable("id") Long id, @PathVariable("agentId") Long id1) {
    AcceptKYCResponse acceptKYCResponse = this.userMasterService.acceptKYC(id, id1);

    if (acceptKYCResponse != null) {
      return new ResponseEntity(acceptKYCResponse, HttpStatus.OK);
    } else {
      return new ResponseEntity(acceptKYCResponse, HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/rejectkyc")
  public ResponseEntity rejectKYC(@RequestBody RejectKYCRequest rejectKYCRequest) {
    RejectKYCResponse rejectKYCResponse = this.userMasterService.rejectKYC(rejectKYCRequest);

    if (rejectKYCResponse != null) {
      return new ResponseEntity(rejectKYCResponse, HttpStatus.OK);
    } else {
      return new ResponseEntity(rejectKYCResponse, HttpStatus.BAD_REQUEST);
    }
  }


  @PutMapping("/updatekycfrommanagement")
  public ResponseEntity updateKYCFromManagement(@RequestBody FromManagementUpdateKYCRequest fromManagementUpdateKYCRequest) {
    KYCResponse kycResponse = this.userMasterService.fromManagementUpdateKYC(fromManagementUpdateKYCRequest);

    if (kycResponse != null) {
      return new ResponseEntity(kycResponse, HttpStatus.OK);
    } else {
      return new ResponseEntity(kycResponse, HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/getstatuswisekyclist/{kycStatus}")
  public ResponseEntity getStatusWiseKYCList(@PathVariable("kycStatus") String kycStatus) {
    List<KYCResponse> kycResponse = this.userMasterService.getStatusWiseKYCList(kycStatus);

    if (kycResponse != null) {
      return new ResponseEntity(kycResponse, HttpStatus.OK);
    } else {
      return new ResponseEntity(kycResponse, HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/getidwisekycdetails/{id}")
  public ResponseEntity getIdWiseKYCDetails(@PathVariable("id") Long id) {
    KycDetailsResponse kycDetailsResponse = this.userMasterService.getIdWiseKycDetails(id);

    if (kycDetailsResponse != null) {
      return new ResponseEntity(kycDetailsResponse, HttpStatus.OK);
    } else {
      return new ResponseEntity(kycDetailsResponse, HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/getuserslistaddedbyadmin/{id}")
  public ResponseEntity getUsersListAddedByAdmin(@PathVariable("id") Long id){
    List<UserRes> list = this.userMasterService.getUsersListAddedByAdmin(id);

    if (list!=null){
      return new ResponseEntity(list, HttpStatus.OK);
    }else {
      return new ResponseEntity(list, HttpStatus.BAD_REQUEST);
    }
  }

}
