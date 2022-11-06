package com.nttdata.bootcamp.msbootcoinmovement.model;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class BankAccount.
 * BootcoinMovement microservice class BankAccount.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Bootcoin {

    @Id
    private String idBootCoin;
    private Client client;
    private Double balance;

}
