package com.example.ClaimCreated;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableZeebeClient
public class SendEmail {

    @Autowired
    private JavaMailSender javaMailSender;

    public static void main(String[] args) {
        SpringApplication.run(SendEmail.class, args);
    }

    @ZeebeWorker(type = "send_notification_handler")
    public void SendEmailHandler(final JobClient client, final ActivatedJob job) {

        Map<String, Object> variablesAsMap = job.getVariablesAsMap();

        String receiver = variablesAsMap.get("handlerEmail").toString();

        //String policyId = variablesAsMap.get("policyId").toString();
        String claimId = variablesAsMap.get("claimId").toString();

        String sender = "mslop2017@gmail.com";
        //String receiver = "mslop2017@gmail.com";
        String subject = "New Claim";
        String body = "You should create assigment for Claim = " + claimId;

        List<String> invalidEmailAddresses = new ArrayList();
        boolean invalidEmails = false;

        if(!ValidateEmail.isValidEmail(sender)){
            invalidEmailAddresses.add(sender);
            invalidEmails = true;
        }
        if(!ValidateEmail.isValidEmail(receiver)){
            invalidEmailAddresses.add(receiver);
            invalidEmails = true;
        }
        if(invalidEmails)
        {
            client.newThrowErrorCommand(job)
                    .errorCode("INVALID_EMAIL")
                    .send();
        }else {

            try {
                sendMail(sender, receiver, subject, body);
                String resultMessage = "Mail Sent Successfully to " + receiver;

                HashMap<String, Object> variables = new HashMap<>();
                variables.put("result", resultMessage);
                client.newCompleteCommand(job.getKey())
                        .variables(variables)
                        .send()
                        .exceptionally((throwable -> {
                            throw new RuntimeException("Could not complete job", throwable);
                        }));
            } catch (MessagingException e) {
                e.printStackTrace();
                client.newFailCommand(job.getKey());
            }
        }
    }

    @ZeebeWorker(type = "send_notification_manager")
    public void SendEmailManager(final JobClient client, final ActivatedJob job) {

        Map<String, Object> variablesAsMap = job.getVariablesAsMap();

        String receiver = variablesAsMap.get("managerEmail").toString();

        String claimId = variablesAsMap.get("claimId").toString();
        String handlerEmail = variablesAsMap.get("handlerEmail").toString();

        String sender = "mslop2017@gmail.com";
        //String receiver = "mslop2017@gmail.com";
        String subject = "New Claim";
        String body = "The task to create Assignment for Claim = " + claimId + " is assigned to the user = " + handlerEmail;

        List<String> invalidEmailAddresses = new ArrayList();
        boolean invalidEmails = false;

        if(!ValidateEmail.isValidEmail(sender)){
            invalidEmailAddresses.add(sender);
            invalidEmails = true;
        }
        if(!ValidateEmail.isValidEmail(receiver)){
            invalidEmailAddresses.add(receiver);
            invalidEmails = true;
        }
        if(invalidEmails)
        {
            client.newThrowErrorCommand(job)
                    .errorCode("INVALID_EMAIL")
                    .send();
        }else {

            try {
                sendMail(sender, receiver, subject, body);
                String resultMessage = "Mail Sent Successfully to " + receiver;

                HashMap<String, Object> variables = new HashMap<>();
                variables.put("result", resultMessage);
                client.newCompleteCommand(job.getKey())
                        .variables(variables)
                        .send()
                        .exceptionally((throwable -> {
                            throw new RuntimeException("Could not complete job", throwable);
                        }));
            } catch (MessagingException e) {
                e.printStackTrace();
                client.newFailCommand(job.getKey());
            }
        }
    }

    @ZeebeWorker(type = "send_result_to_manager")
    public void SendEmailResult(final JobClient client, final ActivatedJob job) {

        Map<String, Object> variablesAsMap = job.getVariablesAsMap();

        String receiver = variablesAsMap.get("managerEmail").toString();

        String claimId = variablesAsMap.get("claimId").toString();
        String assignmentId = variablesAsMap.get("assignmentId").toString();

        String sender = "mslop2017@gmail.com";
        //String receiver = "mslop2017@gmail.com";
        String subject = "New Claim";
        String body = " Claim = " + claimId + ", Assignment = " + assignmentId + " was created" ;

        List<String> invalidEmailAddresses = new ArrayList();
        boolean invalidEmails = false;

        if(!ValidateEmail.isValidEmail(sender)){
            invalidEmailAddresses.add(sender);
            invalidEmails = true;
        }
        if(!ValidateEmail.isValidEmail(receiver)){
            invalidEmailAddresses.add(receiver);
            invalidEmails = true;
        }
        if(invalidEmails)
        {
            client.newThrowErrorCommand(job)
                    .errorCode("INVALID_EMAIL")
                    .send();
        }else {

            try {
                sendMail(sender, receiver, subject, body);
                String resultMessage = "Mail Sent Successfully to " + receiver;

                HashMap<String, Object> variables = new HashMap<>();
                variables.put("result", resultMessage);
                client.newCompleteCommand(job.getKey())
                        .variables(variables)
                        .send()
                        .exceptionally((throwable -> {
                            throw new RuntimeException("Could not complete job", throwable);
                        }));
            } catch (MessagingException e) {
                e.printStackTrace();
                client.newFailCommand(job.getKey());
            }
        }
    }



    @ZeebeWorker(type = "send_overdue_notification")
    public void SendEmailManagerAboutOverdueTask(final JobClient client, final ActivatedJob job) {

        Map<String, Object> variablesAsMap = job.getVariablesAsMap();

        String receiver = variablesAsMap.get("managerEmail").toString();

        String claimId = variablesAsMap.get("claimId").toString();
        //String handlerEmail = variablesAsMap.get("handlerEmail").toString();

        String sender = "mslop2017@gmail.com";
        //String receiver = "mslop2017@gmail.com";
        String subject = "New Claim";
        String body = "The task with Claim = " + claimId + "was overdue, task assigned to you ";

        List<String> invalidEmailAddresses = new ArrayList();
        boolean invalidEmails = false;

        if(!ValidateEmail.isValidEmail(sender)){
            invalidEmailAddresses.add(sender);
            invalidEmails = true;
        }
        if(!ValidateEmail.isValidEmail(receiver)){
            invalidEmailAddresses.add(receiver);
            invalidEmails = true;
        }
        if(invalidEmails)
        {
            client.newThrowErrorCommand(job)
                    .errorCode("INVALID_EMAIL")
                    .send();
        }else {

            try {
                sendMail(sender, receiver, subject, body);
                String resultMessage = "Mail Sent Successfully to " + receiver;

                HashMap<String, Object> variables = new HashMap<>();
                variables.put("result", resultMessage);
                client.newCompleteCommand(job.getKey())
                        .variables(variables)
                        .send()
                        .exceptionally((throwable -> {
                            throw new RuntimeException("Could not complete job", throwable);
                        }));
            } catch (MessagingException e) {
                e.printStackTrace();
                client.newFailCommand(job.getKey());
            }
        }
    }



    private void sendMail(String sender, String receiver, String subject, String body) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(sender);
        helper.setTo(receiver);
        helper.setSubject(subject);
        helper.setText(body, true);

        javaMailSender.send(message);
    }

}
