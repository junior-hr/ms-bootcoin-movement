package com.nttdata.bootcamp.msbootcoinmovement.dto;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class ErrorDetail.
 * BootcoinMovement microservice class ErrorDetail.
 */
@Getter
@Setter
@NoArgsConstructor
public class ErrorDetail {

    private Date timestamp;
    private String message;
    private String details;

    public ErrorDetail(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

}