package com.forestlake.workflow.OwnerServices;


import com.forestlake.workflow.OwnerServices.utils.PostMethodUtils;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.IOException;

@Configuration
@EnableKafka
public class SendOrderToFreightForwardService {


    @Bean
    @ExternalTaskSubscription(topicName = "ownerSendOrderToFF", lockDuration=50000)
    public ExternalTaskHandler handleOwnerOrder() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://localhost:10005/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();






            System.out.println("业务编号:"+externalTask.getBusinessKey());


            System.out.println("货主：");
            System.out.println("货主给货代发送订单信息");

            try {
                PostMethodUtils.sendPost("Message_Owner_order_received", externalTask.getBusinessKey(), "freight-forward");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            externalTaskService.complete(externalTask);

        };
    }





}
