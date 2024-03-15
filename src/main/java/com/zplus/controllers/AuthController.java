package com.zplus.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.zplus.models.*;
import com.zplus.payload.request.*;
import com.zplus.payload.response.*;
import com.zplus.repository.RefreshTokenRepository;
import com.zplus.services.UserMasterService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.zplus.security.exception.TokenRefreshException;
import com.zplus.repository.RoleRepository;
import com.zplus.repository.UserRepository;
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
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

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

    }else {
      user = this.userRepository.findByUserMobNo(loginRequest.getUserMobNo()).get();
      dPassword= user.getPassword();
      if (!encoder.matches(loginRequest.getPassword(),dPassword)){
        mainResDto.setResponseCode(HttpStatus.BAD_REQUEST.value());
        mainResDto.setFlag(false);
        mainResDto.setMessage("Error: password is Invalid!");
        return ResponseEntity.badRequest().body(mainResDto);
      }
    }

    String message;
    Integer responseCode;
    Boolean flag;

    if(userRepository.existsByUserMobNo(loginRequest.getUserMobNo())){
      if (encoder.matches(loginRequest.getPassword(),dPassword)){
        System.out.println("login successfully");
        message = "login successfully";
        responseCode = HttpStatus.OK.value();
        flag = true;
      }else {
        message = "Invalid password ";
        responseCode = HttpStatus.BAD_REQUEST.value();
        flag = false;
      }
    }else {
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
    String role=roles.get(0);

    RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
    return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
            userDetails.getEmail(), userDetails.getUserMobNo(), userDetails.getStatus(), role,message,responseCode,flag));
  }

  @PostMapping("/registration")
  public ResponseEntity registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    MainResDto mainResDto = new MainResDto();

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

    User user =new User();
    user.setEmail(signUpRequest.getEmail());
    user.setUserMobNo(signUpRequest.getUserMobNo());
    user.setStatus("Active");
    BeanUtils.copyProperties(signUpRequest,user);
    user.setPassword(encoder.encode(signUpRequest.getPassword()));
    String strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      user.setRoleId(2);
      roles.add(userRole);
      user.setRoles(roles);
      message = "user registered successfully";
      responseCode = HttpStatus.OK.value();
      flag = true;
      mainResDto.setMessage(message);
      mainResDto.setResponseCode(responseCode);
      mainResDto.setFlag(flag);
      this.userRepository.save(user);
      return new ResponseEntity(mainResDto, HttpStatus.OK);
    } else {
        switch (strRoles) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);
          user.setRoleId(1);

          message = "admin registered successfully";
          responseCode = HttpStatus.OK.value();
          flag = true;
          mainResDto.setMessage(message);
          mainResDto.setResponseCode(responseCode);
          mainResDto.setFlag(flag);
          break;

          case "user":
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
            user.setRoleId(2);

            message = "user registered successfully";
            responseCode = HttpStatus.OK.value();
            flag = true;
            mainResDto.setMessage(message);
            mainResDto.setResponseCode(responseCode);
            mainResDto.setFlag(flag);
          break;

          case "agent":
            Role agentRole = roleRepository.findByName(ERole.ROLE_AGENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(agentRole);
            user.setRoleId(3);

            message = "agent registered successfully";
            responseCode = HttpStatus.OK.value();
            flag = true;
            mainResDto.setMessage(message);
            mainResDto.setResponseCode(responseCode);
            mainResDto.setFlag(flag);
            break;
        }
    }
    System.out.println(roles);
    user.setRoles(roles);
    userRepository.save(user);

    return new ResponseEntity(mainResDto, HttpStatus.OK);
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
  @GetMapping(value = "/getAllUser/{role}")
  @PreAuthorize("hasRole('ADMIN')")
  public  ResponseEntity getAllUser(@PathVariable String role)
  {
    List<User> list=userMasterService.getAllUser(role);
    return  new ResponseEntity(list,HttpStatus.OK);
  }


  @PutMapping("/updateUser")
   public ResponseEntity updateUser(@RequestBody SignupRequest  signupRequest)
  {
    Boolean flag=userMasterService.updateUser(signupRequest);
    if(flag)
    {
      return new ResponseEntity(flag, HttpStatus.OK);
    }else {
      return new ResponseEntity(flag, HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(value = "/getUserByUserId/{id}")
  public ResponseEntity getUserByUserId(@PathVariable("id") Long id)
  {
    UserRes userRes=new UserRes();
    userRes=userMasterService.getUserByUserId(id);

    if(userRes!=null)
    {
      return  new ResponseEntity(userRes,HttpStatus.OK);
    }
    else
    {
      return new ResponseEntity(userRes,HttpStatus.BAD_REQUEST);
    }
  }


  @GetMapping(value = "/getActiveUsers")
  public ResponseEntity getActiveUser()
  {
    UserRes userRes=new UserRes();
    List<UserRes> userResList=userMasterService.getActiveUser();
    return new ResponseEntity(userResList,HttpStatus.OK);
  }


  @GetMapping(value = "/editByUserId/{id}")
  public ResponseEntity editByUserId(@PathVariable("id") Long id)
  {

    User user=new User();
     user=userRepository.findById(id).get();
     return new ResponseEntity(user,HttpStatus.OK);
  }



  @PostMapping(value = "/ChangePassword")
  public ResponseEntity ChangePassword(@RequestBody ChangePasswordReq changePasswordReq) {
    ChangePasswordRes changePasswordRes = userMasterService.ChangePassword(changePasswordReq);
    return new ResponseEntity(changePasswordRes, HttpStatus.OK);
  }

  @PostMapping(value = "/forgotPassword")
  public ResponseEntity forgotPassword(@RequestBody ForgotPasswordReq forgotPasswordReq)
  {
    Boolean flag= userMasterService.forgotPassword1(forgotPasswordReq);
    if(flag)
    {
      return new ResponseEntity(flag,HttpStatus.OK);
    }else {
      return new ResponseEntity(flag,HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping(value = "/VerificationOtpReq")
  public ResponseEntity VerificationOtpReq(@RequestBody VerificationOtpReq verificationOtpReq)
  {
    Boolean flag=userMasterService.VerificationOtp(verificationOtpReq);
    if(flag)
    {
      return new ResponseEntity(flag,HttpStatus.OK);
    }else
      return new ResponseEntity(flag,HttpStatus.BAD_REQUEST);
  }


  @GetMapping("/getAAllUsers")
  public  ResponseEntity getAAllUsers()
  {
    List list=new ArrayList();
    list=userMasterService.getAAllUsers();

      return new ResponseEntity( list,HttpStatus.OK);
  }

}
