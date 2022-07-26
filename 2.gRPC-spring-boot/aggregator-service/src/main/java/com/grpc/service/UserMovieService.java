package com.grpc.service;


import com.grpc.dto.ReccomendedMovie;
import com.grpc.dto.UserGenre;
import com.grpcspringboot.common.Genre;
import com.grpcspringboot.movie.MovieSearchRequest;
import com.grpcspringboot.movie.MovieSearchResponse;
import com.grpcspringboot.movie.MovieServiceGrpc;
import com.grpcspringboot.user.UserGenreUpdateRequest;
import com.grpcspringboot.user.UserResponse;
import com.grpcspringboot.user.UserSearchRequest;
import com.grpcspringboot.user.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMovieService {

    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userStub;

    @GrpcClient("movie-service")
    private MovieServiceGrpc.MovieServiceBlockingStub movieStub;

    public List<ReccomendedMovie> getUserMovieSuggestions(String loginId){

        UserSearchRequest userSearchRequest =
                UserSearchRequest.newBuilder().setLoginId(loginId).build();
        UserResponse userResponse =
                this.userStub.getUserGenre(userSearchRequest);
        MovieSearchRequest movieSearchRequest =
                MovieSearchRequest.newBuilder().setGenre(
                userResponse.getGenre()
        ).build();

        MovieSearchResponse movieSearchResponse =
                this.movieStub.getMovies(movieSearchRequest);

        return movieSearchResponse.getMovieList()
                .stream()
                .map(movieDto ->
                        new ReccomendedMovie(
                                movieDto.getTitle(),
                                movieDto.getYear(),
                                movieDto.getRating()))
                .collect(Collectors.toList());
    }


    public void setUserGenre(UserGenre userGenre){
        UserGenreUpdateRequest userGenreUpdateRequest = UserGenreUpdateRequest.newBuilder()
                .setLoginId(userGenre.getLoginId())
                .setGenre(Genre.valueOf(userGenre.getGenre().toUpperCase()))
                .build();
        this.userStub.updateUserGenre(userGenreUpdateRequest);
    }
}
