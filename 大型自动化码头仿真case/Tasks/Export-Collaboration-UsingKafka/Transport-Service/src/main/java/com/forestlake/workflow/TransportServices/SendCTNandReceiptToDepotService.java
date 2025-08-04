package com.forestlake.workflow.TransportServices;


import com.forestlake.workflow.TransportServices.utils.PostMethodUtils;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SendCTNandReceiptToDepotService {


    @Bean
    @ExternalTaskSubscription(topicName = "sendCTNandReceiptToDepot", lockDuration=50000)
    public ExternalTaskHandler sendCTNandReceiptToDepot() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://localhost:10008/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();

            System.out.println("业务编号:"+externalTask.getBusinessKey());

            System.out.println("车队：");
            System.out.println("车队在货主处装箱完成，将出口货物送抵从场站，并交接设备交接单");
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                PostMethodUtils.sendPost("Message_outbound_CTN_and_Receipt_received", externalTask.getBusinessKey(), "depot");

            } catch (IOException e) {
                e.printStackTrace();
            }

            externalTaskService.complete(externalTask);

        };
    }
}
