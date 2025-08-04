package com.forestlake.workflow.ShippingAgencyServices;


import com.forestlake.workflow.ShippingAgencyServices.utils.PostMethodUtils;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class AskDepotForCTNService  {


    @Bean
    @ExternalTaskSubscription(topicName = "askDepotForCTN", lockDuration=50000)
    public ExternalTaskHandler askDepotForCTN() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://localhost:10007/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();

            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {

                System.out.println("业务编号:"+externalTask.getBusinessKey());

                System.out.println("船代：");
                System.out.println("向场站要求发箱");
                PostMethodUtils.sendPost("Message_ask_for_CTN", externalTask.getBusinessKey(), "depot");

            } catch (IOException e) {
                e.printStackTrace();
            }

            externalTaskService.complete(externalTask);

        };
    }

}
