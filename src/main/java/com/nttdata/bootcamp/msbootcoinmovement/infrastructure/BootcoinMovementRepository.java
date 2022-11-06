package com.nttdata.bootcamp.msbootcoinmovement.infrastructure;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.nttdata.bootcamp.msbootcoinmovement.dto.BootcoinMovementDto;
import com.nttdata.bootcamp.msbootcoinmovement.model.BootcoinMovement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Class BootcoinMovementRepository.
 * BootcoinMovement microservice class BootcoinMovementRepository.
 */
public interface BootcoinMovementRepository extends ReactiveMongoRepository<BootcoinMovement, String> {
    @Aggregation(pipeline = {"{ '$match': { 'accountNumber' : ?0 } }",
            "{ '$sort' : { 'movementDate' : -1 } }", "{'$limit': 1}"})
    Mono<BootcoinMovementDto> findLastBootcoinMovementByAccount(String accountNumber);

    @Aggregation(pipeline = {"{ '$match': { 'accountNumber' : ?0, 'idBootcoinMovement' : { $ne: ?1 } } }", "{ '$sort' : { 'movementDate' : -1 } }", "{'$limit': 1}"})
    Mono<BootcoinMovementDto> findLastBootcoinMovementByAccountExceptCurrentId(String accountNumber, String idBootcoinMovement);

    @Aggregation(pipeline = {"{ '$match': { 'accountNumber' : ?0 } }", "{ '$sort' : { 'movementDate' : -1 } }"})
    Flux<BootcoinMovementDto> findBootcoinMovementsByAccount(String accountNumber);

    @Query(value = "{$and:[{'movementDate':{$gte:  { '$date' : ?0} }},{'movementDate': {$lte:  { '$date' : ?1} }}],'accountNumber':?2}")
    Flux<BootcoinMovement> findBootcoinMovementsByDateRange(String iniDate, String finalDate, String accountNumber);

    @Aggregation(pipeline = {"{ '$match': { 'credit.creditNumber' : ?0 } }", "{ '$sort' : { 'movementDate' : -1 } }", "{'$limit': 1}"})
    public Mono<BootcoinMovement> findByCreditNumber(Integer creditNumber);

    @Aggregation(pipeline = {"{ '$match': { 'loan.loanNumber' : ?0 } }", "{ '$sort' : { 'movementDate' : -1 } }"})
    Flux<BootcoinMovementDto> findBootcoinMovementsByLoanNumber(String loanNumber);

    @Aggregation(pipeline = {"{ '$match': { 'credit.creditNumber' : ?0 } }", "{ '$sort' : { 'movementDate' : -1 } }"})
    Flux<BootcoinMovementDto> findBootcoinMovementsByCreditNumber(String creditNumber);

}