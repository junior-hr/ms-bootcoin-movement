package com.nttdata.bootcamp.msbootcoinmovement.dto;

import com.nttdata.bootcamp.msbootcoinmovement.exception.ResourceNotFoundException;
import com.nttdata.bootcamp.msbootcoinmovement.model.BootcoinMovement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@ToString
@Builder
public class BootcoinMovementDto {

    @Id
    private String idBootcoinMovement;

    private String accountNumber;

    private String debitCardNumber;

    private Integer numberBootcoinMovement;

    private Integer creditNumber;

    private Integer loanNumber;

    private String cellphone;

    @NotEmpty(message = "no debe estar vacío")
    private String bootcoinMovementType;

    @NotNull(message = "no debe estar nulo")
    private Double amount;

    private Double balance;

    @NotEmpty(message = "no debe estar vacio")
    private String currency;

    private Double commission;

    private String accountNumberForTransfer;

    public Mono<Boolean> validateBootcoinMovementType() {
        log.info("ini validateBootcoinMovementType-------: ");
        return Mono.just(this.getBootcoinMovementType()).flatMap(mt -> {
            log.info("1 validateBootcoinMovementType-------this.getBootcoinMovementType() -> mt: " + mt);
            log.info("--validateBootcoinMovementType-------this.getAccountNumberForTransfer(): " + (this.getAccountNumberForTransfer() == null ? "" : this.getAccountNumberForTransfer()));
            if (mt.equals("deposit")) { // deposito.
                log.info("--validateBootcoinMovementType-------deposit: ");
                return Mono.just(true);
            } else if (mt.equals("withdrawal")) { // retiro.
                log.info("--validateBootcoinMovementType-------withdrawal: ");
                return Mono.just(true);
            } else if (mt.equals("input-transfer")) { // transferencia de entrada.
                log.info("--validateBootcoinMovementType-------input-transfer: ");
                if (this.getAccountNumberForTransfer() == null) {
                    return Mono.error(new ResourceNotFoundException("Número de Cuenta para transferencia", "AccountNumberForTransfer", ""));
                }
                return Mono.just(true);
            } else if (mt.equals("output-transfer")) { // transferencia de salida.
                log.info("--validateBootcoinMovementType-------output-transfer: ");
                if (this.getAccountNumberForTransfer() == null) {
                    return Mono.error(new ResourceNotFoundException("Número de Cuenta para transferencia", "AccountNumberForTransfer", ""));
                }
                return Mono.just(true);
            } else if (mt.equals("payment")) { // pago.
                log.info("--validateBootcoinMovementType-------payment: ");
                return Mono.just(true);
            } else {
                log.info("--validateBootcoinMovementType-------no getBootcoinMovementType: ");
                return Mono.error(new ResourceNotFoundException("Tipo movimiento", "getBootcoinMovementType", this.getBootcoinMovementType()));
            }
        });
    }

    public Mono<Boolean> validateBootcoinMovementTypeCreditLoan() {
        log.info("Inicio validateBootcoinMovementTypeCreditLoan-------: ");
        return Mono.just(this.getBootcoinMovementType()).flatMap(ct -> {
            Boolean isOk = false;
            if (this.getBootcoinMovementType().equals("payment")) { // pago.
                log.info("Fin validateBootcoinMovementTypeCreditLoan-------: ");
                return this.validateCreditCardAndLoanPayment();
            } else if (this.getBootcoinMovementType().equals("consumption")) { // consumo.
                log.info("Fin validateBootcoinMovementTypeCreditLoan-------: ");
                return this.validateCreditCardAndLoanConsumption();
                //return Mono.just(isOk);
            } else {
                return Mono.error(new ResourceNotFoundException("Tipo movimiento", "getBootcoinMovementType", this.getBootcoinMovementType()));
            }
        });
    }

    public Mono<Boolean> validateCreditCardAndLoanPayment() { //Validar Pago de Producto de Credito
        log.info("Inicio validateCreditCardAndLoanPayment-------: ");
        return Mono.just(this.getBalance()).flatMap(ct -> {
            Boolean isOk = false;
            if (this.getBalance() > 0.0) {
                if (this.getAmount() <= this.getBalance()) {
                    isOk = true;
                    //this.setBalance(this.getBalance()-this.getAmount());
                } else {
                    return Mono.error(new ResourceNotFoundException("Monto de movimiento de credito(Pago) supera el saldo por pagar"));
                }
            } else {
                return Mono.error(new ResourceNotFoundException("Movimiento Credito(Pago) no se puede realizar porque no tiene Saldo por pagar"));
            }
            log.info("Fin validateCreditCardAndLoanPayment-------: ");
            return Mono.just(isOk);
        });
    }

    public Mono<Boolean> validateCreditCardAndLoanConsumption() { //Validar consumo de saldo de limite de Credito
        log.info("Inicio validateCreditCardAndLoanConsumption-------: ");
        return Mono.just(this.getBalance()).flatMap(ct -> {
            Boolean isOk = false;
            if (this.getAmount() <= this.getBalance()) {
                isOk = true;
            } else {
                return Mono.error(new ResourceNotFoundException("Movimiento de credito(Consumo) supera tu saldo de tu linea de credito"));
            }
            log.info("Fin validateCreditCardAndLoanConsumption-------: ");
            return Mono.just(isOk);
        });
    }

    public Mono<BootcoinMovement> MapperToBootcoinMovement() {
        LocalDateTime date = LocalDateTime.now();
        log.info("ini validateBootcoinMovementLimit-------: LocalDateTime.now()" + LocalDateTime.now());
        log.info("ini validateBootcoinMovementLimit-------date: " + date);

        BootcoinMovement movement = BootcoinMovement.builder()
                .idBootcoinMovement(this.getIdBootcoinMovement())
                .bootcoinMovementType(this.getBootcoinMovementType())
                .amount(this.getAmount())
                .balance(this.getBalance())
                .currency(this.getCurrency())
                .bootcoinMovementDate(date)
                .cellphone(this.getCellphone())
                //.idCredit(this.getIdCredit())
                //.idBankAccount(this.getIdBankAccount())
                //.idLoan(this.getIdLoan())
                .build();

        return Mono.just(movement);
    }
}
