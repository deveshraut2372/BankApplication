package com.zplus.services.impl;

import com.zplus.models.ERole;
import com.zplus.models.Role;
import com.zplus.models.User;
import com.zplus.payload.request.*;
import com.zplus.payload.response.*;
import com.zplus.repository.UserRepository;
import com.zplus.services.UserMasterService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private PasswordEncoder encoder;

    @Override
    public List<User> getAllUser(String role) {

        int id=0;
        if (role.equals("Admin")) {
            id=1;
        }else if (role.equals("User")){
            id=2;
        }
        System.out.println("role.........."+role);
        List<User> tempList=new ArrayList<>();
        List<User> list=userRepository.findAll();

        for (User user:list)
        {
                for (Role role1:user.getRoles())
                {
                    System.out.println("role1......"+role1.toString());
                    if (role1.getId()==id)
                    {
                        System.out.println("role......"+role1.toString());
                        tempList.add(user);
                    }
                }
        }
        return tempList;
    }

    @Override
    public UserRes getUserByUserId(Long id) {

        UserRes userRes=userRepository.getUserByUserId(id);

        return userRes;
    }

    @Override
    public Boolean updateUser(SignupRequest signupRequest) {

        User user=new User();
         user=userRepository.findById(signupRequest.getId()).get();
//         user.setAddress(signupRequest.getAddress());

        try
        {
            userRepository.save(user);

            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<UserRes> getActiveUser() {
        List<UserRes> userResList=userRepository.getActiveUser();
        return userResList;
    }

    @Override
    public Boolean forgotPassword1(ForgotPasswordReq forgotPasswordReq) {

        Boolean flag=false;
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

//                MimeMessage message = emailSender.createMimeMessage();
//
//                MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//                helper.setFrom("noreply@baeldung.com");
//                helper.setTo(user.getEmail());
//                helper.setSubject(" Otp Verification  ");
//                helper.setText(" hii welcome your otp is "+otp+" please do not share this Any one ");
//
//                emailSender.send(message);

                flag= true;
            }catch (Exception e)
            {
                e.printStackTrace();
                flag =false;
            }
        }

        return flag;
    }

    @Override
    public Boolean VerificationOtp(VerificationOtpReq verificationOtpReq) {

        Boolean flag=false;
        User user=new User();

        Optional<User> user1=userRepository.findById(verificationOtpReq.getId());
        user1.ifPresent(settingMaster ->BeanUtils.copyProperties(settingMaster,user));

        if(verificationOtpReq.getOtp().equals(user.getOtp()))
        {
            flag=true;
        }
        else {
            flag=false;
        }
        return flag;
    }

  @Override
  public List getAAllUsers() {

      List<User> list=new ArrayList();

      list=userRepository.findAll();
      List list1=new ArrayList();
    return  list1;
  }


  @Override
    public ChangePasswordRes ChangePassword(ChangePasswordReq changePasswordReq) {

        ChangePasswordRes changePasswordRes=new ChangePasswordRes();
//        String oldpass=encoder.encode(changePasswordReq.getOldPassword());
        String dbpassword = userRepository.findOldPasswordById((changePasswordReq.getId()));

        System.out.println(" old password =="+dbpassword);
        System.out.println(" check old password =="+changePasswordReq.getOldPassword());

        if (encoder.matches(changePasswordReq.getOldPassword(),dbpassword)) {
            System.out.println(" pass is match ");
            String nPassword=encoder.encode(changePasswordReq.getNewPassword());
            Integer updatepassword1 =userRepository.updatePassword(changePasswordReq.getId(), nPassword);
            if (updatepassword1>0) {
                System.out.println("password update successfully");
                changePasswordRes.setMsg("password update successfully");
                changePasswordRes.setFlag(true);
                return changePasswordRes;
            } else {
                changePasswordRes.setMsg("Inavlid Password");
                changePasswordRes.setFlag(false);
                return changePasswordRes;
            }
        } else {
            changePasswordRes.setFlag(false);
            changePasswordRes.setMsg("password not mach");
            System.out.println("password not mach..!");
            return changePasswordRes;
        }
    }
}






















////////////////////


//    @Override
//    public DashBoardCountResDto getActiveUserCount(Long agentId) {
//
//        DashBoardCountResDto dashBoardCountResDto=new DashBoardCountResDto();
//
//        dashBoardCountResDto.setActiveUserCnt(userRepository.getActiveUserCount(agentId));
//        System.out.println(" Active User Count  =="+dashBoardCountResDto.getActiveUserCnt());
//        dashBoardCountResDto.setActiveAgentCnt(userRepository.setActiveAgentCnt());
//        System.out.println(" Active Agents Count  =="+dashBoardCountResDto.getActiveAgentCnt());
//        dashBoardCountResDto.setTotalBidAmount(bidMasterDao.getTotalBidAmount(agentId));
//        System.out.println(" Sum of totalBidAmount  =="+dashBoardCountResDto.getTotalBidAmount());
//        dashBoardCountResDto.setTotalWinAmount(winingHistoryDao.getTotalWinAmount(agentId));
//        System.out.println(" Sum of totalWinAmount  =="+dashBoardCountResDto.getTotalWinAmount());
//
//        return dashBoardCountResDto;
//    }

//    @Override
//    public TotalWinningAmountResDto getTotalWinningAmount(String fromdate, String todate) {
//
//        TotalWinningAmountResDto totalWinningAmountResDto=new TotalWinningAmountResDto();
//        totalWinningAmountResDto.setTotalBidAmount(bidMasterDao.sumByBidPointFromtoDate(fromdate,todate));
//        System.out.println(" total Bid Amount "+totalWinningAmountResDto.getTotalBidAmount());
//
//        totalWinningAmountResDto.setTotalWinAmount(winingHistoryDao.sumByBidPointFromToDate(fromdate,todate));
//        System.out.println(" Total Win Amount "+totalWinningAmountResDto.getTotalWinAmount());
//        return totalWinningAmountResDto;
//    }
//
//    @Override
//    public List agentWiseUserList(AgentWiseUserReqDto agentWiseUserReqDto) {
//
//        List<AgentWiseUserListResDto> list=bidMasterDao.agentWiseUserList(agentWiseUserReqDto.getFromDate(),agentWiseUserReqDto.getToDate(),agentWiseUserReqDto.getAgentId());
//
//        for (AgentWiseUserListResDto  agentWiseUserListResDto:list) {
//            User user=new User();
//            user=userRepository.findById(agentWiseUserListResDto.getId()).get();
//
//            User agent=userRepository.findById(user.getParentId()).get();
//            agentWiseUserListResDto.setId(agent.getId());
//            agentWiseUserListResDto.setAgentName(agent.getUsername());
//            agentWiseUserListResDto.setAgentMobNo(agent.getUserMobNo());
//        }
//        return list;
//    }

//    @Override
//    public List agentWiseUserWinningList(AgentWiseUserReqDto agentWiseUserReqDto) {
//
//        List<WinningHistoryUserwise> list=winingHistoryDao.agentWiseUserWinningList(agentWiseUserReqDto.getFromDate(),agentWiseUserReqDto.getToDate(),agentWiseUserReqDto.getAgentId());
//        return list;
//    }
