package com.zplus.repository;

import com.zplus.models.UserBankAccountMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBankAccountRepository extends JpaRepository<UserBankAccountMaster,Integer> {
    @Query("select u from UserBankAccountMaster as u where u.userBankAccountNumber=:userBankAccountNumber")
    UserBankAccountMaster getByUserBankAccountNumber(@Param("userBankAccountNumber") String userBankAccountNumber);

    @Query("select ubam from UserBankAccountMaster as ubam where ubam.user.id=:id")
    List<UserBankAccountMaster> getAllBankAccountsById(@Param("id") Long id);

    @Query("select ubam from UserBankAccountMaster as ubam where ubam.accountTypeMaster.accountTypeId=:accountTypeId and ubam.user.id=:id ")
    UserBankAccountMaster getByUserAccountTypeIdAndIdWise(@Param("accountTypeId") Integer accountTypeId,@Param("id") Long id);

    @Query("select ubam from UserBankAccountMaster as ubam where ubam.user.id=:id ")
    List<UserBankAccountMaster> findByIdUserId(@Param("id") Long id);

    @Query("select count(ubam.userBankAccountId) from UserBankAccountMaster as ubam where ubam.userBankAccountNumber=:userBankAccountNumber")
    Integer getcountByUserBankAccountNumber(@Param("userBankAccountNumber") String bankAccountNumber);

    boolean existsByUserBankAccountNumber(String bankAccountNumber);
}
