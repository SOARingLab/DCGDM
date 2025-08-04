package com.forestlake.workflow.DepotServices;


import com.forestlake.workflow.DepotServices.utils.PostMethodUtils;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SendEmptyCTNtoTransportService {


    @Bean
    @ExternalTaskSubscription(topicName = "sendEmptyCTNtoTransport", lockDuration=50000)
    public ExternalTaskHandler sendEmptyCTNtoTransport() {


        return (externalTask, externalTaskService) -> {
            ExternalTaskClient client = ExternalTaskClient.create()
                    .baseUrl("http://localhost:10003/engine-rest")
                    .asyncResponseTimeout(10000) // long polling timeout
                    .build();

            System.out.println("业务编号:"+externalTask.getBusinessKey());

            System.out.println("场站：");
            System.out.println("收到船代的发箱请求");
            System.out.println("场站发送空的CTN给车队（发箱）");
            try {                Thread.currentThread().sleep(2000);
                PostMethodUtils.sendPost("Message_Transport_empty_CTN_received", externalTask.getBusinessKey(), "transport");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            externalTaskService.complete(externalTask);

        };
    }
}
