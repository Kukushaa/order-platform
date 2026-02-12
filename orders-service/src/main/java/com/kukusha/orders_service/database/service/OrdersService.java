package com.kukusha.orders_service.database.service;

import com.kukusha.orders_service.database.model.Order;
import com.kukusha.orders_service.database.repository.OrderHistoryRepository;
import com.kukusha.orders_service.database.repository.OrdersRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrdersService {
    private final OrdersRepository repository;
    private final OrderHistoryRepository historyRepository;

    public OrdersService(OrdersRepository repository, OrderHistoryRepository historyRepository) {
        this.repository = repository;
        this.historyRepository = historyRepository;
    }

    public Optional<Order> findById(Long id) {
        return repository.findById(id);
    }

    public void save(Order order) {
        repository.save(order);
    }
}
