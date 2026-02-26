package me.anisjamadar.onlinestore.repositories;

import me.anisjamadar.onlinestore.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

public interface OrderRepository extends JpaRepository<Order, Long> {
}