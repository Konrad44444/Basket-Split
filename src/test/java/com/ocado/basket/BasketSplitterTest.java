package com.ocado.basket;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.ocado.basket.exceptions.IncorrectPathException;
import com.ocado.basket.exceptions.InvalidJsonFileException;

class BasketSplitterTest {

    static BasketSplitter basketSplitter;

    @BeforeAll
    public static void setup() {
        String configPath = "src\\test\\java\\com\\ocado\\basket\\resources\\config.json";
        basketSplitter = new BasketSplitter(configPath);
    }

    @Test
    void givenCorrectConfigFile_expectDataInHashMap() {
        assertFalse(basketSplitter.getProductsAndDeliveryTypes().isEmpty());
    }

    @Test
    void givenIncorrectPathToFile_expectIncorrectPathException() {
        String incorrectPath = "incorrectpath";

        assertThrows(IncorrectPathException.class, () -> new BasketSplitter(incorrectPath));

    }

    @Test
    void givenPathToIncorrectBasketFile_expectInvalidJsonFileException() {
        String path = "src\\test\\java\\com\\ocado\\basket\\resources\\invalidJson.json";

        assertThrows(InvalidJsonFileException.class, () -> new BasketSplitter(path));
    }

    @Test
    void givenEmptyList_expectEmptyMap() {
        assertEquals(Collections.emptyMap(), basketSplitter.split(Collections.emptyList()));
    }

    @Test
    void givenTooBigList_expectEmptyMap() {
        List<String> testBasket = new ArrayList<>();
        for (int i = 0; i <= 101; i++) {
            testBasket.add("test");
        }

        assertThrows(IllegalArgumentException.class, () -> basketSplitter.split(testBasket));
    }

    @Test
    void givenNullAsAnArgument_expectEmptyMap() {
        assertEquals(Collections.emptyMap(), basketSplitter.split(null));
    }

    @Test
    void givenBasket1_returnCorrectOutput() {
        Basket basket = new Basket("src\\test\\java\\com\\ocado\\basket\\resources\\basket-1.json");

        Map<String, List<String>> result = basketSplitter.split(basket.getItems());

        assertAll(
                () -> assertEquals(2, result.keySet().size()),
                () -> assertEquals(5, result.get("Courier").size()),
                () -> assertEquals(1, result.get("Pick-up point").size()));
    }

    @Test
    void givenBasket2_returnCorrectOutput() {
        Basket basket = new Basket("src\\test\\java\\com\\ocado\\basket\\resources\\basket-2.json");

        Map<String, List<String>> result = basketSplitter.split(basket.getItems());

        assertAll(
                () -> assertEquals(3, result.keySet().size()),
                () -> assertEquals(13, result.get("Express Collection").size()),
                () -> assertEquals(3, result.get("Same day delivery").size()),
                () -> assertEquals(1, result.get("Courier").size()));
    }

    @Test
    void givenBasket3_returnEmptyMap() {
        Basket basket = new Basket("src\\test\\java\\com\\ocado\\basket\\resources\\basket-3.json");

        Map<String, List<String>> result = basketSplitter.split(basket.getItems());

        assertEquals(Collections.emptyMap(), result);

    }

    @Test
    void givenBasket4_returnCoorrectOutput() {
        Basket basket = new Basket("src\\test\\java\\com\\ocado\\basket\\resources\\basket-4.json");

        Map<String, List<String>> result = basketSplitter.split(basket.getItems());

        assertEquals(3, result.keySet().size());
    }
}
