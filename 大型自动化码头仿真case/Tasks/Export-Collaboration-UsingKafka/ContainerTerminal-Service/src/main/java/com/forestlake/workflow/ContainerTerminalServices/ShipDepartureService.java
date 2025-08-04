package com.forestlake.workflow.ContainerTerminalServices;


import com.forestlake.workflow.ContainerTerminalServices.utils.PostMethodUtils;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ShipDepartureService {


    @Bean
    @ExternalTaskSubscription(topicName = "shipDeparture", lockDuration=50000)
    public ExternalTaskHandler shipDeparture() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://localhost:10000/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();

            System.out.println("业务编号:"+externalTask.getBusinessKey());

            System.out.println("码头：");
            System.out.println("收到海关放行通知");
            System.out.println("船驶离海关，完成出口，告知船代");
            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                PostMethodUtils.sendPost("Message_Ship_departure", externalTask.getBusinessKey(), "shipping-agency");
            } catch (IOException e) {
                e.printStackTrace();
            }
            externalTaskService.complete(externalTask);

        };
    }
}
