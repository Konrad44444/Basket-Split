package com.ocado.basket;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.ocado.exceptions.IncorrectPathException;
import com.ocado.exceptions.InvalidJsonFileException;

class BasketSplitterTest {

    static BasketSplitter basketSplitter;

    @BeforeAll
    public static void setup() {
        String configPath = "src\\test\\java\\com\\ocado\\basket\\resources\\config.json";
        basketSplitter = new BasketSplitter(configPath);
    }

    @Test
    void givenCorrectConfigFile_expectDataInHashMap() {
        assertFalse(basketSplitter.getDeliveryTypesAndProducts().isEmpty());
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

    // TODO: test empty list, test not empty list, test basket 1 and basket 2

}
