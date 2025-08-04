package com.forestlake.workflow.Collaboration_export;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("sendOutBoundCTNtoTransport")
public class sendOutBoundCTNtoTransportService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("车队在货主处货物装箱");
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();


//        runtimeService.correlateMessage("Message_Owner_Outbound_CTN_received");


    }
}