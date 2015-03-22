package com.pairoo.backend.impl.http.payment;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponseToObjectTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpResponseToObjectTransformer.class);

    public Map<String, String> transform(final String responsePayload) {
        LOGGER.info("responsePayload = [{}]", responsePayload);
        Map<String, String> result = new HashMap<>();

        Scanner scanner = new Scanner(responsePayload);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] strings = line.split("=");
            String key = strings[0];
            String value = "";
            if (strings.length > 1) {
                value = strings[1];
            }
            result.put(key, value);
        }
        scanner.close();

        return result;
    }
}
