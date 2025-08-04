package com.forestlake.workflow.ShippingAgencyServices;


import com.forestlake.workflow.ShippingAgencyServices.utils.PostMethodUtils;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class HandleManifestService  {


    @Bean
    @ExternalTaskSubscription(topicName = "handleManifest", lockDuration=50000)
    public ExternalTaskHandler handleManifest() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://localhost:10007/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();

            System.out.println("业务编号:"+externalTask.getBusinessKey());

            System.out.println("船代：");
            System.out.println("收到货代发来的S/O");
            System.out.println("生成订舱单");

            try {
                System.out.println("订舱单发送给海关、货代、集装箱码头");



                Thread.currentThread().sleep(2000);
                PostMethodUtils.sendPost("Message_FF_Manifest_received", externalTask.getBusinessKey(), "freight-forward");
                PostMethodUtils.sendPost("Message_CB_Manifest_received", externalTask.getBusinessKey(), "customs");
                PostMethodUtils.sendPost("Message_CT_Manifest_received", externalTask.getBusinessKey(), "container-terminal");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            externalTaskService.complete(externalTask);

        };
    }

}
