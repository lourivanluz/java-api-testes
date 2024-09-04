package com.apitestes.demo.exceptions;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class StandardExpection {

    private LocalDateTime timeStamp;
    private Integer status;
    private String error;
    private String path;
    
}
