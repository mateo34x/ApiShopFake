package com.example.demo.SecurityID;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IDSe {

    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }
}
