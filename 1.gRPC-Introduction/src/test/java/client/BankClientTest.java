package client;

import com.google.common.util.concurrent.Uninterruptibles;
import com.proto.models.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClientTest {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();
         this.blockingStub =
                BankServiceGrpc.newBlockingStub(channel);
         this.bankServiceStub = BankServiceGrpc.newStub(channel);
    }

    @Test
    public void balanceTest(){

        BalanceCheckRequest checkRequest = BalanceCheckRequest.newBuilder()
                .setAccountNumber(7)
                .build();

        Balance balance = this.blockingStub.getBalance(checkRequest);

        System.out.println("Received: " +
                balance.getAmount());
    }

    @Test
    public void withdrawTest(){
        WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder().setAccountNumber(7).setAmount(40).build();

        this.blockingStub.withdraw(withdrawRequest)
                .forEachRemaining(money ->
                        System.out.println("Received: " + money.getValue()));

    }

    @Test
    public void withdrawAsyncTest(){
        WithdrawRequest withdrawRequest = WithdrawRequest.newBuilder().setAccountNumber(10).setAmount(50).build();

        this.bankServiceStub.withdraw(withdrawRequest, new MoneyStreamingResponse());
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
    }

    @Test
    public void cashStreamingRequest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<DepositRequest> streamObserver =
                this.bankServiceStub.cashDeposit(new BalanaceStreamObserver(latch));
        for(int i = 0; i < 10; i++)
        {
            DepositRequest request =
                    DepositRequest.newBuilder()
                    .setAccountNumber(8)
                    .setAmount(10)
                    .build();
            streamObserver.onNext(request);
        }
        streamObserver.onCompleted();
        latch.await();
    }

}
