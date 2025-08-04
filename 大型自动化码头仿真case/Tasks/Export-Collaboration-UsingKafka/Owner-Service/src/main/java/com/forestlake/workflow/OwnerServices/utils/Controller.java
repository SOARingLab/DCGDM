package com.forestlake.workflow.OwnerServices.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.sf.json.JSONObject;


@RestController
@Slf4j
public class Controller {
    public String sendMessageToKafka(String message, String businessKey){

        String params = "{ \"messageName\":\"" + message +  "\", " + " \"businessKey\":\"" + businessKey + "\"}";

        return  params;

    }
    @Autowired
    private KafkaTemplate<String, String> template;

    @RequestMapping("/springKafkaSend")
    public String send(@RequestBody JSONObject params) {


        String messageName = params.getString("messageName");
        String businessKey = params.getString("businessKey");
        String topicName = params.getString("topicName");


        String mes = sendMessageToKafka(messageName, businessKey);
        //将消息发送到Kafka服务器的名称为“one-more-topic”的Topic中
        this.template.send(topicName, mes );
        log.info("mes : {}", mes );
        return mes ;
    }
}
