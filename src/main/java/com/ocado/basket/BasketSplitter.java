package com.ocado.basket;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class BasketSplitter {

    Map<String, List<String>> deliveryProducts = new HashMap<>();

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
                System.out.println(key);
            }

        } catch (JSONException e) {
            throw new RuntimeException("File is not a valid JSON file", e);
        }
    }

    public Map<String, List<String>> split(List<String> items) {
        return Collections.emptyMap();
    }
}
