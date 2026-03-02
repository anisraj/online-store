package me.anisjamadar.onlinestore.services;

import me.anisjamadar.onlinestore.domain.Order;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
}
