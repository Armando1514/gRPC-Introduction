package com.grpc.movie.service;


import com.grpc.movie.repository.MovieRepository;
import com.grpcspringboot.movie.MovieDto;
import com.grpcspringboot.movie.MovieSearchRequest;
import com.grpcspringboot.movie.MovieSearchResponse;
import com.grpcspringboot.movie.MovieServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class MovieService extends MovieServiceGrpc.MovieServiceImplBase{
    @Autowired
    private MovieRepository repository;

    @Override
    public void getMovies(MovieSearchRequest request, StreamObserver<MovieSearchResponse> responseObserver) {
        List<MovieDto> movieDtoList = this.repository
                .getMoviesByGenreOrderByYearDesc(
                        request.getGenre().toString()
                )
                .stream()
                .map(movie -> MovieDto.newBuilder()
                                .setTitle(movie.getTitle())
                                .setYear(movie.getYear())
                                .setRating(movie.getRating())
                                .build())
                .collect(Collectors.toList());
        responseObserver.onNext(MovieSearchResponse
                .newBuilder()
                .addAllMovie(movieDtoList)
                .build());
        responseObserver.onCompleted();
    }
}
