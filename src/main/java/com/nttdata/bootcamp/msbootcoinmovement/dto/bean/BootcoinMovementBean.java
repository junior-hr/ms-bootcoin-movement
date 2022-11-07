package com.nttdata.bootcamp.msbootcoinmovement.dto.bean;

import com.nttdata.bootcamp.msbootcoinmovement.model.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Class BankAccountBean.
 * BankAccount microservice class BankAccountBean.
 */
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Data
@SuperBuilder
@ToString

public abstract class BootcoinMovementBean {

    private String bootcoinMovementType;
    private Double amount;
    private Double balance;
    private String currency;
    private LocalDateTime bootcoinMovementDate;
    private String documentNumber;
    private String documentNumberForTransfer;

    public abstract Mono<Boolean> validateAvailableBalance(Bootcoin bootcoin);
    public abstract Mono<BootcoinMovement> mapperToBootcoinMovement(Bootcoin bootcoin);
}