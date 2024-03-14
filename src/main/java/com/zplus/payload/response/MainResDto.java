package com.zplus.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MainResDto {

    private String Message;

    private Boolean flag;

    private Integer ResponseCode;


    public MainResDto() {

    }
}
