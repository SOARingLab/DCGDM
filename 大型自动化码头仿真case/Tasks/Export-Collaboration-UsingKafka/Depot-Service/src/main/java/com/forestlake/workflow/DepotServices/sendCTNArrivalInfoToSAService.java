package com.forestlake.workflow.DepotServices;


import com.forestlake.workflow.DepotServices.utils.PostMethodUtils;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class sendCTNArrivalInfoToSAService {


    @Bean
    @ExternalTaskSubscription(topicName = "send_CTN_arrival_info_to_SA", lockDuration=50000)
    public ExternalTaskHandler send_CTN_arrival_info_to_SA() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://localhost:10003/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();

            System.out.println("业务编号:"+externalTask.getBusinessKey());

            System.out.println("场站：");
            System.out.println("空的CTN已送达车队");
            System.out.println("告知船代空的CTN已送达车队");
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                PostMethodUtils.sendPost("Message_empty_CTN_arrival", externalTask.getBusinessKey(), "shipping-agency");

            } catch (IOException e) {
                e.printStackTrace();
            }

            externalTaskService.complete(externalTask);

        };
    }
}
