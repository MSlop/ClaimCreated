package com.example.ClaimCreated;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

@Component
public class Worker {
    private static Logger log = LoggerFactory.getLogger(Worker.class);

    @JobWorker(type = "set_handler")
    public void Sethandler(JobClient jobClient, final ActivatedJob job ){

       // Set output email handler
        String handlerEmail = "mslop2017@gmail.com";
        // Send new variable, for example numberClaim
        jobClient
                .newCompleteCommand(job.getKey())
                .variables(Collections.singletonMap("handlerEmail", handlerEmail ))
                .send().join();

    }


    private static void logJob(final ActivatedJob job, Object parameterValue) {
        log.info(
                "complete job\n>>> [type: {}, key: {}, element: {}, workflow instance: {}]\n{deadline; {}]\n[headers: {}]\n[variable parameter: {}\n[variables: {}]",
                job.getType(),
                job.getKey(),
                job.getElementId(),
                job.getProcessInstanceKey(),
                Instant.ofEpochMilli(job.getDeadline()),
                job.getCustomHeaders(),
                parameterValue,
                job.getVariables());
    }

}
