package com.luv2code.spring_boot_library.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ExtractJWT {

    // creating a static function that is going to be used in multiple places throughout our application
    // here below by adding the String extraction parameter, we are making it more dynamic instead of simply passing the static sub means subject which is having value of username.
    // so here in extraction we can pass any payload information and search it. so extraction again is going to be what we're passing in that we want to search for. so now we're grabbing our userEmail to make all of our endpoints dynamic.
    public static String payloadJWTExtraction(String token, String extraction) {

        // here we're removing all the information at the beginning ie we're removing the Bearer from the token, so token would just be equal to our token
        token.replace("Bearer ", "");

        // we're doing this bcos the JWT token having header, payload and signature is split by period (ie .(dot symbol))
        // so chunks[0] index = is having header; chunks[1] index = is having the payload information and this chunks will be having Base64 encoded payload information
        String[] chunks = token.split("\\.");

        // below code will be going to Decoder the JWT into the information that we can understand
        Base64.Decoder decoder = Base64.getUrlDecoder();

        // decoding the chunks[1] we get the payload information about the JWT that we're passing through
        String payload = new String(decoder.decode(chunks[1]));

        // so now we have the String which is the payload, and now our String of array of entries ie list of multiple payload entries is going to separate each of these elements by commas.
        String[] entries = payload.split(",");

        Map<String, String> map = new HashMap<>();

        for (String entry : entries) {
            String[] keyValue = entry.split(":");

            // here \"sub\" means the sub is enclosed with inverted comma "sub" . Also sub is having the username information like "sub" : "testuser1@email.com"
            // so here instead of passing \"sub\" directly which is static approach, we'll be passing the sub in the BookController dynamically
            if (keyValue[0].equals(extraction)) {

                int remove = 1;

                if (keyValue[1].endsWith("}")) {
                    remove = 2;
                }

                // below code is for the value ie the username in the payload entries
                keyValue[1] = keyValue[1].substring(0, keyValue[1].length() - remove);
                keyValue[1] = keyValue[1].substring(1);

                map.put(keyValue[0], keyValue[1]);
            }
        }

        // here the key is the "sub" and value is the username
        if (map.containsKey(extraction)) {
            return map.get(extraction);
        }

        return null;

    }

}

/* JWT Extraction using Jackson: another approach to extract the username from the JWT token payload portion.

    public static String payloadJWTExtraction(String token) {
        token.replace("Bearer", "");
        String[] chunks = token.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));

        ObjectMapper mapper = new ObjectMapper();
        JsonNode parent = null;
        try {
            parent = mapper.readTree(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String content = parent.path("sub").asText();
        return content;
    }

 */


