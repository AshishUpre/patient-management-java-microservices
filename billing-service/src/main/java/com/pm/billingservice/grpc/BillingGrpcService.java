package com.pm.billingservice.grpc;

import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    @Override
    public void createBillingAccount(billing.BillingRequest billingRequest,
                                     StreamObserver<BillingResponse> responseObserver) {

        log.info("createBillingRequest received: {}", billingRequest);

        // buz logik

        BillingResponse billingResponse = BillingResponse.newBuilder()
                .setAccountId("123")
                .setStatus("OK")
                .build();

        // used to send response back to the client
        responseObserver.onNext(billingResponse);
        // saying response is completed and we can end the cycle in this response
        responseObserver.onCompleted();
        // ------- the reason 2 lines required is that we can send many responses to the client (unlike rest)
        // ------- so can also be used for real time analytics / chat apps
    }
}
