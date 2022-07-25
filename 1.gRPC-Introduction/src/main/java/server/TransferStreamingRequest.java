package server;

import com.proto.models.Account;
import com.proto.models.TransferRequest;
import com.proto.models.TransferResponse;
import com.proto.models.TransferStatus;
import io.grpc.stub.StreamObserver;

public class TransferStreamingRequest implements StreamObserver<TransferRequest> {

    private StreamObserver<TransferResponse> transferResponseStreamObserver;

    public TransferStreamingRequest(StreamObserver<TransferResponse> transferResponseStreamObserver) {
        this.transferResponseStreamObserver = transferResponseStreamObserver;
    }

    @Override
    public void onNext(TransferRequest transferRequest) {
        int fromAccount = transferRequest.getFromAccount();
        int toAccount = transferRequest.getToAccount();
        int amount = transferRequest.getAmount();
        int balance = AccountDatabase.getBalance(fromAccount);
        TransferStatus status = TransferStatus.FAILED;

        if(balance >= amount && fromAccount != toAccount){
            AccountDatabase.deductBalance(fromAccount, amount);
            AccountDatabase.addBalance(toAccount, amount);
            status = TransferStatus.SUCCESS;
        }
        Account fromAccountInfo =
                Account.newBuilder()
                        .setAccountNumber(fromAccount)
                        .setAmount(AccountDatabase.getBalance(fromAccount))
                        .build();

        Account toAccountInfo =
                Account.newBuilder()
                        .setAccountNumber(toAccount)
                        .setAmount(AccountDatabase.getBalance(fromAccount))
                        .build();

           TransferResponse transferResponse = TransferResponse
                .newBuilder()
                .setStatus(status)
                .addAccounts(
                        fromAccountInfo
                )
                .addAccounts(
                        toAccountInfo
                )
                .build();

        this.transferResponseStreamObserver.onNext(transferResponse);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        this.transferResponseStreamObserver.onCompleted();
    }
}
