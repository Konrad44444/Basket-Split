package com.ocado.basket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.ocado.exceptions.IncorrectPathException;
import com.ocado.exceptions.InvalidJsonFileException;

class BasketTest {

    @Test
    void givenIncorrectPathToFile_expectIncorrectPathException() {
        String incorrectPath = "bababa";

        assertThrows(IncorrectPathException.class, () -> new Basket(incorrectPath));

    }

    @Test
    void givenPathToIncorrectBasketFile_expectInvalidJsonFileException() {
        String path = "src\\test\\java\\com\\ocado\\basket\\resources\\invalidJson.json";

        assertThrows(InvalidJsonFileException.class, () -> new Basket(path));
    }

    @Test
    void givenCorrectFile_expectCorrectList() {
        String path = "src\\test\\java\\com\\ocado\\basket\\resources\\basketTest.json";

        Basket basket = new Basket(path);

        assertEquals(List.of("item1", "item2"), basket.getItems());
    }

    @Test
    void givenEmptyBasket_expectEmptyList() {
        String path = "src\\test\\java\\com\\ocado\\basket\\resources\\emptyBasket.json";

        Basket basket = new Basket(path);

        assertEquals(0, basket.getItems().size());
    }

    @Test
    void givenEmptyFile_expectEmptyList() {
        String path = "src\\test\\java\\com\\ocado\\basket\\resources\\emptyFile.json";

        Basket basket = new Basket(path);

        assertEquals(0, basket.getItems().size());
    }
}
