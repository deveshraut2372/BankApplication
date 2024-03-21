package com.zplus.services;

import com.zplus.models.User;
import com.zplus.payload.request.*;
import com.zplus.payload.response.*;

import java.util.List;

public interface UserMasterService {


    List<UserRes> getAllUser(String role);

    UserRes getUserByUserId(Long id);

    UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest);

    List<UserRes> getActiveUser();

    ChangePasswordRes ChangePassword(ChangePasswordReq changePasswordReq);

    MainResDto forgotPassword1(ForgotPasswordReq forgotPasswordReq);

    OtpVerificationResponse VerificationOtp(VerificationOtpReq verificationOtpReq);

    List<UserRes> getAllUsers();


    List<UserRes> getAllAgents();

    List<UserRes> getAllActiveAgentsList();

    KYCResponse createKyc(KYCRequest kycRequest);

    Boolean updatePassword(UpdatePasswordRequest updatePasswordRequest);

    List<PendingKYCResponse> getAllPendingKYC();

    KYCResponse updateKYC(KYCRequest kycRequest);

    AcceptKYCResponse acceptKYC(Long id,Long id1);

    RejectKYCResponse rejectKYC(RejectKYCRequest rejectKYCRequest);

    List<ApplyingKYCResponse> getApplyingKyc();

    KYCResponse fromManagementUpdateKYC(FromManagementUpdateKYCRequest fromManagementUpdateKYCRequest);

    List<KYCResponse> getStatusWiseKYCList(String kycStatus);

    KycDetailsResponse getIdWiseKycDetails(Long id);
}
