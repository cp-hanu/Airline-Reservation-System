package com.edu.hanu.service;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GeneratePNRService {
    public static final char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    public  String generatePnr() {
        Random random = new Random();
        return NanoIdUtils.randomNanoId(random, alphabet, 6).toUpperCase();
    }
}
