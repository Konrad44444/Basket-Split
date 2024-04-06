package com.ocado;

import com.ocado.basket.Basket;
import com.ocado.basket.BasketSplitter;

public class Main {
    public static void main(String[] args) {
        String testConfig = "C:\\Users\\Konrad\\Desktop\\basket\\src\\main\\resources\\config.json";
        String testBasket1 = "C:\\Users\\Konrad\\Desktop\\basket\\src\\main\\resources\\basket-1.json";
        String testBasket2 = "C:\\Users\\Konrad\\Desktop\\basket\\src\\main\\resources\\basket-2.json";
        String testBasket3 = "C:\\Users\\Konrad\\Desktop\\basket\\src\\main\\resources\\basket-3.json";

        BasketSplitter basketSplitter = new BasketSplitter(testConfig);
        Basket basket = new Basket(testBasket2);

        System.out.println(basketSplitter.split(basket.getItems()));

    }
}