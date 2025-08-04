package com.forestlake.workflow.Collaboration_export;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("MakeEquipmentReceipt")
public class MakeEquipmentReceiptService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("生成设备交接单");
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

        System.out.println("发送货代设备交接单");
//        runtimeService.correlateMessage("Message_SA_Equipment_Receipt_received");


    }
}
