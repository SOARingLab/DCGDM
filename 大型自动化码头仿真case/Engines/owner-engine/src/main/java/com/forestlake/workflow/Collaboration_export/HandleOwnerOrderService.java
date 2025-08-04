package com.forestlake.workflow.Collaboration_export;


import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("handleOwnerOrder")
public class HandleOwnerOrderService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String telephone = (String)execution.getVariable("telephone");

        System.out.println("处理货主的订单，货主电话"+telephone);
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();

//        runtimeService.correlateMessage("Message_Owner_order_received ");

    }
}
