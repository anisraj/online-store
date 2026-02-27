package me.anisjamadar.onlinestore.services;

import lombok.AllArgsConstructor;
import me.anisjamadar.onlinestore.domain.Order;
import me.anisjamadar.onlinestore.dtos.cart.CheckoutRequest;
import me.anisjamadar.onlinestore.dtos.cart.CheckoutResponse;
import me.anisjamadar.onlinestore.exceptions.CartEmptyException;
import me.anisjamadar.onlinestore.exceptions.CartNotFoundException;
import me.anisjamadar.onlinestore.repositories.CartRepository;
import me.anisjamadar.onlinestore.repositories.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@AllArgsConstructor
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;

    public CheckoutResponse checkout(CheckoutRequest request) {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        if (cart.isEmpty()) {
            throw new CartEmptyException();
        }

        var order = Order.getOrderFromCart(cart, authService.getCurrentUser());

        orderRepository.save(order);
        cartService.clearCart(cart.getId());

        return new CheckoutResponse(order.getId());
    }
}
