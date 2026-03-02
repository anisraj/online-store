package me.anisjamadar.onlinestore.controllers;

import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.anisjamadar.onlinestore.dtos.cart.CheckoutRequest;
import me.anisjamadar.onlinestore.dtos.cart.CheckoutResponse;
import me.anisjamadar.onlinestore.exceptions.CartEmptyException;
import me.anisjamadar.onlinestore.exceptions.CartNotFoundException;
import me.anisjamadar.onlinestore.exceptions.PaymentException;
import me.anisjamadar.onlinestore.services.CheckoutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<CheckoutResponse> checkout(
        @Valid @RequestBody CheckoutRequest request
    ) {
        var response =  checkoutService.checkout(request);
        return ResponseEntity.ok(response);
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
