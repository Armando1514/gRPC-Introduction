package com.grpc.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReccomendedMovie {

    private String title;
    private int year;
    private double rating;


}
