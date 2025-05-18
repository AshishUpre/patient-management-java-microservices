package com.pm.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceGrpcClient {
    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);
    // provides blocking (synchronous) calls to grpc server.
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    public BillingServiceGrpcClient(
            // adding env variables and injecting them
            @Value("${billing-service.host:billing-service}") String serverAddress,
            @Value ("${billing-service.port:9001}") int serverPort
    ) {
        log.info("Connecting to billingService grpc server on {} : {}", serverAddress, serverPort);

        // creates a channel that is already managed, useplaintext disables encryption --> good for local dev / test
        // this channel will be used to create blocking stub
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
                .usePlaintext()
                .build();

        blockingStub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public BillingResponse createBillingAccount(String patientId, String name, String email) {
        BillingRequest billingRequest = BillingRequest.newBuilder()
                .setPatientId(patientId)
                .setName(name)
                .setEmail(email)
                .build();
        BillingResponse billingResponse = blockingStub.createBillingAccount(billingRequest);
        log.info("received response from billing-service via grpc : {}", billingResponse);
        return billingResponse;
    }
}
