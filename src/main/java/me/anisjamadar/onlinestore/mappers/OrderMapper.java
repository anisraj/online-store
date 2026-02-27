package me.anisjamadar.onlinestore.mappers;

import me.anisjamadar.onlinestore.domain.Order;
import me.anisjamadar.onlinestore.dtos.orders.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);
}
