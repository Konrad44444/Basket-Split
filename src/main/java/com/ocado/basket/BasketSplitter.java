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

import com.ocado.basket.exceptions.IncorrectPathException;
import com.ocado.basket.exceptions.InvalidJsonFileException;

public class BasketSplitter {

    private Map<String, List<String>> deliveryTypesAndProducts = new HashMap<>();

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

            Set<String> products = config.keySet();

            for (String product : products) {
                // product has one or many delivery types
                JSONArray deliveryTypes = config.getJSONArray(product);

                // add product to map
                deliveryTypesAndProducts.put(product, new ArrayList<>());

                // for each product add a delivery type
                for (Object deliveryType : deliveryTypes) {

                    String dType = (String) deliveryType;
                    deliveryTypesAndProducts.get(product).add(dType);

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
            // available
            if (items.size() == 1) {

                String item = items.get(0);
                result.put(deliveryTypesAndProducts.get(item).get(0), List.of(item));

                break;
            }

            // stage 1: count how many products can every delivery type deliver by adding
            // them to the list
            Map<String, List<String>> deliveryCount = new HashMap<>();

            for (String item : items) {
                // for each item get its deliveries
                List<String> deliveryTypes = deliveryTypesAndProducts.get(item);

                // for each delivery in map add this item to list
                for (String deliveryType : deliveryTypes) {
                    deliveryCount.computeIfAbsent(deliveryType, k -> new ArrayList<>()).add(item);
                }
            }

            // stage 2: find delivery type with the highest amount of items
            int maxItems = 0;
            String maxDeliveryType = "";

            List<String> equalsAmounts = new ArrayList<>();

            for (String deliveryType : deliveryCount.keySet()) {
                int size = deliveryCount.get(deliveryType).size();

                if (size > maxItems) {
                    maxItems = size;
                    maxDeliveryType = deliveryType;

                    // change of the max amount of items - clear list
                    equalsAmounts.clear();
                } else if (size == maxItems) {
                    // if there are delivery types with equal amounts of items -> needs to be
                    // computed which gives the best solution
                    equalsAmounts.add(deliveryType);
                }
            }

            // stage 2.1: if there are more than one delivery type with the highes amount of
            // items compute the best solution
            if (!equalsAmounts.isEmpty()) {
                Map<String, Map<String, List<String>>> possibleResults = new HashMap<>();

                // for each delivery type compute the possible final result
                for (String deliveryType : equalsAmounts) {
                    List<String> copyItems = new ArrayList<>(items);
                    copyItems.removeAll(deliveryCount.get(deliveryType));

                    possibleResults.put(deliveryType, this.split(copyItems));
                }

                // for each possible result find the one with the smallest amount of delivery
                // groups
                int min = Integer.MAX_VALUE;
                String minDeliveryType = "";

                for (String deliveryType : possibleResults.keySet()) {
                    int deliveryGroups = possibleResults.get(deliveryType).keySet().size();

                    if (deliveryGroups < min) {
                        min = deliveryGroups;
                        minDeliveryType = deliveryType;
                    }
                }

                // return the best option found
                possibleResults.get(minDeliveryType).put(minDeliveryType, deliveryCount.get(minDeliveryType));
                return possibleResults.get(minDeliveryType);
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

        for (String product : deliveryTypesAndProducts.keySet()) {
            result.append("Product: " + product + "\n");

            for (String deliveryType : deliveryTypesAndProducts.get(product)) {
                result.append(deliveryType + ", ");
            }

            result.append("\n\n-----\n");
        }

        return result.toString();
    }

    public Map<String, List<String>> getDeliveryTypesAndProducts() {
        return this.deliveryTypesAndProducts;
    }
}
