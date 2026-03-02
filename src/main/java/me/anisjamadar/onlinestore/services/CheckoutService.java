package me.anisjamadar.onlinestore.services;

import lombok.RequiredArgsConstructor;
import me.anisjamadar.onlinestore.domain.Order;
import me.anisjamadar.onlinestore.dtos.cart.CheckoutRequest;
import me.anisjamadar.onlinestore.dtos.cart.CheckoutResponse;
import me.anisjamadar.onlinestore.exceptions.CartEmptyException;
import me.anisjamadar.onlinestore.exceptions.CartNotFoundException;
import me.anisjamadar.onlinestore.exceptions.PaymentException;
import me.anisjamadar.onlinestore.repositories.CartRepository;
import me.anisjamadar.onlinestore.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;

    @Transactional
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

        try {
            var session = paymentGateway.createCheckoutSession(order);
            cartService.clearCart(cart.getId());

            return new CheckoutResponse(order.getId(), session.getCheckoutUrl());
        } catch (PaymentException e) {
            orderRepository.delete(order);
            throw e;
        }
    }
}
