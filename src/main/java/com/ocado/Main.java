package com.ocado;

import com.ocado.basket.BasketSplitter;

public class Main {
    public static void main(String[] args) {
        String testConfig = "C:\\Users\\Konrad\\Desktop\\basket\\src\\main\\resources\\config.json";

        BasketSplitter b = new BasketSplitter(testConfig);
    }
}