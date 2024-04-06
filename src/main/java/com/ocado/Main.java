package com.ocado;

import com.ocado.basket.Basket;

public class Main {
    public static void main(String[] args) {
        String testConfig = "C:\\Users\\Konrad\\Desktop\\basket\\src\\main\\resources\\config.json";
        String testBasket1 = "C:\\Users\\Konrad\\Desktop\\basket\\src\\main\\resources\\basket-1.json";

        Basket basket = new Basket(testBasket1);
        System.out.println(basket);
    }
}