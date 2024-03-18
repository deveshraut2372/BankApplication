package com.zplus.services;

import com.zplus.models.User;
import com.zplus.payload.request.*;
import com.zplus.payload.response.*;

import java.util.List;

public interface UserMasterService {


    List<UserRes> getAllUser(String role);

    UserRes getUserByUserId(Long id);

    UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest);

//    DashBoardCountResDto getActiveUserCount(Long agentId);
//
//    TotalWinningAmountResDto getTotalWinningAmount(String fromdate, String todate);
//
//    List agentWiseUserList(AgentWiseUserReqDto agentWiseUserReqDto);
//
//    List agentWiseUserWinningList(AgentWiseUserReqDto agentWiseUserReqDto);

    List<UserRes> getActiveUser();

    ChangePasswordRes ChangePassword(ChangePasswordReq changePasswordReq);

    MainResDto forgotPassword1(ForgotPasswordReq forgotPasswordReq);

    OtpVerificationResponse VerificationOtp(VerificationOtpReq verificationOtpReq);

    List<UserRes> getAllUsers();


    List<UserRes> getAllAgents();

    List<UserRes> getAllActiveAgentsList();
}
