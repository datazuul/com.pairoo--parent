package com.pairoo.backend.impl.http.payment;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for HttpResponseToObjectTransformer
 * @author ralf
 */
public class HttpResponseToObjectTransformerTest {
    
    /**
     * Test of transform method, of class HttpResponseToObjectTransformer.
     */
    @Test
    public void testKeyValue() {
        String responsePayload = "key=value";
        HttpResponseToObjectTransformer instance = new HttpResponseToObjectTransformer();
        
        Map<String, String> expResult = new HashMap<>();
        expResult.put("key", "value");
        
        Map<String, String> result = instance.transform(responsePayload);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testKeyWithoutValue() {
        String responsePayload = "key=";
        HttpResponseToObjectTransformer instance = new HttpResponseToObjectTransformer();
        
        Map<String, String> expResult = new HashMap<>();
        expResult.put("key", "");
        
        Map<String, String> result = instance.transform(responsePayload);
        assertEquals(expResult, result);
    }
    
}
