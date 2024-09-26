package com.exe.sharkauction.services.implement;

import com.exe.sharkauction.models.DeliveryEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

public class GHNAPIService{
    private final String TOKEN = "43a8eec9-7b4c-11ef-b441-069be3e54cb9";
    private final String SHOP_ID = "5347335";

    public static void createShippingOrder(DeliveryEntity deliveryEntity) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create";

        // Prepare JSON payload
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode request = mapper.createObjectNode();
        request.put("payment_type_id", deliveryEntity.getPaymentTypeId());
        request.put("service_type_id", deliveryEntity.getServiceTypeId());
        request.put("from_name", deliveryEntity.getFromName());
        request.put("from_phone", deliveryEntity.getFromPhone());
        request.put("from_address", deliveryEntity.getFromAddress());
        request.put("from_ward_name", deliveryEntity.getFromWardName());
        request.put("from_district_name", deliveryEntity.getFromDistrictName());
        request.put("from_provice_name", deliveryEntity.getFromProvinceName());

        request.put("to_name", deliveryEntity.getToName());
        request.put("to_phone", deliveryEntity.getToPhone());
        request.put("to_address", deliveryEntity.getToAddress());
        request.put("to_ward_code", deliveryEntity.getToWardCode());
        request.put("to_district_id", deliveryEntity.getToDistrictId());

        request.put("weight", deliveryEntity.getWeight());
        request.put("length", deliveryEntity.getLength());
        request.put("width", deliveryEntity.getWidth());
        request.put("height", deliveryEntity.getHeight());

        request.put("required_note", "CHOXEMHANGKHONGTHU");
        // Add other fields as needed

        ArrayNode itemsArray = mapper.createArrayNode();
        ObjectNode item = mapper.createObjectNode();
        item.put("name", "Sample Item"); // Replace with actual item name or logic to get item details
        item.put("quantity", 1);
        item.put("weight", deliveryEntity.getWeight()); // Adjust as necessary
        itemsArray.add(item);

        // Add the items array to the request
        request.set("items", itemsArray);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Token", "43a8eec9-7b4c-11ef-b441-069be3e54cb9");
        headers.set("ShopId", "5347335");

        HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            // Handle the response as needed
            System.out.println("Response: " + response.getBody());
        } catch (HttpClientErrorException e) {
            System.err.println("HTTP Status Code: " + e.getStatusCode());
            System.err.println("Response Body: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
