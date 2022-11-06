package com.nttdata.bootcamp.msbootcoinmovement.application;

import com.nttdata.bootcamp.msbootcoinmovement.dto.bean.BootcoinMovementBean;
import com.nttdata.bootcamp.msbootcoinmovement.exception.ResourceNotFoundException;
import com.nttdata.bootcamp.msbootcoinmovement.infrastructure.BootcoinRepository;
import com.nttdata.bootcamp.msbootcoinmovement.infrastructure.BootcoinMovementRepository;
import com.nttdata.bootcamp.msbootcoinmovement.model.Bootcoin;
import com.nttdata.bootcamp.msbootcoinmovement.model.BootcoinMovement;
import com.nttdata.bootcamp.msbootcoinmovement.producer.BootcoinProducer;
import com.nttdata.bootcamp.msbootcoinmovement.producer.mapper.BalanceBootcoinModel;
import com.nttdata.bootcamp.msbootcoinmovement.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class BootcoinMovementServiceImpl implements BootcoinMovementService {

    @Autowired
    private BootcoinMovementRepository bootcoinMovementRepository;
    @Autowired
    private BootcoinRepository bootcoinRepository;

    @Autowired
    private BootcoinProducer bootcoinProducer;

    @Override
    public Flux<BootcoinMovement> findAll() {
        return bootcoinMovementRepository.findAll();
    }

    @Override
    public Mono<BootcoinMovement> findById(String idBootcoinMovementCredit) {
        return Mono.just(idBootcoinMovementCredit)
                .flatMap(bootcoinMovementRepository::findById)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("BootcoinMovement", "IdBootcoinMovement", idBootcoinMovementCredit)));
    }

    @Override
    public Mono<BootcoinMovement> save(BootcoinMovementBean movementDto) {
        log.info("ini save------movementDto: " + movementDto.toString());
        return Mono.just(movementDto)
                .flatMap(mvDto -> validateAvailableBalance(mvDto)
                        .flatMap(bc -> mvDto.mapperToBootcoinMovement(bc)
                                .flatMap(mvt -> bootcoinMovementRepository.save(mvt))
                                .flatMap(mvt -> validateTransferBootcoin(movementDto).then(Mono.just(mvt)))
                                .flatMap(mvt -> {
                                    bootcoinProducer.sendMessage(mapperBootcoinBalanceModel(mvt.getIdBootcoinMovement(), mvt.getBalance()));
                                    return Mono.just(mvt);
                                })
                        )
                );
    }

    private BalanceBootcoinModel mapperBootcoinBalanceModel(String idBootcoin, Double balance) {

        BalanceBootcoinModel balanceModel = new BalanceBootcoinModel();
        balanceModel.setIdBootCoin(idBootcoin == null ? Constants.EMPTY_TEXT : idBootcoin);
        balanceModel.setBalance(balance);

        return balanceModel;
    }

    public Mono<Bootcoin> validateTransferBootcoin(BootcoinMovementBean bootcoinMovementBean) {
        log.info("ini validateTransfer-------0: " + bootcoinMovementBean.toString());
        if (bootcoinMovementBean.getBootcoinMovementType().equals("output-transfer")) { // transferencia de salida.
            log.info("1 validateTransfer-------output-transfer: ");
            return bootcoinRepository.findBootcoinByDocumentNumber(bootcoinMovementBean.getDocumentNumberForTransfer())
                    .switchIfEmpty(Mono.error(new ResourceNotFoundException("Bootcoin", "DocumentNumber", bootcoinMovementBean.getDocumentNumberForTransfer())))
                    .flatMap(ac -> {

                        BootcoinMovementBean mvDTO = bootcoinMovementBean;
                        mvDTO.setDocumentNumber(bootcoinMovementBean.getDocumentNumberForTransfer());
                        mvDTO.setBootcoinMovementType("input-transfer");
                        mvDTO.setAmount(bootcoinMovementBean.getAmount());
                        mvDTO.setCurrency(bootcoinMovementBean.getCurrency());
                        mvDTO.setDocumentNumberForTransfer(bootcoinMovementBean.getDocumentNumber());

                        return save(mvDTO)
                                .then(Mono.just(ac));
                    });
        } else if (bootcoinMovementBean.getBootcoinMovementType().equals("input-transfer")) {
            log.info("2 validateTransfer-------input-transfer: ");
            return bootcoinRepository.findBootcoinByDocumentNumber(bootcoinMovementBean.getDocumentNumberForTransfer())
                    .switchIfEmpty(Mono.error(new ResourceNotFoundException("Bootcoin", "DocumentNumber", bootcoinMovementBean.getDocumentNumberForTransfer())));
        } else {
            log.info("3 validateTransfer------- : ");
            return Mono.error(new ResourceNotFoundException("Movimiento de bootcoin", "BootcoinMovementType", bootcoinMovementBean.getBootcoinMovementType()));
        }
    }

    public Mono<Bootcoin> validateAvailableBalance(BootcoinMovementBean movementDto) {
        log.info("ini validateAvailableBalance-------: ");
        return bootcoinRepository.findBootcoinByDocumentNumber(movementDto.getDocumentNumber())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Bootcoin", "DocumentNumber", movementDto.getDocumentNumber())))
                .doOnNext(d -> log.info("-- findBootcoinByDocumentNumber-------d: " + movementDto.getDocumentNumber() + " -- " + d.toString()))
                .flatMap(bcn -> movementDto.validateAvailableBalance(bcn)
                        .then(Mono.just(bcn))
                );
    }

    @Override
    public Mono<BootcoinMovement> update(BootcoinMovementBean movementDto, String idBootcoinMovement) {
        return null;/*
        return Mono.just(movementDto)
                .flatMap(mvDto -> validateAvailableBalance(mvDto))
                .flatMap(mvDto -> mvDto.mapperToBootcoinMovement(null))
                .flatMap(mvt -> bootcoinMovementRepository.findById(idBootcoinMovement)
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("BootcoinMovement", "IdBootcoinMovement", idBootcoinMovement)))
                        .flatMap(c -> {
                            mvt.setIdBootcoinMovement(idBootcoinMovement);
                            return bootcoinMovementRepository.save(c);
                        }))
                .flatMap(mvt -> validateTransferBootcoin(movementDto).then(Mono.just(mvt)))
                .flatMap(mvt -> {
                    bootcoinProducer.sendMessage(mapperBootcoinBalanceModel(mvt.getIdBootcoinMovement(), mvt.getBalance()));
                    return Mono.just(mvt);
                });*/

    }

    @Override
    public Mono<Void> delete(String idBootcoinMovement) {
        return bootcoinMovementRepository.findById(idBootcoinMovement)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("BootcoinMovement", "IdBootcoinMovement", idBootcoinMovement)))
                .flatMap(bootcoinMovementRepository::delete);
    }

}
