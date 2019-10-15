package io.github.joaomlneto;

import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

public class CalculatorServiceHandlerTest {
	private CalculatorServiceGrpc.CalculatorServiceBlockingStub blockingStub;

	@Rule
	public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

	@BeforeClass
	public void setupService() throws IOException {
		// Generate a unique in-process server name
		String serverName = InProcessServerBuilder.generateName();

		// Create a server, add a service, start, and register for automatic graceful shutdown
		grpcCleanup.register(InProcessServerBuilder.forName(serverName)
				.directExecutor()
				.addService(new CalculatorServiceHandler()).build().start());

		// Create a client channel and register for automatic graceful shutdown
		blockingStub = CalculatorServiceGrpc.newBlockingStub(
				grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build()));
		System.out.println("SETTIN UP SERVICE TO USE IN TEST");
	}

	@Test
	public void shouldAdd() {
		Assert.assertEquals(4, blockingStub.add(AddRequest.newBuilder().setA(2).setB(2).build()).getResult());
		Assert.assertEquals(-5, blockingStub.add(AddRequest.newBuilder().setA(7).setB(-12).build()).getResult());
	}

	@Test
	public void shouldExecuteAllOperations() throws IOException {
		Assert.assertEquals(5, blockingStub.execute(OperationRequest.newBuilder().setOp(Operation.ADD).setA(2).setB(3).build()).getResult());
		Assert.assertEquals(3, blockingStub.execute(OperationRequest.newBuilder().setOp(Operation.SUB).setA(10).setB(7).build()).getResult());
		Assert.assertEquals(35, blockingStub.execute(OperationRequest.newBuilder().setOp(Operation.MUL).setA(7).setB(5).build()).getResult());
		Assert.assertEquals(3, blockingStub.execute(OperationRequest.newBuilder().setOp(Operation.DIV).setA(15).setB(4).build()).getResult());
	}

}
