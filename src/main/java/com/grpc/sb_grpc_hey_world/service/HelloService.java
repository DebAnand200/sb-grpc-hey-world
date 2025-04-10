package com.grpc.sb_grpc_hey_world.service;

import com.grpc.sb_grpc_hey_world.HelloServiceGrpc;
import com.grpc.sb_grpc_hey_world.HeyProtoGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class HelloService extends HelloServiceGrpc.HelloServiceImplBase {
    @Override
    public void sayHello(HeyProtoGrpc.HelloRequest request, StreamObserver<HeyProtoGrpc.HelloResponse> responseObserver) {
        String message = "Hello, " + request.getName() + "! ❤️";
        HeyProtoGrpc.HelloResponse response = HeyProtoGrpc.HelloResponse.newBuilder()
                .setMessage(message)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
