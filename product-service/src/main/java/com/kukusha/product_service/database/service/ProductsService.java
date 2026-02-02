package com.kukusha.product_service.database.service;

import com.kukusha.product_service.database.model.Product;
import com.kukusha.product_service.database.repository.ProductsRepository;
import com.kukusha.product_service.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductsService {
    private final ProductsRepository repository;

    public ProductsService(ProductsRepository repository) {
        this.repository = repository;
    }

    public Page<Product> findAllByUsername(String username, Pageable pageable) {
        return repository.findAllByUsername(username, pageable);
    }

    public Optional<Product> findById(Long id) {
        return repository.findById(id);
    }

    public Page<Product> findAllByUsernameIsNot(String email, Pageable pageable) {
        return repository.findAllByUsernameIsNot(email, pageable);
    }

    public void createNewProduct(ProductDTO dto, String username) {
        save(new Product(dto, username));
    }

    private void save(Product product) {
        repository.save(product);
    }
}
