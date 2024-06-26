package com.example.multifileupload.controller;

import cn.hutool.json.JSONUtil;
import com.example.combiner.BpmnPanel;
import com.example.coordinator.combineProcessFactory;
import com.example.coordinator.getFileNamesFactory;
import com.example.coordinator.resolveProcessFactory;
import com.example.entity.Process;
import com.example.multifileupload.Utils.TransformMultipartFile2File;

import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import java.io.File;

import java.io.FileWriter;

import java.io.BufferedWriter;

import java.io.IOException;

/**
 * @author zhanghaopeng04
 * Date 2022/11/17
 */
@RestController
@CrossOrigin
public class FileController {

    @PostMapping("/upload")
    public List<String> upload(MultipartFile[] files) throws IOException {
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        System.out.println("files: "+files.length);

//        BpmnPanel bpmnPanel = new BpmnPanel();
        for (int i = 0; i < files.length; i++) {
            if(files[i].getOriginalFilename().contains(".bpmn")){
//                resolveProcessFactory.resolveProcess(
//                        TransformMultipartFile2File.transform(files[i]));

//                System.out.println((TransformMultipartFile2File.transform(files[i])).toString());
//                bpmnPanel.addBpmnInfo(TransformMultipartFile2File.transform(files[i]));


                System.out.println("testurl"+ResourceUtils.getURL("classpath:").getPath());
                String baseurl = ResourceUtils.getURL("classpath:").getPath();
                // 新的文件名，使用uuid生成文件名
                String fileName = files[i].getOriginalFilename();
                // 创建新的文件
                File fileExist = new File(baseurl+"src/main/resources");
                // 文件夹不存在，则新建
                if (!fileExist.exists()) {
                    fileExist.mkdirs();
                }
                // 获取文件对象
                File file = new File(baseurl+"src/main/resources", fileName);
                // 完成文件的上传
                files[i].transferTo(file);
            }
        }
//        bpmnPanel.sortParticipantHoldersByWidth();
//        bpmnPanel.initEnvironmentWidth();
//        bpmnPanel.confirmParticipantPositionAndSize();
//        bpmnPanel.drawParticipant();
//        bpmnPanel.drawMessageFlow();
//        bpmnPanel.testXml();
        List<String> list = Arrays.stream(files).map(MultipartFile::getOriginalFilename).collect(Collectors.toList());
        System.out.println("文件上传成功 文件名为：" + JSONUtil.toJsonStr(list));
        return list;
    }
    @GetMapping("/getCombine")
    public String getCombine(@RequestParam List<String> values) throws IOException {
        String str = "test";
//        File folder = new File(ResourceUtils.getURL("classpath:").getPath()+"src/main/resources");
        File folder = new File("src/main/resources");
        File[] listOfFiles = folder.listFiles();
        BpmnPanel bpmnPanel = new BpmnPanel();
        for (File file : listOfFiles) {
//            if (file.isFile() && file.getName().contains(".bpmn") && values.contains(file.getName())) {
            if(file.getName().equals("a.bpmn") || file.getName().equals("b.bpmn")){
                System.out.println(file.getName());
                bpmnPanel.addBpmnInfo(file);
            }
        }

        bpmnPanel.sortParticipantHoldersByWidth();
        bpmnPanel.initEnvironmentWidth();
        bpmnPanel.confirmParticipantPositionAndSize();
        bpmnPanel.drawParticipant();
        bpmnPanel.drawMessageFlow();
        str = bpmnPanel.testXml();


        File fileExist = new File("src/main/resources/collaboration");
        // 文件夹不存在，则新建
        if (!fileExist.exists()) {
            fileExist.mkdirs();
        }
        TransformMultipartFile2File.fileWriterMethod("src/main/resources/collaboration/" + TransformMultipartFile2File.getCurDate() + ".bpmn", str);
        return str;
    }



    @GetMapping("/getAllCombineList")
    public List<List<Process>> getAllCombineList() throws IOException {
        String FILENAME_BASE =ResourceUtils.getURL("classpath:").getPath()+"src/main/resources/";
        List<Process> processList = new ArrayList<>();
        List<String> fileNames = getFileNamesFactory.getFileName();
        for (int i = 0; i < fileNames.size(); i++) {
            Process process = resolveProcessFactory.resolveProcess(FILENAME_BASE, fileNames.get(i));
            processList.add(process);
        }
        combineProcessFactory combineProcessFactory = new combineProcessFactory();
        combineProcessFactory.allCombineList(processList);
        System.out.println("最后结果所有res：" + combineProcessFactory.res);

        return combineProcessFactory.res;
    }



}
