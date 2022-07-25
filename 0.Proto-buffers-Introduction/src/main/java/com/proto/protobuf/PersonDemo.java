package com.proto.protobuf;

import com.proto.models.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PersonDemo {

    public static void main(String[] args) throws IOException {
        Person armando = Person.newBuilder()
                .setName("Armando")
                .setAge(18)
                .build();

        Path path = Paths.get("armando.ser");

        // serialization (creating binary file armando.ser)
        Files.write(path, armando.toByteArray());        byte[] bytes = Files.readAllBytes((path));

        // deserialized from the binary file armando.ser
        Person armandoDeserialized = Person.parseFrom(bytes);
        System.out.println(armandoDeserialized.toString());
    }
}
