package com.forestlake.workflow.OwnerServices;


import com.forestlake.workflow.OwnerServices.utils.PostMethodUtils;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SendOutBoundCTNtoTransportService {


    @Bean
    @ExternalTaskSubscription(topicName = "sendOutBoundCTNtoTransport", lockDuration=50000)
    public ExternalTaskHandler sendOutBoundCTNtoTransport() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://localhost:10005/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();

            System.out.println("业务编号:"+externalTask.getBusinessKey());

            System.out.println("货主：");
            System.out.println("车队运来的空的CTN到达货主处");
            System.out.println("车队在货主处货物装箱");


            try {
                PostMethodUtils.sendPost("Message_Owner_Outbound_CTN_received", externalTask.getBusinessKey(),"transport");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            externalTaskService.complete(externalTask);

        };
    }





}
