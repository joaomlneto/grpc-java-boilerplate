package io.github.joaomlneto;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;

public class Client {

    private final ManagedChannel channel;
    private final CalculatorServiceGrpc.CalculatorServiceBlockingStub blockingStub;

    public Client(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext().build());
    }

    Client(ManagedChannel channel) {
        this.channel = channel;
        this.blockingStub = CalculatorServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public int add(int a, int b) throws StatusRuntimeException {
        AddRequest request = AddRequest.newBuilder().setA(a).setB(b).build();
        OperationResult response = blockingStub.add(request);
        return response.getResult();
    }

    public int execute(Operation op, int a, int b) throws StatusRuntimeException {
        OperationRequest request = OperationRequest.newBuilder().setOp(op).setA(a).setB(b).build();
        OperationResult response = blockingStub.execute(request);
        return response.getResult();
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client("localhost", 50051);
        try {
            System.out.println("2+2=" + client.add(2, 2));
            System.out.println("3*5=" + client.execute(Operation.MUL, 3, 5));
        } finally {
            client.shutdown();
        }
    }
}
