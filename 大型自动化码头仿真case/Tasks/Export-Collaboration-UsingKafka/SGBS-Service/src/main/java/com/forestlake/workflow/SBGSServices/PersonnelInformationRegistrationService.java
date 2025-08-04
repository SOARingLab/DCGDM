package com.forestlake.workflow.SBGSServices;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonnelInformationRegistrationService {


    @Bean
    @ExternalTaskSubscription(topicName = "PersonnelInformationRegistration", lockDuration=50000)
    public ExternalTaskHandler PersonnelInformationRegistration() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://localhost:10006/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();



            System.out.println("业务编号:"+externalTask.getBusinessKey());


            System.out.println("边防：");
            System.out.println("收到船代发来的出海人员信息");
            System.out.println("出海人员信息登记");

            externalTaskService.complete(externalTask);

        };
    }

}
