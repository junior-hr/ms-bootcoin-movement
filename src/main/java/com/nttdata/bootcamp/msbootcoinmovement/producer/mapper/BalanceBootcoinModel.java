package com.nttdata.bootcamp.msbootcoinmovement.producer.mapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

/**
 * Class BalanceBankAccountModel.
 * BootcoinMovement microservice class BalanceBankAccountModel.
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BalanceBootcoinModel {

    @JsonIgnore
    private String id;

    private String idBootCoin;

    private Double balance;
}
