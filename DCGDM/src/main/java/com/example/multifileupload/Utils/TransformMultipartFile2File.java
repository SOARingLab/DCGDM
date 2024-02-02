package com.example.multifileupload.Utils;


import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransformMultipartFile2File {
    public static File transform(MultipartFile multfile) throws IOException {
        String fileName = multfile.getOriginalFilename();

        // 用uuid作为文件名，防止生成的临时文件重复
        final File file = File.createTempFile(fileName, "bpmn");
        // MultipartFile to File
        multfile.transferTo(file);
        return file;
    }

    public static void fileWriterMethod(String filepath, String content) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filepath)) {
            fileWriter.append(content);
        }
    }

    public static String getCurDate() {
        Date d = new Date();
        return dateToStr(d);
    }
    public static String dateToStr(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateNowStr = sdf.format(d);
        return dateNowStr;
    }


}
