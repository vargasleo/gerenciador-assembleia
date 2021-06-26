package com.vargas.leo.gerenciadorassembleia.messaging;

import com.vargas.leo.gerenciadorassembleia.controller.response.FinishVotingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ListenerService {

    @RabbitListener(queues = CloudAMQPConfig.QUEUE_NAME)
    public void listenMessage(FinishVotingResponse finishVotingResponse) {
        log.info("voting session " + finishVotingResponse.getId() + " has ended");
        log.info("subject is " + finishVotingResponse.getAgendaSubject());
        log.info("result is " + finishVotingResponse.getWinnerOption());
        throw new AmqpRejectAndDontRequeueException("simulating failure to requeue to dlq");
    }

    @RabbitListener(queues = CloudAMQPConfig.DLQ_NAME)
    public void sendErrorEmail(FinishVotingResponse finishVotingResponse) {
        log.info("message received in " + CloudAMQPConfig.DLQ_NAME);
    }
}
