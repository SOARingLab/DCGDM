package com.example.coordinator;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.impl.instance.camunda.CamundaPropertiesImpl;
import org.camunda.bpm.model.bpmn.instance.MessageFlow;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import com.example.entity.Process;
public class resolveProcessFactory {


    public static Process resolveProcess(String FILENAME_BASE, String FILE){
        String FILENAME = FILENAME_BASE+FILE;
        BpmnModelInstance modelInst;
        File file = new File(FILENAME);
        modelInst = Bpmn.readModelFromFile(file);

        Process process = new Process(FILE);
        Collection<MessageFlow> messageFlowList =  modelInst.getModelElementsByType(MessageFlow.class);
        Iterator<MessageFlow> messageFlowIterator = messageFlowList.iterator();


        while (messageFlowIterator.hasNext()){
            MessageFlow messageFlow = messageFlowIterator.next();

//
//            Iterator<ModelElementInstance> messageFlowPropertiesIterator = messageFlow.getExtensionElements().getElements().iterator();
//            while(messageFlowPropertiesIterator.hasNext()){
//                ModelElementInstance modelElementInstance = messageFlowPropertiesIterator.next();
//
//
//                System.out.println(modelElementInstance.getChildElementsByType(CamundaPropertyImpl.class));
//            }


            if(messageFlow.getExtensionElements()!=null) {
//            System.out.println(messageFlow.getExtensionElements().getUniqueChildElementByType(CamundaPropertiesImpl.class));
                CamundaPropertiesImpl camundaProperties = (CamundaPropertiesImpl) messageFlow.getExtensionElements().getUniqueChildElementByType(CamundaPropertiesImpl.class);
//            System.out.println(camundaProperties.getCamundaProperties());
                Iterator<CamundaProperty> prosIterator = camundaProperties.getCamundaProperties().iterator();
                while (prosIterator.hasNext()) {
                    System.out.println(prosIterator.next().getCamundaValue());
                }

            }
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
