package me.anisjamadar.onlinestore.exceptions;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException() {
        super("Cart is empty");
    }
}
