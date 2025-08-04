package com.forestlake.workflow.OwnerServices;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentService {


    @Bean
    @ExternalTaskSubscription(topicName = "payment", lockDuration=50000)
    public ExternalTaskHandler payment() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://localhost:10005/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();

            System.out.println("业务编号:"+externalTask.getBusinessKey());

            System.out.println("货主：");
            System.out.println("收到船代的付款请求");
            System.out.println("货主付款");



            externalTaskService.complete(externalTask);

        };
    }





}
