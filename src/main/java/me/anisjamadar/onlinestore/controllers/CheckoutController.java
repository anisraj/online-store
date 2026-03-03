package me.anisjamadar.onlinestore.controllers;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.anisjamadar.onlinestore.domain.OrderStatus;
import me.anisjamadar.onlinestore.dtos.cart.CheckoutRequest;
import me.anisjamadar.onlinestore.dtos.cart.CheckoutResponse;
import me.anisjamadar.onlinestore.exceptions.CartEmptyException;
import me.anisjamadar.onlinestore.exceptions.CartNotFoundException;
import me.anisjamadar.onlinestore.exceptions.PaymentException;
import me.anisjamadar.onlinestore.repositories.OrderRepository;
import me.anisjamadar.onlinestore.services.CheckoutService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final OrderRepository orderRepository;
    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<CheckoutResponse> checkout(
        @Valid @RequestBody CheckoutRequest request
    ) {
        var response =  checkoutService.checkout(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(
        @RequestHeader("Stripe-signature") String signature,
        @RequestBody String payload
    ) {
        try {
            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);
            System.out.println(event.getType());

            switch (event.getType()) {
                case "payment_intent.succeeded" -> {
                    var orderId = extractOrderId(event);
                    var order = orderRepository.findById(orderId).orElseThrow();
                    order.setStatus(OrderStatus.PAID);
                    orderRepository.save(order);
                }
                case "payment_intent.failed" -> {
                    var orderId = extractOrderId(event);
                    var order = orderRepository.findById(orderId).orElseThrow();
                    order.setStatus(OrderStatus.FAILED);
                    orderRepository.save(order);
                }
            }
            return ResponseEntity.ok().build();
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private Long extractOrderId(Event event) {
        var stripeObject = event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new PaymentException("Could not deserialize stripe event, check the SDK compatibility."));
        var paymentIntent = (PaymentIntent) stripeObject;
        return Long.valueOf(paymentIntent.getMetadata().get("order_id"));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<Map<String, String>> handlePaymentException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error creating checkout"));
    }

    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<Map<String, Object>> handleException(Exception exception) {
        return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
    }
}
