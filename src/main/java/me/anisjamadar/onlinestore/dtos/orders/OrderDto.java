package me.anisjamadar.onlinestore.dtos.orders;

import lombok.Data;
import me.anisjamadar.onlinestore.domain.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
public class OrderDto {
    private Long id;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItemDto> orderItems;
    private BigDecimal totalPrice;
}
