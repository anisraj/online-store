package me.anisjamadar.onlinestore.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.anisjamadar.onlinestore.domain.Order;
import me.anisjamadar.onlinestore.domain.OrderItem;
import me.anisjamadar.onlinestore.domain.OrderStatus;
import me.anisjamadar.onlinestore.dtos.cart.CheckoutRequest;
import me.anisjamadar.onlinestore.dtos.cart.CheckoutResponse;
import me.anisjamadar.onlinestore.repositories.CartRepository;
import me.anisjamadar.onlinestore.repositories.OrderRepository;
import me.anisjamadar.onlinestore.services.AuthService;
import me.anisjamadar.onlinestore.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<?> checkout(
        @Valid @RequestBody CheckoutRequest request
    ) {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Cart not found")
            );
        }

        if (cart.getCartItems().isEmpty()) {
            return ResponseEntity.badRequest().body(
                Map.of("error", "Cart is empty")
            );
        }

        var order = new Order();
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(authService.getCurrentUser());

        cart.getCartItems().forEach(item -> {
            var orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotalPrice(item.getTotalPrice());
            orderItem.setUnitPrice(item.getProduct().getPrice());
            order.getOrderItems().add(orderItem);
        });

        orderRepository.save(order);
        cartService.clearCart(cart.getId());

        return ResponseEntity.ok(new CheckoutResponse(order.getId()));
    }
}
