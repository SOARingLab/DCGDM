package com.forestlake.workflow.FreightForwarderServices;


import com.forestlake.workflow.FreightForwarderServices.utils.PostMethodUtils;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SendSOtoSAService {


    @Bean
    @ExternalTaskSubscription(topicName = "sendSOtoSA", lockDuration=50000)
    public ExternalTaskHandler sendSOtoSA() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://localhost:10004/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();

            System.out.println("业务编号:"+externalTask.getBusinessKey());

            System.out.println("货代：");
            System.out.println("收到货主提交的订单");
            System.out.println("处理S/O，将其发送给船代");
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                PostMethodUtils.sendPost("Message_SO_received", externalTask.getBusinessKey(), "shipping-agency");
            } catch (IOException e) {
                e.printStackTrace();
            }

            externalTaskService.complete(externalTask);

        };
    }
}
