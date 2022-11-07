package com.nttdata.bootcamp.msbootcoinmovement.controller;

import com.nttdata.bootcamp.msbootcoinmovement.application.BootcoinMovementService;
import com.nttdata.bootcamp.msbootcoinmovement.dto.BootcoinMovementDto;
import com.nttdata.bootcamp.msbootcoinmovement.dto.bean.MobileWalletMovement;
import com.nttdata.bootcamp.msbootcoinmovement.dto.bean.MovementAccount;
import com.nttdata.bootcamp.msbootcoinmovement.model.BootcoinMovement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bootcoinmovements")
@Slf4j
public class BootcoinMovementController {
    @Autowired
    private BootcoinMovementService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<BootcoinMovement>>> listBootcoinMovements() {
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(service.findAll()));
    }

    @GetMapping("/{idBootcoinMovement}")
    public Mono<ResponseEntity<BootcoinMovement>> getBootcoinMovementsDetails(@PathVariable("idBootcoinMovement") String idBootcoinMovement) {
        return service.findById(idBootcoinMovement).map(c -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<BootcoinMovement>> saveBootcoinMovement(@Valid @RequestBody Mono<MovementAccount> movementAccount) {
        return movementAccount.flatMap(mvDto -> service.save(mvDto)
                .doOnNext(s -> log.info("--saveBootcoinMovement-------s : " + s))
                .map(c -> ResponseEntity.created(URI.create("/api/bootcoinmovements/".concat(c.getIdBootcoinMovement())))
                        .contentType(MediaType.APPLICATION_JSON).body(c)
                )
        );
    }

    @PutMapping("/{idBootcoinMovement}")
    public Mono<ResponseEntity<BootcoinMovement>> editBootcoinMovement(@Valid @RequestBody MovementAccount movementDto, @PathVariable("idBootcoinMovement") String idBootcoinMovement) {
        return service.update(movementDto, idBootcoinMovement)
                .map(c -> ResponseEntity.created(URI.create("/api/bootcoinmovements/".concat(idBootcoinMovement)))
                        .contentType(MediaType.APPLICATION_JSON).body(c));
    }

    @PostMapping("/mobileWallet")
    public Mono<ResponseEntity<Map<String, Object>>> saveBootcoinMovementMobileWallet(@Valid @RequestBody Mono<MobileWalletMovement> mobileWalletMovement) {
        Map<String, Object> request = new HashMap<>();
        return mobileWalletMovement.flatMap(mvDto ->
                service.save(mvDto).map(c -> {
                    request.put("Movimiento Bootcoin", c);
                    request.put("mensaje", "Movimiento de Bootcoin guardado con exito");
                    request.put("timestamp", new Date());
                    return ResponseEntity.created(URI.create("/api/bootcoinmovements/".concat(c.getIdBootcoinMovement())))
                            .contentType(MediaType.APPLICATION_JSON).body(request);
                })
        );
    }

    @PutMapping("/mobileWallet/{idBootcoinMovement}")
    public Mono<ResponseEntity<BootcoinMovement>> editBootcoinMovementMobileWallet(@Valid @RequestBody MobileWalletMovement movementDto, @PathVariable("idBootcoinMovement") String idBootcoinMovement) {
        return service.update(movementDto, idBootcoinMovement)
                .map(c -> ResponseEntity.created(URI.create("/api/bootcoinmovements/".concat(idBootcoinMovement)))
                        .contentType(MediaType.APPLICATION_JSON).body(c));
    }

    @DeleteMapping("/{idBootcoinMovement}")
    public Mono<ResponseEntity<Void>> deleteBootcoinMovement(@PathVariable("idBootcoinMovement") String idBootcoinMovement) {
        return service.delete(idBootcoinMovement).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
    }
}
