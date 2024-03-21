package com.zplus.repository;

import com.zplus.models.UserBankAccountMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBankAccountRepository extends JpaRepository<UserBankAccountMaster,Integer> {
    @Query("select ubam from UserBankAccountMaster as ubam where ubam.userBankAccountNumber=:userBankAccountNumber")
    boolean existsByUserBankAccountNumber(@Param("userBankAccountNumber") String userBankAccountNumber);
}
