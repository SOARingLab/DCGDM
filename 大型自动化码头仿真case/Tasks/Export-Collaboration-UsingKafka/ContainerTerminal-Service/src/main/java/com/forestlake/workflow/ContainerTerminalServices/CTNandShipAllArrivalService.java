package com.forestlake.workflow.ContainerTerminalServices;


import com.forestlake.workflow.ContainerTerminalServices.utils.PostMethodUtils;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class CTNandShipAllArrivalService {


    @Bean
    @ExternalTaskSubscription(topicName = "CTNandShipAllArrival", lockDuration=50000)
    public ExternalTaskHandler CTNandShipAllArrival() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://localhost:10000/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();

            System.out.println("业务编号:"+externalTask.getBusinessKey());

            System.out.println("码头：");
            System.out.println("码头告知海关船已到达海关");
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                PostMethodUtils.sendPost("Message_CTN_and_ship_arrive", externalTask.getBusinessKey(), "customs");
            } catch (IOException e) {
                e.printStackTrace();
            }
            externalTaskService.complete(externalTask);

        };
    }
}
