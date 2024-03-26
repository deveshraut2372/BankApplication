package com.zplus.repository;

import com.zplus.models.CollectionMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepository extends JpaRepository<CollectionMaster,Integer> {

    @Query("select sum(cm.dailyCollectionAmount) from CollectionMaster as cm where cm.user.id=:id")
    Double getTotalCollectionAmount(@Param("id") Long id);

}
