package com.forestlake.workflow.Collaboration_export;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("handleManifest")
public class handleManifestService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("生成订舱单");
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

        System.out.println("订舱单发送给海关、货代、集装箱码头");

//        runtimeService.correlateMessage("Message_Manifest_received");
//        runtimeService.correlateMessage("Message_CT_Manifest_received");
//        runtimeService.correlateMessage("Message_CB_Manifest_received");
    }
}
