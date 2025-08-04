package com.forestlake.workflow.DepotServices;


import com.forestlake.workflow.DepotServices.utils.PostMethodUtils;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class sendOutboundCTNtoCTService {


    @Bean
    @ExternalTaskSubscription(topicName = "sendOutboundCTNtoCT", lockDuration=50000)
    public ExternalTaskHandler sendOutboundCTNtoCT() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://localhost:10003/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();

            System.out.println("业务编号:"+externalTask.getBusinessKey());
            System.out.println("场站：");
            System.out.println("收到车队运来的出口货物CTN");
            System.out.println("将场站的出口货物CTN被调到码头");
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try {
                PostMethodUtils.sendPost("Message_depot_outbound_CTN_received", externalTask.getBusinessKey(), "container-terminal");

            } catch (IOException e) {
                e.printStackTrace();
            }

            externalTaskService.complete(externalTask);

        };
    }
}
