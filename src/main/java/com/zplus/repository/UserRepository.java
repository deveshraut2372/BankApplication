package com.zplus.repository;

import com.zplus.models.User;
import com.zplus.payload.response.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Boolean existsByEmail(String email);

    boolean existsByUserMobNo(String userMobNo);

    @Query("select new com.zplus.payload.response.UserRes(um.id, um.email,um.password,um.userMobNo,um.address,um.status,um.fullName,um.age,um.gender,um.dateOfBirth,um.nomineeFullName,um.nomineeAge,um.nomineeGender,um.nomineeMobileNumber,um.nomineeDateOfBirth,um.photo,um.otherAddress,um.villageAddress,um.occupation,um.officeAddress,um.panNo,um.aadhaarCardNo,um.propertyType,um.referenceName,um.referenceMobile,um.registrationDate) from User as um where um.id=:id")
    UserRes getUserByUserId(@Param("id") Long id);

  @Query("select um.fcmTokenString as fcmTokenString from User as um where um.id=:id")
  String getToken(@Param("id") Long id);

  @Query("select um from User as um where um.parentId=:parentId")
    List getAllUserByParentId(@Param("parentId") Integer parentId);

  @Query(" select um from User as um where um.id=:id")
  User getByUserId(@Param("id") Long id);
  @Query("select new com.zplus.payload.response.UserRes(um.id,um.email,um.password,um.userMobNo,um.address,um.status,um.fullName,um.age,um.gender,um.dateOfBirth,um.nomineeFullName,um.nomineeAge,um.nomineeGender,um.nomineeMobileNumber,um.nomineeDateOfBirth,um.photo,um.otherAddress,um.villageAddress,um.occupation,um.officeAddress,um.panNo,um.aadhaarCardNo,um.propertyType,um.referenceName,um.referenceMobile,um.registrationDate)  from User as um  where um.status='Active' and um.roleId=2")
  List<UserRes> getActiveUser();
  String findOldPasswordById(@Param("id") Long id);
  @Transactional
  @Modifying
  @Query("update User as um  set um.password=:npassword where um.id=:id")
  Integer updatePassword(@Param("id") Long id,@Param("npassword") String nPassword);

  Optional<User> findByUserMobNo(String username);

  @Query("select um from User as um where um.userMobNo=:userMobNo")
  User getByUserMobNo1(@Param("userMobNo") String userMobNo);

  @Query("select new com.zplus.payload.response.UserRes(um.id,um.email,um.password,um.userMobNo,um.address,um.status,um.fullName,um.age,um.gender,um.dateOfBirth,um.nomineeFullName,um.nomineeAge,um.nomineeGender,um.nomineeMobileNumber,um.nomineeDateOfBirth,um.photo,um.otherAddress,um.villageAddress,um.occupation,um.officeAddress,um.panNo,um.aadhaarCardNo,um.propertyType,um.referenceName,um.referenceMobile,um.registrationDate)  from User as um  where um.roleId=:rid and um.status='Active'")
  List<UserRes> findUserListByRoleId(@Param("rid") int rid);

  @Query("select new com.zplus.payload.response.UserRes(um.id,um.email,um.password,um.userMobNo,um.address,um.status,um.fullName,um.age,um.gender,um.dateOfBirth,um.nomineeFullName,um.nomineeAge,um.nomineeGender,um.nomineeMobileNumber,um.nomineeDateOfBirth,um.photo,um.otherAddress,um.villageAddress,um.occupation,um.officeAddress,um.panNo,um.aadhaarCardNo,um.propertyType,um.referenceName,um.referenceMobile,um.registrationDate)  from User as um ")
  List<UserRes> getAllUsers();

  @Query("select new com.zplus.payload.response.UserRes(um.id,um.email,um.password,um.userMobNo,um.address,um.status,um.fullName,um.age,um.gender,um.dateOfBirth,um.nomineeFullName,um.nomineeAge,um.nomineeGender,um.nomineeMobileNumber,um.nomineeDateOfBirth,um.photo,um.otherAddress,um.villageAddress,um.occupation,um.officeAddress,um.panNo,um.aadhaarCardNo,um.propertyType,um.referenceName,um.referenceMobile,um.registrationDate)  from User as um where um.roleId=3")
    List<UserRes> getAllAgents();

  @Query("select new com.zplus.payload.response.UserRes(um.id,um.email,um.password,um.userMobNo,um.address,um.status,um.fullName,um.age,um.gender,um.dateOfBirth,um.nomineeFullName,um.nomineeAge,um.nomineeGender,um.nomineeMobileNumber,um.nomineeDateOfBirth,um.photo,um.otherAddress,um.villageAddress,um.occupation,um.officeAddress,um.panNo,um.aadhaarCardNo,um.propertyType,um.referenceName,um.referenceMobile,um.registrationDate)  from User as um where um.roleId=3 and um.status='Active'")
  List<UserRes> getAllActiveAgents();

  @Query("select new com.zplus.payload.response.PendingKYCResponse(um.id,um.kycStatus) from User as um where um.kycStatus='Pending' and um.roleId=2 ")
    List<PendingKYCResponse> getAllPendingKYCResponse(String pending);

  @Query("select new com.zplus.payload.response.ApplyingKYCResponse(um.id,um.kycStatus) from User as um where um.kycStatus='Applying' and um.roleId=2 ")
  List<ApplyingKYCResponse> getAllApplyingKyc(String applying);

  @Query("select new com.zplus.payload.response.KYCResponse(u.id,u.fullName,u.gender,u.dateOfBirth,u.nomineeFullName,u.nomineeAge,u.nomineeGender,u.nomineeMobileNumber,u.nomineeDateOfBirth,u.address,u.otherAddress,u.villageAddress,u.occupation,u.officeAddress,u.panNo,u.aadhaarCardNo,u.propertyType,u.referenceName,u.referenceMobile,u.kycDate,u.aadhaarFront,u.aadhaarBack,u.panCardFront,u.passbookFront,u.photo) from User as u where u.kycStatus=:kycStatus")
  List<KYCResponse> findAllByKycStatus(@Param("kycStatus") String kycStatus);

  @Query("select new com.zplus.payload.response.KycDetailsResponse(u.id,u.fullName,u.gender,u.dateOfBirth,u.nomineeFullName,u.nomineeAge,u.nomineeGender,u.nomineeMobileNumber,u.nomineeDateOfBirth,u.address,u.otherAddress,u.villageAddress,u.occupation,u.officeAddress,u.panNo,u.aadhaarCardNo,u.propertyType,u.referenceName,u.referenceMobile,u.aadhaarFront,u.aadhaarBack,u.panCardFront,u.passbookFront,u.kycStatus,u.kycDate,u.kycAcceptedDate,u.kycRejectReason,u.kycRejectDate,u.photo) from User as u where u.id=:id and u.status='Active'")
  KycDetailsResponse getIdWiseKycDetails(@Param("id") Long id);
}
