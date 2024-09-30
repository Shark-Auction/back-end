package com.exe.sharkauction.services.implement;

import com.exe.sharkauction.components.exceptions.AppException;
import com.exe.sharkauction.models.OrderEntity;
import com.exe.sharkauction.models.PaymentEntity;
import com.exe.sharkauction.models.UserEntity;
import com.exe.sharkauction.models.enums.PaymentStatus;
import com.exe.sharkauction.repositories.IOrderRepository;
import com.exe.sharkauction.repositories.IPaymentRepository;
import com.exe.sharkauction.repositories.IUserRepository;
import com.exe.sharkauction.requests.PaymentResponseRequest;
import com.exe.sharkauction.responses.PaymentResponse;
import com.exe.sharkauction.services.IPaymentService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {
    private final IPaymentRepository paymentRepository;
    private final IUserRepository userRepository;
    private final IOrderRepository orderRepository;


    private final String CLIENT_ID = "7d3784da-544f-466f-bec3-799ef1fd4c6a";
    private final String API_KEY = "f76ce004-2300-4657-9ca5-9ae629d5b233";
    private final String CHECK_SUM_KEY = "99a51f9b4ebe9b533ebaa675c924d543c137c48c2020406d583d4b575ef4b20f";
    private final String PARTNER_CODE = "Phuc0987";
    private String CANCEL_URL = "http://localhost:8080/api/v1/payment/success";
    private String RETURN_URL = "http://localhost:8080/api/v1/payment/cancel";

    @Override
    public PaymentResponse.PaymentData createPaymentLink(PaymentEntity paymentEntity) throws Exception {
        String url = "https://api-merchant.payos.vn/v2/payment-requests";
        RestTemplate restTemplate = new RestTemplate();
        //Create Payment Entity
        OrderEntity order = orderRepository.findById(paymentEntity.getOrderEntity().getId())
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "There are no order!"));

        UserEntity user = userRepository.findById(paymentEntity.getPaymentUser().getId())
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "There are no User!"));

        paymentEntity.setAmount((int)order.getPrice());
        paymentEntity.setDescription("O" + order.getId());
        paymentEntity.setOrderCode(Integer.parseInt(String.valueOf(new Date().getTime()).substring(String.valueOf(new Date().getTime()).length() - 6)));

        paymentEntity = paymentRepository.save(paymentEntity);

        //Call API
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", CLIENT_ID);
        headers.set("x-api-key", API_KEY);
        headers.set("x-partner-code", PARTNER_CODE);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode paymentRequest = mapper.createObjectNode();

        paymentRequest.put("orderCode", paymentEntity.getOrderCode());
        paymentRequest.put("amount", paymentEntity.getAmount());
        paymentRequest.put("description", paymentEntity.getDescription());
        paymentRequest.put("buyerName", paymentEntity.getPaymentUser().getFull_name());
        paymentRequest.put("cancelUrl", CANCEL_URL);
        paymentRequest.put("returnUrl", RETURN_URL);
        paymentRequest.put("signature", this.getSignature(paymentEntity));

        ArrayNode itemsArray = paymentRequest.putArray("items");
//        for (PaymentRequest.Item item : paymentEntity.getItems()) {
            ObjectNode itemNode = mapper.createObjectNode();
            itemNode.put("name", order.getProduct().getName());
            itemNode.put("quantity", 1);
            itemNode.put("price", order.getPrice());
            itemsArray.add(itemNode);
//        }

        HttpEntity<ObjectNode> requestEntity = new HttpEntity<>(paymentRequest, headers);
        PaymentResponse response = restTemplate.postForObject(url, requestEntity, PaymentResponse.class);

        if (response != null && "00".equals(response.getCode())) {
            // Save payment data to the database (optional)
            paymentEntity.setCheckoutUrl(response.getData().getCheckoutUrl());
            paymentEntity.setStatus(PaymentStatus.fromString(response.getData().getStatus()));
            paymentEntity.setPaymentID(response.getData().getPaymentLinkId());
            paymentRepository.save(paymentEntity);

            // Return the checkout URL
            return response.getData();
        } else {
            throw new Exception("Payment creation failed: " + (response != null ? response.getDesc() : "Unknown error"));
        }
    }

    @Override
    public PaymentEntity returnValueOfPayment(PaymentResponseRequest paymentResponseRequest) {
        List<PaymentEntity> payments =  paymentRepository.findByPaymentID(paymentResponseRequest.getPaymentLinkId());
        PaymentEntity payment = payments.get(0);
        payment.setStatus(PaymentStatus.fromString(paymentResponseRequest.getStatus()));

        return paymentRepository.save(payment);
    }

    private String getSignature(PaymentEntity paymentEntity) throws Exception {
        // Concatenate the necessary fields
        String data = "amount=" + paymentEntity.getAmount()
                + "&cancelUrl=" + CANCEL_URL
                + "&description=" + paymentEntity.getDescription()
                + "&orderCode=" + paymentEntity.getOrderCode()
                + "&returnUrl=" + RETURN_URL;

        // Generate HMAC SHA256 hash
        Mac sha256HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(CHECK_SUM_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256HMAC.init(secretKey);

        // Compute the hash and encode it to hex
        byte[] hashBytes = sha256HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // Convert the byte array into a hexadecimal string
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }

        // Return the hex-encoded hash (signature)
        return sb.toString();
    }


}
