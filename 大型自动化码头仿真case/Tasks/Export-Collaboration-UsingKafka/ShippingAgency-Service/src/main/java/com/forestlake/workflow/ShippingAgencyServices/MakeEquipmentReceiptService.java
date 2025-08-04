package com.forestlake.workflow.ShippingAgencyServices;


import com.forestlake.workflow.ShippingAgencyServices.utils.PostMethodUtils;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class MakeEquipmentReceiptService  {


    @Bean
    @ExternalTaskSubscription(topicName = "makeEquipmentReceipt", lockDuration=50000)
    public ExternalTaskHandler makeEquipmentReceipt() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://localhost:10007/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();

            System.out.println("业务编号:"+externalTask.getBusinessKey());

            System.out.println("船代：");
            System.out.println("生成设备交接单");
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                System.out.println("船代给货代发送设备交接单");
                PostMethodUtils.sendPost("Message_SA_Equipment_Receipt_received", externalTask.getBusinessKey(), "freight-forward");

            } catch (IOException e) {
                e.printStackTrace();
            }

            externalTaskService.complete(externalTask);

        };
    }

}
