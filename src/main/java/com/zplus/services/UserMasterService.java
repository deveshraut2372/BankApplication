package com.zplus.services;

import com.zplus.models.User;
import com.zplus.payload.request.*;
import com.zplus.payload.response.ChangePasswordRes;
import com.zplus.payload.response.UserRes;

import java.util.List;

public interface UserMasterService {


    List<User> getAllUser(String role);

    UserRes getUserByUserId(Long id);

    Boolean updateUser(SignupRequest signupRequest);

//    DashBoardCountResDto getActiveUserCount(Long agentId);
//
//    TotalWinningAmountResDto getTotalWinningAmount(String fromdate, String todate);
//
//    List agentWiseUserList(AgentWiseUserReqDto agentWiseUserReqDto);
//
//    List agentWiseUserWinningList(AgentWiseUserReqDto agentWiseUserReqDto);

    List<UserRes> getActiveUser();

    ChangePasswordRes ChangePassword(ChangePasswordReq changePasswordReq);

    Boolean forgotPassword1(ForgotPasswordReq forgotPasswordReq);

    Boolean VerificationOtp(VerificationOtpReq verificationOtpReq);

  List getAAllUsers();
//    List<User> findByRole(String role);


}
