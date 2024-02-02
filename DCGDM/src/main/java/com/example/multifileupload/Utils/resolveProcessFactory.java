package com.example.multifileupload.Utils;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.MessageFlow;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import com.example.entity.Process;
public class resolveProcessFactory {

    /**
     * 获取指定字符串前的子字符串
     * @param input 输入字符串
     * @param target 指定字符串
     * @return 指定字符串前的子字符串
     */
    private static int getTargetIndex(String input, String target) {
        return input.indexOf(target);
    }

    private static String getSubStringBeforeTarget(String input, String target) {
        int targetIndex = getTargetIndex(input, target);
        return input.substring(0, targetIndex);
    }



    public static Process resolveProcess(File file){

        BpmnModelInstance modelInst;

        modelInst = Bpmn.readModelFromFile(file);

        Process process = new Process(getSubStringBeforeTarget(file.getName(), "."));
        Collection<MessageFlow> messageFlowList =  modelInst.getModelElementsByType(MessageFlow.class);
        Iterator<MessageFlow> messageFlowIterator = messageFlowList.iterator();


        while (messageFlowIterator.hasNext()){
            MessageFlow messageFlow = messageFlowIterator.next();

            if(messageFlow.getSource().getId().contains("Activity")){
                process.getMessageSent().add(messageFlow.getName());
            }else{
                process.getMessageReceived().add(messageFlow.getName());
            }

        }
        System.out.println(process.toString());

        return process;
    }
}
