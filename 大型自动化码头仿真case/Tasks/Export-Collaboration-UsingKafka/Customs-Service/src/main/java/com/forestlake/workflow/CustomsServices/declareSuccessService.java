package com.forestlake.workflow.CustomsServices;


import com.forestlake.workflow.CustomsServices.utils.PostMethodUtils;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


@Configuration
public class declareSuccessService {


    @Bean
    @ExternalTaskSubscription(topicName = "declareSuccess", lockDuration=50000)
    public ExternalTaskHandler declareSuccess() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://localhost:10002/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();

            System.out.println("业务编号:"+externalTask.getBusinessKey());

            System.out.println("海关：");
            System.out.println("收到报关行发来的申报信息");
            System.out.println("向报关行发送申报成功");

            try {
                Thread.currentThread().sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try {

                PostMethodUtils.sendPost("Message_declare_success_received", externalTask.getBusinessKey(), "customs-broker");
            } catch (IOException e) {
                e.printStackTrace();
            }

            externalTaskService.complete(externalTask);

        };
    }
}