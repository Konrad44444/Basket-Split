package com.ocado.basket;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ocado.exceptions.IncorrectPathException;
import com.ocado.exceptions.InvalidJsonFileException;

public class BasketSplitter {

    Map<String, List<String>> deliveryTypesAndProducts = new HashMap<>();

    public BasketSplitter(String absolutePathToConfigFile) {
        String jsonTxt = "";

        // read the file
        try {

            InputStream is = new FileInputStream(absolutePathToConfigFile);
            jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new IncorrectPathException("Cannot find file - check path", e);
        }

        // create JSON object, iterate through it and add every product to each delivery
        // way
        try {

            JSONObject config = new JSONObject(jsonTxt);

            Set<String> keys = config.keySet();

            for (String key : keys) {
                // key is a prduct, which has one or many delivery types
                JSONArray deliveryTypes = config.getJSONArray(key);

                // for each delivery type add an product
                for (Object deliveryType : deliveryTypes) {

                    String dType = (String) deliveryType;

                    if (deliveryTypesAndProducts.containsKey(dType)) {
                        deliveryTypesAndProducts.get(dType).add(key);
                    } else {
                        deliveryTypesAndProducts.put(dType, new ArrayList<>(List.of(key)));
                    }
                }
            }

        } catch (JSONException e) {
            throw new InvalidJsonFileException("File is not a valid JSON file", e);
        }
    }

    public Map<String, List<String>> split(List<String> items) {
        Map<String, List<String>> result = new HashMap<>();

        if (items.size() > 100) {
            return result;
        }

        while (!items.isEmpty()) {

            // stage 0: if there is only one item in basket, return first delivery type
            // found
            if (items.size() == 1) {
                for (String deliveryType : deliveryTypesAndProducts.keySet()) {

                    if (deliveryTypesAndProducts.get(deliveryType).contains(items.get(0))) {
                        result.put(deliveryType, List.of(items.get(0)));
                        break;
                    }

                }
                break;
            }

            // stage 1: count how many products can every delivery type deliver
            Map<String, List<String>> deliveryCount = new HashMap<>();

            for (String deliveryType : deliveryTypesAndProducts.keySet()) {
                deliveryCount.put(deliveryType, new ArrayList<>());

                for (String item : items) {

                    // if this delivery type can deliver this item increase count
                    if (deliveryTypesAndProducts.get(deliveryType).contains(item)) {
                        List<String> i = deliveryCount.get(deliveryType);
                        i.add(item);
                        deliveryCount.replace(deliveryType, i);
                    }
                }
            }

            // stage 2: find delivery type with the highest amount of items
            int maxItems = 0;
            String maxDeliveryType = "";

            for (String deliveryType : deliveryCount.keySet()) {
                int size = deliveryCount.get(deliveryType).size();

                if (size > maxItems) {
                    maxItems = size;
                    maxDeliveryType = deliveryType;
                }
            }

            // stage 3: add delivery and items to result
            result.put(maxDeliveryType, deliveryCount.get(maxDeliveryType));

            // stage 4: remove items from list
            items.removeAll(deliveryCount.get(maxDeliveryType));

        }

        return result;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();

        for (String key : deliveryTypesAndProducts.keySet()) {
            result.append("Delivery type: " + key + "\n");

            for (String product : deliveryTypesAndProducts.get(key)) {
                result.append(product + ", ");
            }

            result.append("\n\n-----\n");
        }

        return result.toString();
    }
}
