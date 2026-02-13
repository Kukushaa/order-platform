package com.kukusha.product_service.kafka;

import com.kukusha.kafka_messages_sender.model.ProductsBuyData;
import com.kukusha.product_service.database.model.Product;
import com.kukusha.product_service.database.service.ProductsService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductsKafkaMessagesReceiver {
    private final ProductsService productsService;

    public ProductsKafkaMessagesReceiver(ProductsService productsService) {
        this.productsService = productsService;
    }

    @KafkaListener(topics = "product.buy", groupId = "products-service")
    public void listen(ProductsBuyData data) {
        Optional<Product> byId = productsService.findById(data.getProductId());

        if (byId.isPresent()) {
            Product product = byId.get();
            product.setAmount(product.getAmount() - 1);
            productsService.save(product);
        }
    }
}
