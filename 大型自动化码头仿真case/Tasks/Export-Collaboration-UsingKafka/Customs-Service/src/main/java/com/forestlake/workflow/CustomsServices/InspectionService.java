package com.forestlake.workflow.CustomsServices;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InspectionService {


    @Bean
    @ExternalTaskSubscription(topicName = "inspection", lockDuration=50000)
    public ExternalTaskHandler inspection() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://blade01.soaringlab.top:10002/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();

            System.out.println("业务编号:"+externalTask.getBusinessKey());

            System.out.println("海关：");
            System.out.println("检疫完成后，检验出口货物");


            externalTaskService.complete(externalTask);

        };
    }
}
