package com.ocado.basket;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BasketSplitter {

    Map<String, List<String>> deliveryTypesAndProducts = new HashMap<>();

    public BasketSplitter(String absolutePathToConfigFile) {
        String jsonTxt = "";

        // read the file
        try {

            InputStream is = new FileInputStream(absolutePathToConfigFile);
            jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException("Cannot find file - check path", e);
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
            throw new RuntimeException("File is not a valid JSON file", e);
        }
    }

    public Map<String, List<String>> split(List<String> items) {
        return Collections.emptyMap();
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
