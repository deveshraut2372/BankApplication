package com.zplus.services.impl;

import com.zplus.models.ERole;
import com.zplus.models.Role;
import com.zplus.models.User;
import com.zplus.payload.request.*;
import com.zplus.payload.response.*;
import com.zplus.repository.RoleRepository;
import com.zplus.repository.UserRepository;
import com.zplus.services.UserMasterService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserMasterServiceImpl implements UserMasterService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    JavaMailSender mailSender;

    @Override
    public List<UserRes> getAllUser(String role) {

        int rid=0;
        if (role.equals("Admin")) {
            rid=1;
        }else if (role.equals("User")){
            rid=2;
        } else if (role.equals("Agent")) {
            rid=3;
        }

        List<UserRes> userResList = this.userRepository.findUserListByRoleId(rid);
        return userResList;
    }


    @Override
    public UserRes getUserByUserId(Long id) {
        UserRes userRes=userRepository.getUserByUserId(id);
        if (userRes!=null){
            return userRes;
        }else {
            return new UserRes(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
                    null,null,null,null,null,null,null,null);
        }
    }

    @Override
    public UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest) {
        UpdateUserResponse updateUserResponse = new UpdateUserResponse();
        User user=new User();
         user=userRepository.findById(updateUserRequest.getId()).get();
         BeanUtils.copyProperties(updateUserRequest,user);
        try
        {
             User user1= userRepository.save(user);
             BeanUtils.copyProperties(user1,updateUserResponse);
             updateUserResponse.setRole(user1.getRoles().stream().findFirst().get().getName().name());
             return updateUserResponse;
        }catch (Exception e)
        {
            e.printStackTrace();
            return updateUserResponse;
        }
    }

    @Override
    public List<UserRes> getActiveUser() {
        List<UserRes> userResList=userRepository.getActiveUser();
        return userResList;
    }

    @Override
    public MainResDto forgotPassword1(ForgotPasswordReq forgotPasswordReq) {
        MainResDto mainResDto = new MainResDto();
        Random random=new Random();
        Integer otp =random.nextInt(99999);

        System.out.println("  otp otp otp ====   "+otp);
        if(userRepository.existsByUserMobNo(forgotPasswordReq.getUserMobNo()))
        {
            User user=userRepository.getByUserMobNo1(forgotPasswordReq.getUserMobNo());
            user.setOtp(otp);
            try
            {
                userRepository.save(user);

                MimeMessage message = mailSender.createMimeMessage();

                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setFrom("noreply@baeldung.com");
                helper.setTo(user.getEmail());
                helper.setSubject(" Otp Verification  ");
                helper.setText(" hii welcome your otp is "+otp+" please do not share this Any one ");

                mailSender.send(message);
                mainResDto.setMessage("OTP Sent on your email");
                mainResDto.setResponseCode(HttpStatus.OK.value());
                mainResDto.setFlag(true);
                return mainResDto;
            }catch (Exception e)
            {
                e.printStackTrace();
                mainResDto.setMessage("Something went wrong");
                mainResDto.setResponseCode(HttpStatus.BAD_REQUEST.value());
                mainResDto.setFlag(false);
                return mainResDto;
            }
        }else {
            mainResDto.setMessage("Invalid mobile number");
            mainResDto.setFlag(false);
            mainResDto.setResponseCode(HttpStatus.BAD_REQUEST.value());
            return mainResDto;
        }
    }

    @Override
    public OtpVerificationResponse VerificationOtp(VerificationOtpReq verificationOtpReq) {

        OtpVerificationResponse otpVerificationResponse = new OtpVerificationResponse();
        User user=new User();

        Optional<User> user1=userRepository.findById(verificationOtpReq.getId());

        if (!user1.equals(Optional.empty())){
            if(verificationOtpReq.getOtp().equals(user1.get().getOtp())){
                otpVerificationResponse.setId(user1.get().getId());
                otpVerificationResponse.setMessage("OTP Verified");
                otpVerificationResponse.setResponseCode(HttpStatus.OK.value());
                otpVerificationResponse.setFlag(true);
                return otpVerificationResponse;
            }
            else {
                otpVerificationResponse.setId(null);
                otpVerificationResponse.setMessage("OTP not Verified");
                otpVerificationResponse.setResponseCode(HttpStatus.BAD_REQUEST.value());
                otpVerificationResponse.setFlag(false);
                return otpVerificationResponse;
            }
        }else {
            otpVerificationResponse.setId(null);
            otpVerificationResponse.setMessage("User not found");
            otpVerificationResponse.setResponseCode(HttpStatus.BAD_REQUEST.value());
            otpVerificationResponse.setFlag(false);
            return otpVerificationResponse;
        }

    }

  @Override
  public List<UserRes> getAllUsers() {
      List<UserRes> list =userRepository.getAllUsers();
    return  list;
  }

    @Override
    public List<UserRes> getAllAgents() {
        List<UserRes> userResList = this.userRepository.getAllAgents();
        return userResList;
    }

    @Override
    public List<UserRes> getAllActiveAgentsList() {
        List<UserRes> list = this.userRepository.getAllActiveAgents();
        return list;
    }


    @Override
    public ChangePasswordRes ChangePassword(ChangePasswordReq changePasswordReq) {
        ChangePasswordRes changePasswordRes = new ChangePasswordRes();

        Optional<User> user1 = this.userRepository.findById(changePasswordReq.getId());
        if (!user1.equals(Optional.empty())){
            User user = this.userRepository.findById(changePasswordReq.getId()).get();
            if(changePasswordReq.getNewPassword().equals(changePasswordReq.getConfirmPassword())){
                user.setPassword(encoder.encode(changePasswordReq.getConfirmPassword()));
                User user2= this.userRepository.save(user);
                if (user2!=null){
                    changePasswordRes.setMessage("password changed");
                    changePasswordRes.setResponseCode(HttpStatus.OK.value());
                    changePasswordRes.setFlag(true);
                    return changePasswordRes;
                }else {
                    changePasswordRes.setMessage("Something went wrong.User not found.");
                    changePasswordRes.setResponseCode(HttpStatus.BAD_REQUEST.value());
                    changePasswordRes.setFlag(false);
                    return changePasswordRes;
                }
            }else {
                changePasswordRes.setMessage("Both Password doesn't matched");
                changePasswordRes.setResponseCode(HttpStatus.BAD_REQUEST.value());
                changePasswordRes.setFlag(false);
                return changePasswordRes;
            }
        }else {
            changePasswordRes.setMessage("User not found");
            changePasswordRes.setResponseCode(HttpStatus.BAD_REQUEST.value());
            changePasswordRes.setFlag(false);
            return changePasswordRes;
        }
    }
}
