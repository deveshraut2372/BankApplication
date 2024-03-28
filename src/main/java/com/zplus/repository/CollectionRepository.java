package com.zplus.repository;

import com.zplus.models.CollectionMaster;
import com.zplus.payload.response.CollectionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CollectionRepository extends JpaRepository<CollectionMaster,Integer> {

    @Query("select sum(cm.dailyCollectionAmount) from CollectionMaster as cm where cm.user.id=:id")
    Double getTotalCollectionAmount(@Param("id") Long id);

    @Query("select sum(cm.dailyCollectionAmount) from CollectionMaster as cm where date(cm.collectionDate)=:previousDay")
    CollectionResponse getPreviousDayCollection(@Param("previousDay") LocalDate previousDay);
}
