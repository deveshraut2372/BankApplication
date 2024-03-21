package com.zplus.controllers;

import com.zplus.payload.request.AccountTypeRequest;
import com.zplus.payload.response.AccountTypeResponse;
import com.zplus.services.impl.AccountTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounttype")
@CrossOrigin(origins = "*")
public class AccountTypeController {
    @Autowired
    private AccountTypeServiceImpl accountTypeService;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody AccountTypeRequest accountTypeRequest){
        AccountTypeResponse accountTypeResponse = this.accountTypeService.create(accountTypeRequest);

        if (accountTypeResponse!=null){
            return new ResponseEntity(accountTypeResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity(accountTypeResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody AccountTypeRequest accountTypeRequest){
        AccountTypeResponse accountTypeResponse = this.accountTypeService.update(accountTypeRequest);

        if (accountTypeResponse!=null){
            return new ResponseEntity(accountTypeResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity(accountTypeResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getbyid/{accountTypeId}")
    public ResponseEntity getById(@PathVariable("accountTypeId") Integer accountTypeId){
        AccountTypeResponse accountTypeResponse  = this.accountTypeService.getById(accountTypeId);

        if (accountTypeResponse!=null){
            return new ResponseEntity(accountTypeResponse, HttpStatus.OK);
        }else {
            return new ResponseEntity(accountTypeResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getall")
    public ResponseEntity getAll(){
        List<AccountTypeResponse> accountTypeResponses = this.accountTypeService.getAll();

        if (accountTypeResponses!=null){
            return new ResponseEntity(accountTypeResponses, HttpStatus.OK);
        }else {
            return new ResponseEntity(accountTypeResponses, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getallactive")
    public ResponseEntity getAllActive(){
        List<AccountTypeResponse> accountTypeResponses = this.accountTypeService.getAllActive();

        if (accountTypeResponses!=null){
            return new ResponseEntity(accountTypeResponses, HttpStatus.OK);
        }else {
            return new ResponseEntity(accountTypeResponses, HttpStatus.BAD_REQUEST);
        }
    }
}
