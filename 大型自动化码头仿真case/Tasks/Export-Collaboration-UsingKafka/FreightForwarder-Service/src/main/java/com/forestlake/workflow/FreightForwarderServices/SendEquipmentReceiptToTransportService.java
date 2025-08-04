package com.forestlake.workflow.FreightForwarderServices;


import com.forestlake.workflow.FreightForwarderServices.utils.PostMethodUtils;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SendEquipmentReceiptToTransportService {


    @Bean
    @ExternalTaskSubscription(topicName = "sendEquipmentReceiptToTransport", lockDuration=50000)
    public ExternalTaskHandler sendEquipmentReceiptToTransport() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://localhost:10004/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();

            System.out.println("业务编号:"+externalTask.getBusinessKey());

            System.out.println("货代：");
            System.out.println("收到船代的订舱单和设备交接单");
            System.out.println("货代交给车队设备交接单");
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                PostMethodUtils.sendPost("Message_FF_Equipment_Receipt_received", externalTask.getBusinessKey(), "transport");
            } catch (IOException e) {
                e.printStackTrace();
            }

            externalTaskService.complete(externalTask);

        };
    }





}
