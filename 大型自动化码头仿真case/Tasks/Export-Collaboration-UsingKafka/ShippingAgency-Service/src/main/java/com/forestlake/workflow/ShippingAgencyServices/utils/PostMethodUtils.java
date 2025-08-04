package com.forestlake.workflow.ShippingAgencyServices.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.IOException;
import java.util.Base64;

@Slf4j
public class PostMethodUtils {

    /**
     * 发送HttpClient请求
     * @param message
     */


    public static void sendPost(String message, String businessKey, String topicName) throws IOException {




        String requestUrl = "http://localhost:8097/springKafkaSend";



        String params = "{ \"messageName\":\"" + message +  "\", " +
                " \"businessKey\":\"" + businessKey + "\", " +
                " \"topicName\":\"" + topicName + "\"}";

        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(requestUrl);


        // 设置请求头  Content-Type
        postMethod.setRequestHeader("Content-Type", "application/json");
        //Base64加密方式认证方式下的basic auth HAIN460000：用户名    topicis123：密码
        postMethod.setRequestHeader("Authorization", "Basic " + Base64.getUrlEncoder().encodeToString(("admin" + ":" + "123456").getBytes()));
        RequestEntity requestEntity = new StringRequestEntity(params, "application/json", "UTF-8");
        postMethod.setRequestEntity(requestEntity);
        httpClient.executeMethod(postMethod);// 执行请求

    }
}

