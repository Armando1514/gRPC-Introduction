package com.proto.protobuf;

import com.proto.models.Credentials;
import com.proto.models.EmailCredentials;
import com.proto.models.PhoneOTP;

public class OneOfDemo {
    public static void main(String[] args){

        EmailCredentials emailCredentials = EmailCredentials.newBuilder()
                .setEmail("lol@gmail.com")
                .setPassword("admin123")
                .build();

        PhoneOTP phoneOTP = PhoneOTP.newBuilder()
                .setNumber(2323232)
                .build();

        Credentials credentials = Credentials.newBuilder()
                .setEmailMode(emailCredentials)
                .build();

        login(credentials);
    }

    private static void login(Credentials credentials){

        switch(credentials.getModeCase()){
            case EMAILMODE:
                    System.out.println(credentials.getEmailMode());
            break;
            case PHONEMODE:
                System.out.println(credentials.getPhoneMode());
                break;
        }
    }
}
