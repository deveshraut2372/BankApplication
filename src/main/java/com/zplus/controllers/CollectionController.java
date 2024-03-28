package com.zplus.controllers;

import com.zplus.payload.request.CollectionAmountRequest;
import com.zplus.payload.request.CollectionSearchingRequest;
import com.zplus.payload.response.CollectionSearchingResponse;
import com.zplus.payload.response.MainResDto;
import com.zplus.services.impl.CollectionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/collection")
public class CollectionController {

    @Autowired
    private CollectionServiceImpl  collectionService;

    @PostMapping("/adddailycollectionamount")
    public ResponseEntity addDailyCollection(@RequestBody CollectionAmountRequest collectionAmountRequest){
        MainResDto mainResDto = this.collectionService.addDailyCollection(collectionAmountRequest);

        if (mainResDto!=null){
            return new ResponseEntity(mainResDto, HttpStatus.OK);
        }else {
            return new ResponseEntity(mainResDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updatedailycollection")
    public ResponseEntity updateDailyCollection(@RequestBody CollectionAmountRequest collectionAmountRequest){
        MainResDto mainResDto = this.collectionService.updateDailyCollectionAmount(collectionAmountRequest);

        if (mainResDto!=null){
            return new ResponseEntity(mainResDto, HttpStatus.OK);
        }else {
            return new ResponseEntity(mainResDto, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/searchingcollection")
    public ResponseEntity searchingCollection(@RequestBody CollectionSearchingRequest collectionSearchingRequest){
        CollectionSearchingResponse collectionSearchingResponse = this.collectionService.serachCollection(collectionSearchingRequest);

        if (collectionSearchingResponse!=null){
            return new ResponseEntity(collectionSearchingResponse,HttpStatus.OK);
        }else {
            return new ResponseEntity(collectionSearchingResponse, HttpStatus.BAD_REQUEST);
        }
    }

}
