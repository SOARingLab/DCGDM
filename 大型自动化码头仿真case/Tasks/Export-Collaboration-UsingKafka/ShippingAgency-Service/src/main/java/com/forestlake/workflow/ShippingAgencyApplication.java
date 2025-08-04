package com.forestlake.workflow;

//import org.camunda.community.rest.EnableCamundaRestClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableCamundaRestClient
public class ShippingAgencyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShippingAgencyApplication.class, args);
    }

}
