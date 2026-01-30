package com.kukusha.procustservice.database.repository;

import com.kukusha.procustservice.database.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByUsernameIsNot(String email, Pageable pageable);
    Page<Product> findAllByUsername(String username, Pageable pageable);
}
