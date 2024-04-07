package com.ocado.basket;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;

import com.ocado.basket.exceptions.IncorrectPathException;
import com.ocado.basket.exceptions.InvalidJsonFileException;

public class Basket {
    List<String> items = new ArrayList<>();

    public Basket(String absolutePathToBasketFile) {
        String jsonTxt = "";

        // read the file
        try {

            InputStream is = new FileInputStream(absolutePathToBasketFile);
            jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new IncorrectPathException("Cannot find file - check path", e);
        }

        try {

            if (jsonTxt.equals("")) {
                return;
            }

            // read data form file as an array
            JSONArray basketItems = new JSONArray(jsonTxt);

            // put the data to list
            for (Object basketItem : basketItems) {
                this.items.add((String) basketItem);
            }

        } catch (JSONException e) {
            throw new InvalidJsonFileException("File is not a valid JSON file", e);
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder();

        for (String item : items) {
            result.append(item + "\n");
        }

        return result.toString();
    }

    public List<String> getItems() {
        return this.items;
    }
}
