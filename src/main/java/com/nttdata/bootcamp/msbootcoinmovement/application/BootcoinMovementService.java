package com.nttdata.bootcamp.msbootcoinmovement.application;

import com.nttdata.bootcamp.msbootcoinmovement.dto.BootcoinMovementDto;
import com.nttdata.bootcamp.msbootcoinmovement.dto.bean.BootcoinMovementBean;
import com.nttdata.bootcamp.msbootcoinmovement.dto.bean.MovementAccount;
import com.nttdata.bootcamp.msbootcoinmovement.model.BootcoinMovement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Class BootcoinMovementService.
 * BootcoinMovement microservice class BootcoinMovementService.
 */
public interface BootcoinMovementService {
    public Flux<BootcoinMovement> findAll();
    public Mono<BootcoinMovement> findById(String idBootcoinMovement);
    public Mono<BootcoinMovement> save(BootcoinMovementBean movementDto);
    public Mono<BootcoinMovement> update(BootcoinMovementBean movementDto, String idBootcoinMovement);
    public Mono<Void> delete(String idBootcoinMovement);

}
