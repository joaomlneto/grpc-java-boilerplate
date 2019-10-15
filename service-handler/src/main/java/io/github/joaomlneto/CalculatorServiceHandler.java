package io.github.joaomlneto;

import io.github.joaomlneto.CalculatorServiceGrpc;
import io.grpc.stub.StreamObserver;

import javax.naming.OperationNotSupportedException;

public class CalculatorServiceHandler extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void add(AddRequest request, StreamObserver<OperationResult> responseObserver) {
        int a = request.getA();
        int b = request.getB();
        int result = a + b;
        OperationResult response = OperationResult.newBuilder().setResult(result).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void execute(OperationRequest request, StreamObserver<OperationResult> responseObserver) {
        Operation op = request.getOp();
        int a = request.getA();
        int b = request.getB();
        switch (op) {
            case ADD: responseObserver.onNext(OperationResult.newBuilder().setResult(a + b).build()); break;
            case SUB: responseObserver.onNext(OperationResult.newBuilder().setResult(a - b).build()); break;
            case MUL: responseObserver.onNext(OperationResult.newBuilder().setResult(a * b).build()); break;
            case DIV: responseObserver.onNext(OperationResult.newBuilder().setResult(a / b).build()); break;
            case UNRECOGNIZED:
                responseObserver.onError(new OperationNotSupportedException("Invalid Operation: " + op));
                break;
        }
        responseObserver.onCompleted();
    }
}
