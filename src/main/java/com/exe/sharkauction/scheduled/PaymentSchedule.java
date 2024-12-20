package com.exe.sharkauction.scheduled;

import com.exe.sharkauction.components.exceptions.AppException;
import com.exe.sharkauction.mappers.IOrderMapper;
import com.exe.sharkauction.models.*;
import com.exe.sharkauction.models.enums.OrderStatus;
import com.exe.sharkauction.models.enums.PaymentStatus;
import com.exe.sharkauction.models.enums.TransactionStatus;
import com.exe.sharkauction.models.enums.VoucherStatus;
import com.exe.sharkauction.repositories.*;
import com.exe.sharkauction.requests.OrderRequest;
import com.exe.sharkauction.requests.PaymentRequest;
import com.exe.sharkauction.responses.PaymentResponse;
import com.exe.sharkauction.services.IOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class PaymentSchedule {
    @Autowired
    private IPaymentRepository paymentRepository;

    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IVoucherRepository voucherRepository;
    @Autowired
    private IOrderService orderService;

    @Autowired
    private ISystemTransactionRepository transactionRepository;


    private final String CLIENT_ID = "7d3784da-544f-466f-bec3-799ef1fd4c6a";
    private final String API_KEY = "f76ce004-2300-4657-9ca5-9ae629d5b233";
    private final String CHECK_SUM_KEY = "99a51f9b4ebe9b533ebaa675c924d543c137c48c2020406d583d4b575ef4b20f";
    private final String PARTNER_CODE = "Phuc0987";

    @Scheduled(fixedRate = 60000)
    public void trackingPayment(){
        List<PaymentEntity> paymentEntities = paymentRepository.findAll();
        for(PaymentEntity paymentEntity : paymentEntities){
            if( paymentEntity.getStatus() != null &&
                paymentEntity.getStatus() != PaymentStatus.CANCELLED &&
                paymentEntity.getStatus() != PaymentStatus.PAID){

                PaymentStatus currentStatus = this.getPaymentStatus(paymentEntity.getPaymentID());
                paymentEntity.setStatus(currentStatus);
                paymentEntity = paymentRepository.save(paymentEntity);

                if(paymentEntity.getStatus() == PaymentStatus.PAID){


                    OrderRequest orderRequest = new OrderRequest();
                    orderRequest.setFullName(paymentEntity.getToFullName());
                    orderRequest.setNote(paymentEntity.getNote());
                    orderRequest.setType(paymentEntity.getType());
                    orderRequest.setPhoneNumber(paymentEntity.getToPhoneNumber());
                    orderRequest.setProduct_id(paymentEntity.getProduct().getId());
                    orderRequest.setToAddress(paymentEntity.getToAddress());
                    if(paymentEntity.getVoucherCode()!= null){
                        VoucherEntity voucher = voucherRepository.findByVoucherCode(paymentEntity.getVoucherCode());
                        if(voucher != null) {
                            orderRequest.setDiscount(voucher.getDiscount());
                            voucher.setStatus(VoucherStatus.Used);
                            voucherRepository.save(voucher);
                        }
                    }
                    OrderEntity order = IOrderMapper.INSTANCE.toModel(orderRequest);
                    order.setBuyer(paymentEntity.getPaymentUser());

                    orderService.createOrder(order);
                    SystemTransactionEntity systemTransaction = new SystemTransactionEntity();
                    systemTransaction.setMoney(paymentEntity.getAmount());
                    systemTransaction.setStatus(TransactionStatus.Payment);
                    systemTransaction.setUser(paymentEntity.getPaymentUser());
                    transactionRepository.save(systemTransaction);

                }

            }
        }
    }

    public PaymentStatus getPaymentStatus(String id) {
        String url = "https://api-merchant.payos.vn/v2/payment-requests/" + id;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", CLIENT_ID); // Replace with actual client ID
        headers.set("x-api-key", API_KEY); // Replace with actual API key

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // Parse the response JSON using ObjectNode
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode rootNode = (ObjectNode) mapper.readTree(response.getBody());

                // Check the code for success
                String code = rootNode.get("code").asText();
                if ("00".equals(code)) {
                    // Extract payment details from the response
                    ObjectNode dataNode = (ObjectNode) rootNode.get("data");

                    return PaymentStatus.fromString(dataNode.get("status").asText());
                } else {
                    // Handle error code
                    String errorDescription = rootNode.get("desc").asText();
                    System.err.println("Error fetching payment status: " + errorDescription);
                    return null;
                }
            } else {
                System.err.println("Error: Received status code " + response.getStatusCode());
                return null;
            }

        } catch (Exception ex) {
            System.err.println("Exception occurred while fetching payment status: " + ex.getMessage());
            return null;
        }
    }

}
