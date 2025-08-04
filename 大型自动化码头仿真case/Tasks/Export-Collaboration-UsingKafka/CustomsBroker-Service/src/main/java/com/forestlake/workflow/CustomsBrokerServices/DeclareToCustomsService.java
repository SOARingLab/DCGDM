package com.forestlake.workflow.CustomsBrokerServices;


import com.forestlake.workflow.CustomsBrokerServices.utils.PostMethodUtils;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class DeclareToCustomsService{


    @Bean
    @ExternalTaskSubscription(topicName = "declareToCustoms", lockDuration=50000)
    public ExternalTaskHandler declareToCustoms() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://localhost:10001/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();

            System.out.println("业务编号:"+externalTask.getBusinessKey());

            System.out.println("报关行：");
            System.out.println("报关行向海关申报信息");

            try {                Thread.currentThread().sleep(2000);
                PostMethodUtils.sendPost("Message_declaration_received", externalTask.getBusinessKey(), "customs");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            externalTaskService.complete(externalTask);

        };
    }





}
