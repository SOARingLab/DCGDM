package com.forestlake.workflow.ShippingAgencyServices;


import com.forestlake.workflow.ShippingAgencyServices.utils.PostMethodUtils;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class IssueExpenseNotetoOwnerService{


    @Bean
    @ExternalTaskSubscription(topicName = "issueExpenseNotetoOwner", lockDuration=50000)
    public ExternalTaskHandler issueExpenseNotetoOwner() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://localhost:10007/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();

            System.out.println("业务编号:"+externalTask.getBusinessKey());

            System.out.println("船代：");
            System.out.println("向货主开费用单");
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {

                PostMethodUtils.sendPost("Message_expense_note_received", externalTask.getBusinessKey(), "owner");

            } catch (IOException e) {
                e.printStackTrace();
            }

            externalTaskService.complete(externalTask);

        };
    }

}
