package com.forestlake.workflow.ContainerTerminalServices;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadCTNService {


    @Bean
    @ExternalTaskSubscription(topicName = "loadCTN", lockDuration=50000)
    public ExternalTaskHandler loadCTN() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://localhost:10000/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();

            System.out.println("业务编号:"+externalTask.getBusinessKey());

            System.out.println("码头：");
            System.out.println("收到船代发来的设备交接单，船已经到达码头(船代告知)，场站将出口货物CTN送达码头");
            System.out.println("在码头装载出口货物CTN上船");


            externalTaskService.complete(externalTask);

        };
    }
}
