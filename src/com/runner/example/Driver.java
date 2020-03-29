package com.runner.example;

public class Driver {
    public static void main(String[] args) {
        Runner runner = new Runner("FirstRunner", 50000, 5, 2000, 1000);

        runner.init();
    }
}