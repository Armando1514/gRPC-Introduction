package com.proto.protobuf;

import com.proto.models.Person;

public class DefaultValueDemo {
    public static void main(String[] args){
        Person person = Person.newBuilder().build();

        System.out.println(
                "City: " + person.getAddress().getCity());
    }
}
