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
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;
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
                    null,null,null,null,null,null,null,null,null);
        }
    }

    @Override
    public UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest) {
        UpdateUserResponse updateUserResponse = new UpdateUserResponse();
         User user=userRepository.findById(updateUserRequest.getId()).get();
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
        Integer otp =random.nextInt(9999);

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
    public KYCResponse createKyc(KYCRequest kycRequest) {
        KYCResponse kycResponse = new KYCResponse();
        User user = this.userRepository.findById(kycRequest.getId()).get();
        Date NomineeDateOfBirth = kycRequest.getNomineeDateOfBirth();
        LocalDate ndob = NomineeDateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();

        Date userDateOfBirth = kycRequest.getDateOfBirth();
        LocalDate udob = userDateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Period period = Period.between(ndob,currentDate);
        int nomineeAge = period.getYears();
        System.out.println("nomineeAge = "+nomineeAge);

        Period period1 = Period.between(udob,currentDate);
        int userAge = period1.getYears();
        System.out.println("AGE userAge = "+userAge);

        BeanUtils.copyProperties(kycRequest,user);
        try {
            user.setKycStatus("Applying");
            user.setKycDate(new Date());
            user.setAge(userAge);
            user.setNomineeAge(nomineeAge);
            User user1 = this.userRepository.save(user);
            BeanUtils.copyProperties(user1,kycResponse);
            kycResponse.setMessage("KYC Updated");
            kycResponse.setResponseCode(HttpStatus.OK.value());
            kycResponse.setFlag(true);
            return kycResponse;
        }catch (Exception e){
            e.printStackTrace();
            kycResponse.setMessage("KYC not updated");
            kycResponse.setResponseCode(HttpStatus.BAD_REQUEST.value());
            kycResponse.setFlag(false);
            return kycResponse;
        }
    }

    @Override
    public Boolean updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        User user = this.userRepository.findById(updatePasswordRequest.getId()).get();
        try {
            user.setPassword(encoder.encode(updatePasswordRequest.getNewPassword()));
            this.userRepository.save(user);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<PendingKYCResponse> getAllPendingKYC() {
        List<PendingKYCResponse> pendingKYCResponses = this.userRepository.getAllPendingKYCResponse("Pending");
        return pendingKYCResponses;
    }

    @Override
    public KYCResponse updateKYC(KYCRequest kycRequest) {
        KYCResponse kycResponse = new KYCResponse();
        User user = this.userRepository.findById(kycRequest.getId()).get();

        if (user!=null){
            Date NomineeDateOfBirth = kycRequest.getNomineeDateOfBirth();
            LocalDate ndob = NomineeDateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate currentDate = LocalDate.now();

            Date userDateOfBirth = kycRequest.getDateOfBirth();
            LocalDate udob = userDateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Period period = Period.between(ndob,currentDate);
            int nomineeAge = period.getYears();

            Period period1 = Period.between(udob,currentDate);
            int userAge = period1.getYears();

            try {
                user.setAge(userAge);
                user.setNomineeAge(nomineeAge);
                user.setKycDate(new Date());
                user.setKycStatus("Pending");
                user.setKycRejectReason(null);
                user.setKycAcceptedDate(null);
                user.setKycRejectDate(null);
                BeanUtils.copyProperties(kycRequest,user);
                User user1 = this.userRepository.save(user);
                BeanUtils.copyProperties(user1,kycResponse);
                kycResponse.setMessage("KYC Updated");
                kycResponse.setResponseCode(HttpStatus.OK.value());
                kycResponse.setFlag(true);
                return kycResponse;
            }catch (Exception e){
                e.printStackTrace();
                return new KYCResponse(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
                        null,null,null,null,null,null,"something went wrong",HttpStatus.BAD_REQUEST.value(),false);
            }
        }else {
            return new KYCResponse(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
                    null,null,null,null,null,null,"User not found",HttpStatus.BAD_REQUEST.value(),false);
        }
    }

    @Override
    public AcceptKYCResponse acceptKYC(Long id,Long id1) {
        AcceptKYCResponse acceptKYCResponse = new AcceptKYCResponse();
        User user = this.userRepository.findById(id).get();
        User management = this.userRepository.findById(id1).get();
        System.out.println("Management = "+management);
        String managementMember = management.getRoles().stream().findFirst().get().getName().name();
        System.out.println("Management Member = "+managementMember);

        if (managementMember.equalsIgnoreCase("ROLE_AGENT") || managementMember.equalsIgnoreCase("ROLE_ADMIN")){
            System.out.println("Accept USER KYC");
            user.setKycAcceptedDate(new Date());
            try {
                user.setKycStatus("Accepted");
                user.setKycRejectReason(null);
                user.setKycRejectDate(null);
                User user1 = this.userRepository.save(user);
                BeanUtils.copyProperties(user1,acceptKYCResponse);
                return acceptKYCResponse;
            }catch (Exception e){
                e.printStackTrace();
                acceptKYCResponse.setId(null);
                acceptKYCResponse.setKycStatus(null);
                acceptKYCResponse.setKycAcceptedDate(null);
                return acceptKYCResponse;
            }
        }else {
            System.out.println("Invalid member");
            acceptKYCResponse.setKycStatus(null);
            acceptKYCResponse.setKycAcceptedDate(null);
            acceptKYCResponse.setId(null);
            return acceptKYCResponse;
        }
    }

    @Override
    public RejectKYCResponse rejectKYC(RejectKYCRequest rejectKYCRequest) {
        RejectKYCResponse rejectKYCResponse = new RejectKYCResponse();
        User user = this.userRepository.findById(rejectKYCRequest.getId()).get();
        try {
            user.setKycStatus("Rejected");
            user.setKycRejectReason(rejectKYCRequest.getKycRejectReason());
            user.setKycRejectDate(new Date());
            User user1= this.userRepository.save(user);
            rejectKYCResponse.setId(user.getId());
            rejectKYCResponse.setKycRejectReason(user1.getKycRejectReason());
            rejectKYCResponse.setKycStatus(user1.getKycStatus());
            rejectKYCResponse.setKycRejectDate(user1.getKycRejectDate());
            return rejectKYCResponse;
        }catch (Exception e){
            e.printStackTrace();
            return new RejectKYCResponse(null,null,null,null);
        }
    }

    @Override
    public List<ApplyingKYCResponse> getApplyingKyc() {
        List<ApplyingKYCResponse> applyingKYCResponses = this.userRepository.getAllApplyingKyc("Applying");
        return applyingKYCResponses;
    }

    @Override
    public KYCResponse fromManagementUpdateKYC(FromManagementUpdateKYCRequest fromManagementUpdateKYCRequest) {
        KYCResponse kycResponse = new KYCResponse();

        System.out.println("MA KYC = "+fromManagementUpdateKYCRequest);

        User user = this.userRepository.findById(fromManagementUpdateKYCRequest.getId()).get();
        User management = this.userRepository.findById(fromManagementUpdateKYCRequest.getManagementId()).get();

        String managementMember = management.getRoles().stream().findFirst().get().getName().name();

        if (managementMember.equalsIgnoreCase("ROLE_AGENT") || managementMember.equalsIgnoreCase("ROLE_ADMIN")){
            BeanUtils.copyProperties(fromManagementUpdateKYCRequest,user);
            user.setKycStatus("Accepted");
            user.setKycRejectDate(null);
            user.setKycAcceptedDate(new Date());
            user.setKycRejectReason(null);
            user.setKycDate(new Date());
            User user1 = this.userRepository.save(user);
            BeanUtils.copyProperties(user1,kycResponse);
            kycResponse.setMessage("KYC Updated By "+managementMember);
            kycResponse.setResponseCode(HttpStatus.OK.value());
            kycResponse.setFlag(true);
            return kycResponse;
        }else {
            kycResponse.setMessage("Invalid Management User");
            kycResponse.setFlag(false);
            kycResponse.setResponseCode(HttpStatus.BAD_REQUEST.value());
            return kycResponse;
        }
    }

    @Override
    public List<KYCResponse> getStatusWiseKYCList(String kycStatus) {
        List<KYCResponse> statusWiseList = this.userRepository.findAllByKycStatus(kycStatus);
        return statusWiseList;
    }

    @Override
    public KycDetailsResponse getIdWiseKycDetails(Long id) {
        KycDetailsResponse kycDetailsResponse = this.userRepository.getIdWiseKycDetails(id);

        if (kycDetailsResponse!=null){
            return kycDetailsResponse;
        }else {
            return new KycDetailsResponse(null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
                    null,null,null,null,null,null,null,null,null,null,null,null);
        }
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
