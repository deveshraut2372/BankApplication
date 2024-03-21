package com.zplus.repository;

import com.zplus.models.AccountTypeMaster;
import com.zplus.models.User;
import com.zplus.models.UserAccountMaster;
import com.zplus.payload.response.AccountTypeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAccountMasterDao extends JpaRepository<UserAccountMaster,Integer> {

    @Query("select uam.accountTypeMaster  from  UserAccountMaster as uam where uam.user.id=:id")
    List<AccountTypeMaster> getAllAccountTypeMasterByUser(@Param("id") Long id);
}
