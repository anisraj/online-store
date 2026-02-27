package me.anisjamadar.onlinestore.controllers;

import lombok.AllArgsConstructor;
import me.anisjamadar.onlinestore.dtos.orders.OrderDto;
import me.anisjamadar.onlinestore.exceptions.OrderNotFoundException;
import me.anisjamadar.onlinestore.mappers.OrderMapper;
import me.anisjamadar.onlinestore.repositories.OrderRepository;
import me.anisjamadar.onlinestore.services.AuthService;
import me.anisjamadar.onlinestore.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public OrderDto getOrder(@PathVariable("orderId") Long orderId) {
        return orderService.getOrder(orderId);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Void> handleOrderNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            Map.of("error", "Access denied")
        );
    }
}
