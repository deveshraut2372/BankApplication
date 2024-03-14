package com.zplus.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@NoArgsConstructor
public class FileUploadResDto {

    private String path;
    private Boolean status;
}
