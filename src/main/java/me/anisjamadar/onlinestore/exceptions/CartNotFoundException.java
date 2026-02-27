package me.anisjamadar.onlinestore.exceptions;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException() {
        super("Cart not found");
    }
}
