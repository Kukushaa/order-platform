package com.kukusha.order_service.database.repository;

import com.kukusha.order_service.database.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUsernameIsNot(String email, Pageable pageable);
    Page<Order> findAllByUsername(String username, Pageable pageable);
}
