package com.forestlake.workflow.Collaboration_export;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("AppointForInspection")
public class AppointForInspectionService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("报关行向海关预约查验信息");
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

//        runtimeService.correlateMessage("Message_Appointment_received");


    }
}