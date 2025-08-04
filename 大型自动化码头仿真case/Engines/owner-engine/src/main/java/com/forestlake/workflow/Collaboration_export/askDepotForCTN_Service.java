package com.forestlake.workflow.Collaboration_export;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("askDepotForCTN")
public class askDepotForCTN_Service implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("向场站要求发箱");
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

//        runtimeService.correlateMessage("Message_ask_for_CTN");


    }
}