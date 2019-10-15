package io.github.joaomlneto;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class CalculatorServer {
    private Server server;

    private void start() throws IOException {
        int port = 50051;
        server = ServerBuilder.forPort(port)
                .addService(new CalculatorServiceHandler())
                .build()
                .start();

        System.out.println("Server started, listening on port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.err.println("Shutting down gRPC server, since JVM is shutting down");
                CalculatorServer.this.stop();
                System.err.println("Server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main( String[] args ) throws IOException, InterruptedException {
        CalculatorServer server = new CalculatorServer();
        server.start();
        server.blockUntilShutdown();
    }
}
