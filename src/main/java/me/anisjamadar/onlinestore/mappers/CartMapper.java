package me.anisjamadar.onlinestore.mappers;

import me.anisjamadar.onlinestore.domain.Cart;
import me.anisjamadar.onlinestore.domain.CartItem;
import me.anisjamadar.onlinestore.dtos.cart.CartDto;
import me.anisjamadar.onlinestore.dtos.cart.CartItemDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "items", source = "cartItems")
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
