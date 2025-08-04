package com.forestlake.workflow.Collaboration_export;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("issueExpenseNotetoOwner")
public class issueExpenseNotetoOwnerService implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) throws Exception {

        System.out.println("向货主开费用单");
        RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
//        runtimeService.correlateMessage("Message_expense_note_received");

    }
}