package com.nttdata.bootcamp.msbootcoinmovement.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class BootcoinMovement.
 * BootcoinMovement microservice class BootcoinMovement.
 */
@Document(collection = "BootcoinMovement")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class BootcoinMovement {

    @Id
    private String idBootcoinMovement;
    private String documentNumber;
    private String cellphone;
    private String bootcoinMovementType;
    private Double amount;
    private Double balance;
    private String currency;
    private LocalDateTime bootcoinMovementDate;
    private Bootcoin bootcoin;

}