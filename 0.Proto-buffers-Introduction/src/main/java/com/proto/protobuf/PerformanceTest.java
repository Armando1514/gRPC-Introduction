package com.proto.protobuf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.proto.json.JPerson;
import com.proto.models.Person;

import java.io.IOException;

public class PerformanceTest {

    public static void main(String[] args) {
        // json
        JPerson person = new JPerson();
        person.setName("Armando");
        person.setAge(19);
        ObjectMapper mapper = new ObjectMapper();

        Runnable json = () -> {

            byte[] bytes = new byte[0];
            try {
                bytes = mapper.writeValueAsBytes(person);
                JPerson person1 = mapper.readValue(bytes, JPerson.class);

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        };

        // protobuf

        Person armando = Person.newBuilder()
                .setName("Armando")
                .setAge(19)
                .build();

        Runnable proto = () -> {
            byte[] bytes = armando.toByteArray();
            try {
                Person armando1 = Person.parseFrom(bytes);
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            }
        };

        for(int i = 0; i < 10; i++) {
            runPerformanceTest(json, "JSON exc: " + i);
            runPerformanceTest(proto, "PROTO exc: " + i);
        }
    }

    private static void runPerformanceTest(Runnable runnable, String method){
        long time1 = System.currentTimeMillis();
        for(int i = 0; i < 5_000_000; i++)
            runnable.run();

        long time2 = System.currentTimeMillis();

        System.out.println("Benchmark " + method + ": " + (time2 - time1) + " ms");
    }
}
