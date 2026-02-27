package me.anisjamadar.onlinestore.services;

import lombok.AllArgsConstructor;
import me.anisjamadar.onlinestore.dtos.orders.OrderDto;
import me.anisjamadar.onlinestore.exceptions.OrderNotFoundException;
import me.anisjamadar.onlinestore.mappers.OrderMapper;
import me.anisjamadar.onlinestore.repositories.OrderRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getAllOrders() {
        var user = authService.getCurrentUser();
        var orders = orderRepository.getOrdersByCustomer(user);
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    public OrderDto getOrder(Long orderId) {
        var order = orderRepository
                .getOrderWithItems(orderId)
                .orElseThrow(OrderNotFoundException::new);
        var user = authService.getCurrentUser();
        if (!order.getCustomer().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to access this order");
        }

        return orderMapper.toDto(order);
    }
}
