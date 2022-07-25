package com.proto.protobuf;

import com.proto.models.BodyStyle;
import com.proto.models.Car;
import com.proto.models.Dealer;

public class MapDemo {

    public static void main(String [] args) {
        Car car1 = Car.newBuilder()
                .setMake("Honda")
                .setModel("AY32")
                .setYear(2022)
                .setBodyStyle(BodyStyle.COUPE)
                .build();
        Car car2 = Car.newBuilder()
                .setMake("Ferrari")
                .setModel("GT")
                .setYear(2020)
                .build();

        Dealer dealer = Dealer.newBuilder()
                .putModel(2022, car1)
                .putModel(2020, car2)
                .build();

        System.out.println(
                dealer.getModelOrThrow(2022)
        );
        System.out.println(
                dealer.getModelOrThrow(2022).getBodyStyle()
        );
    }
}
