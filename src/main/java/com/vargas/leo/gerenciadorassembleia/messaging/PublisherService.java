package com.vargas.leo.gerenciadorassembleia.messaging;

import com.vargas.leo.gerenciadorassembleia.controller.response.FinishVotingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final RabbitTemplate template;

    public void insertOnQueue(FinishVotingResponse finishVotingResponse) {
        template.convertAndSend(CloudAMQPConfig.EXCHANGE_NAME, CloudAMQPConfig.EXCHANGE_NAME+".#", finishVotingResponse);
    }

}
