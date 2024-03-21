package com.zplus.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class AccountTypeMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountTypeId;

    private String accountTypeName;

    @Temporal(TemporalType.DATE)
    private Date date;

    private String status;
}
