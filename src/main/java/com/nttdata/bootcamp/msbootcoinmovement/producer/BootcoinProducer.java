package com.nttdata.bootcamp.msbootcoinmovement.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import com.nttdata.bootcamp.msbootcoinmovement.producer.mapper.BalanceBootcoinModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Class BankAccountProducer.
 * BootcoinMovement microservice class BankAccountProducer.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BootcoinProducer {
    private final KafkaTemplate<String, BalanceBootcoinModel> kafkaTemplate;

    @Value(value = "${spring.kafka.topic.bootcoin.name}")
    private String topic;

    public void sendMessage(BalanceBootcoinModel balanceModel) {

        ListenableFuture<SendResult<String, BalanceBootcoinModel>>
                future = kafkaTemplate.send(this.topic, balanceModel);

        future.addCallback(new ListenableFutureCallback<SendResult<String,
                BalanceBootcoinModel>>() {
            @Override
            public void onSuccess(SendResult<String, BalanceBootcoinModel> result) {
                log.info("Message {} has been sent ", balanceModel);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Something went wrong with the balanceModel {} ", balanceModel);
            }
        });
    }
}
