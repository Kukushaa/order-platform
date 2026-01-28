package com.kukusha.order_service.database.service;

import com.kukusha.order_service.database.model.Order;
import com.kukusha.order_service.database.repository.OrdersRepository;
import com.kukusha.order_service.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrdersService {
    private final OrdersRepository repository;

    public OrdersService(OrdersRepository repository) {
        this.repository = repository;
    }

    public Page<Order> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Order> findAllByEmail(String email, Pageable pageable) {
        return repository.findAllByEmail(email, pageable);
    }

    public Optional<Order> findById(Long id) {
        return repository.findById(id);
    }

    public Page<Order> findAllByEmailIsNot(String email, Pageable pageable) {
        return repository.findAllByEmailIsNot(email, pageable);
    }

    public void createNewOrder(OrderDTO dto, String username) {
        save(new Order(dto, username));
    }

    private void save(Order order) {
        repository.save(order);
    }
}
