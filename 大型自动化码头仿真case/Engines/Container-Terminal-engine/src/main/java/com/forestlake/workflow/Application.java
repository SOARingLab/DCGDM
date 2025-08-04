package com.forestlake.workflow;

import com.forestlake.workflow.ScheduleService.MessageEvent;
import net.sf.json.JSONObject;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;

import com.forestlake.workflow.ScheduleService.MessageEvent;
import com.forestlake.workflow.ScheduleService.MessageEventService;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController
@EnableScheduling
public class Application {

  @Autowired
  private IdentityService identityService;
  @Autowired
  private RuntimeService runtimeService;

  @Autowired
  private MessageEventService messageEventService;

  public static void main(String... args) {
    SpringApplication.run(Application.class, args);
  }



    @GetMapping("/start/{processKey}")
    public void start(@PathVariable(value = "processKey") String processKey) {
        identityService.setAuthenticatedUserId("admin");
        runtimeService.startProcessInstanceByKey(processKey);

    }

  @GetMapping("/start/{processKey}/{businessKey}")
  public void startWithBusinessKey(@PathVariable(value = "processKey") String processKey, @PathVariable(value = "businessKey") String businessKey) {
    identityService.setAuthenticatedUserId("admin");
    runtimeService.startProcessInstanceByKey(processKey, businessKey);

  }


  @GetMapping("/find-all")
  public List<MessageEventService.MessageWithVariables> findAll() {
    identityService.setAuthenticatedUserId("admin");
    return messageEventService.findAll();
  }

  @PostMapping(value = "/sendMessage")
  public void sendMessage(@RequestBody JSONObject params){

    System.out.println("收到消息");

    String messageName = params.getString("messageName");

    System.out.println(messageName);
    String businessKey = params.getString("businessKey");
    System.out.println(businessKey);

    messageEventService.sendMessage(messageName, businessKey, null);

    System.out.println(messageEventService.findAll().toString());

  }


}