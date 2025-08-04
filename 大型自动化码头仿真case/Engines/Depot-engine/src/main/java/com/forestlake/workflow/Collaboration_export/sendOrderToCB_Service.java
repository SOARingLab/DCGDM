package com.forestlake.workflow.Collaboration_export;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("sendOrderToCB")
public class sendOrderToCB_Service implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("处理报关资料");
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();



//        runtimeService.correlateMessage("Message_BC_order_received");


    }
}