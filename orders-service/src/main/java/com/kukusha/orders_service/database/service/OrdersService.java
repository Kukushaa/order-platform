package com.kukusha.orders_service.database.service;

import com.kukusha.orders_service.database.model.Order;
import com.kukusha.orders_service.database.repository.OrdersRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrdersService {
    private final OrdersRepository repository;

    public OrdersService(OrdersRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "order", key = "#id", unless = "#result == null || #result.isEmpty()")
    public Optional<Order> findById(Long id) {
        return repository.findById(id);
    }

    @CacheEvict(value = "order", key = "#order.id")
    public void save(Order order) {
        repository.save(order);
    }
}
