package com.grpc.user.service;

import com.grpc.user.repository.UserRepository;
import com.grpcspringboot.common.Genre;
import com.grpcspringboot.user.UserGenreUpdateRequest;
import com.grpcspringboot.user.UserResponse;
import com.grpcspringboot.user.UserSearchRequest;
import com.grpcspringboot.user.UserServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@GrpcService
public class UserService extends  UserServiceGrpc.UserServiceImplBase {


    @Autowired
    private UserRepository repository;

    @Override
    public void getUserGenre(UserSearchRequest request, StreamObserver<UserResponse> responseObserver) {

        final UserResponse.Builder builder = UserResponse.newBuilder();
        this.repository.findById(request.getLoginId())
                .ifPresent(user -> {
                    builder.
                            setName(user.getName())
                            .setLoginId(user.getLogin())
                            .setGenre(Genre.valueOf(
                                    user.getGenre().toUpperCase()));
                });

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void updateUserGenre(UserGenreUpdateRequest request, StreamObserver<UserResponse> responseObserver) {
        final UserResponse.Builder builder = UserResponse.newBuilder();

        this.repository.findById(request.getLoginId())
                .ifPresent(user -> {
                    user.setGenre(request.getGenre().toString());
                    builder.
                            setName(user.getName())
                            .setLoginId(user.getLogin())
                            .setGenre(Genre.valueOf(
                                    user.getGenre().toUpperCase()));
                });
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
