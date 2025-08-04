package com.forestlake.workflow.CustomsServices;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CIQService {


    @Bean
    @ExternalTaskSubscription(topicName = "CIQ", lockDuration=50000)
    public ExternalTaskHandler CIQ() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://localhost:10002/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();

            System.out.println("业务编号:"+externalTask.getBusinessKey());

            System.out.println("海关：");
            System.out.println("收到船代发来的订舱单");
            System.out.println("收到报关行的预约信息");
            System.out.println("码头过来的船和出口货物已到达");
            System.out.println("进行检疫");


            externalTaskService.complete(externalTask);

        };
    }
}
