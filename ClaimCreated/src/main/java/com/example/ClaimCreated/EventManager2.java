package com.example.ClaimCreated;

import io.camunda.zeebe.client.ZeebeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class EventManager2 implements CommandLineRunner {

    public static void main(String[] args){
        SpringApplication.run(EventManager2.class, args);
    }

    @Autowired
    private ZeebeClient client;

    @Override
    public void run(String... args) throws Exception{

        final Map<String, Object> variables = new HashMap<String, Object>();

        String claimId = "0001";
        String assignmentId = "0002";

        // Fill data
        //variables.put("managerEmail","mslop2017@gmail.com");
        //variables.put("policyId","100");
        variables.put("assignmentId", assignmentId);
        //variables.put("NotificationType", 1);
        //variables.put("currentStatus", "PEN");

        // Send event messageName with variables = variables
        String messageKey = claimId;
        client.newPublishMessageCommand().messageName("assignment_created").correlationKey(messageKey).variables(variables)
                .send()
                .exceptionally( throwable -> { throw new RuntimeException( "Not message", throwable); } );
    }
}
