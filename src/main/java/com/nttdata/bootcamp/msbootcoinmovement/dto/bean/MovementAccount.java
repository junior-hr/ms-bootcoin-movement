package com.nttdata.bootcamp.msbootcoinmovement.dto.bean;

import com.nttdata.bootcamp.msbootcoinmovement.exception.ResourceNotFoundException;
import com.nttdata.bootcamp.msbootcoinmovement.model.Bootcoin;
import com.nttdata.bootcamp.msbootcoinmovement.model.BootcoinMovement;
import lombok.Builder;
import lombok.ToString;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@AllArgsConstructor
@Slf4j
@Data
@SuperBuilder
@ToString
@Builder
public class MovementAccount extends BootcoinMovementBean {

    @Override
    public Mono<Boolean> validateAvailableBalance(Bootcoin bootcoin) {
        log.info("ini MovementAccount validateAvailableBalance-------: ");
        if (this.getBootcoinMovementType().equals("transfer-out")) {
            Double getBalance = (this.getAmount() != null ? this.getAmount() : 0);
            Double setBalance = (bootcoin.getBalance() != null ? bootcoin.getBalance() : 0) - getBalance;
            if (setBalance <= 0.0) {
                return Mono.error(new ResourceNotFoundException("Saldo", "Balance", getBalance.toString()));
            } else {
                this.setBalance(setBalance);
                return Mono.just(true);
            }
        } else if (this.getBootcoinMovementType().equals("input-transfer")) {
            this.setBalance((bootcoin.getBalance() != null ? bootcoin.getBalance() : 0.0) + this.getAmount());
            return Mono.just(true);
        } else {
            return Mono.error(new ResourceNotFoundException("Tipo movimiento", "BootcoinMovementType", this.getBootcoinMovementType()));
        }
    }

    @Override
    public Mono<BootcoinMovement> mapperToBootcoinMovement(Bootcoin bootcoin) {
        log.info("ini MovementAccount mapperToBootcoinMovement-------: ");
        LocalDateTime date = LocalDateTime.now();
        Bootcoin bc = bootcoin;
        bc.setBalance(null);
        BootcoinMovement bootcoinMovement = BootcoinMovement.builder()
                .bootcoinMovementType(this.getBootcoinMovementType())
                .amount(this.getAmount())
                .balance(this.getBalance())
                .currency(this.getCurrency())
                .bootcoinMovementDate(date)
                .documentNumber(this.getDocumentNumber())
                .bootcoin(bc)
                .build();
        log.info("fn MovementAccount mapperToBootcoinMovement-------: ");
        return Mono.just(bootcoinMovement);
    }
}