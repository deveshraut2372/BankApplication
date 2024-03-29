package com.zplus.services;

import com.zplus.payload.request.CollectionAmountRequest;
import com.zplus.payload.request.CollectionSearchingRequest;
import com.zplus.payload.response.CollectionResponse;
import com.zplus.payload.response.CollectionSearchingResponse;
import com.zplus.payload.response.MainResDto;

import java.util.List;

public interface CollectionService {

    MainResDto addDailyCollection(CollectionAmountRequest collectionAmountRequest);

    MainResDto updateDailyCollectionAmount(CollectionAmountRequest collectionAmountRequest);

    CollectionSearchingResponse serachCollection(CollectionSearchingRequest collectionSearchingRequest);
}
