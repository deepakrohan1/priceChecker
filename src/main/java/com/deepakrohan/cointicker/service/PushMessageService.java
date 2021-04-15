package com.deepakrohan.cointicker.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.CreateQueueResponse;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SqsException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PushMessageService {
    private SqsClient sqsClient;

    @Value("${queue-name}")
    private String queueName;

    public PushMessageService() {
        sqsClient = SqsClient.builder()
                .region(Region.US_EAST_2)
                .build();
    }

    public boolean sendMessageToQueue(String message) {
        try {

            GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder()
                    .queueName(queueName)
                    .build();

            String queueUrl = sqsClient.getQueueUrl(getQueueRequest).queueUrl();

            String uniqId = String.valueOf(UUID.randomUUID());

            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(message)
                    .messageGroupId(uniqId)
                    .messageDeduplicationId(uniqId)
                    .build();
            sqsClient.sendMessage(sendMsgRequest);

        } catch (SqsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            return false;
        }

        return true;
    }

    public SqsClient getSqsClient() {
        return sqsClient;
    }
}
