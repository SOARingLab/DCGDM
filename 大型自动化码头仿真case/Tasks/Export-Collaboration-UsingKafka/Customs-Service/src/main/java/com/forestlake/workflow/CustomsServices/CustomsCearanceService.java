package com.forestlake.workflow.CustomsServices;


import com.forestlake.workflow.CustomsServices.utils.PostMethodUtils;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class CustomsCearanceService {


    @Bean
    @ExternalTaskSubscription(topicName = "CustomsCearance", lockDuration=50000)
    public ExternalTaskHandler CustomsCearance() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://localhost:10002/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();

            System.out.println("业务编号:"+externalTask.getBusinessKey());

            System.out.println("海关：");
            System.out.println("海关批准放行，告知报关行与码头");
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                PostMethodUtils.sendPost("Message_CB_customs_cearance", externalTask.getBusinessKey(), "customs-broker");
                PostMethodUtils.sendPost("Message_CT_customs_cearance", externalTask.getBusinessKey(), "container-terminal");
            } catch (IOException e) {
                e.printStackTrace();
            }

            externalTaskService.complete(externalTask);

        };
    }
}
