package com.zplus.repository;

import com.zplus.models.AccountTypeMaster;
import com.zplus.payload.response.AccountTypeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountTypeMaster,Integer> {
    @Query("select new com.zplus.payload.response.AccountTypeResponse(am.accountTypeId,am.accountTypeName,am.date,am.status) from AccountTypeMaster as am where am.accountTypeId=:accountTypeId")
    AccountTypeResponse getByAccountTypeId(@Param("accountTypeId") Integer accountTypeId);

    @Query("select new com.zplus.payload.response.AccountTypeResponse(am.accountTypeId,am.accountTypeName,am.date,am.status) from AccountTypeMaster as am ")
    List<AccountTypeResponse> getAll();

    @Query("select new com.zplus.payload.response.AccountTypeResponse(am.accountTypeId,am.accountTypeName,am.date,am.status) from AccountTypeMaster as am where am.status='Active'")
    List<AccountTypeResponse> getAllActive();

    @Query("select atm from AccountTypeMaster as atm where atm.accountTypeId=:accountTypeId")
    AccountTypeMaster getByAccountTypeIdWise(@Param("accountTypeId") Integer accountTypeId);
}
