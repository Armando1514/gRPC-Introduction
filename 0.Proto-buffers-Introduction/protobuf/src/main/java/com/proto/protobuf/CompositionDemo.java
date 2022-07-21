package com.proto.protobuf;

import com.proto.models.Address;
import com.proto.models.Car;
import com.proto.models.Person;

public class CompositionDemo {
    public static void main(String[] args) {
        Address address = Address.newBuilder()
                .setPostbox(123)
                .setStreet("main one")
                .setCity("Lugano")
                .build();

        Car car1 = Car.newBuilder()
                .setMake("Honda")
                .setModel("AY32")
                .setYear(2022)
                .build();
        Car car2 = Car.newBuilder()
                .setMake("Ferrari")
                .setModel("GT")
                .setYear(2020)
                .build();

        Person armando = Person.newBuilder()
                .setName("Armando")
                .setAge(24)
                .setAddress(address)
                .addCar(car1)
                .addCar(car2)
                .build();

        System.out.println(armando);
    }
}
