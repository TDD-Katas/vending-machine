package com.vocalink.interview.states;

import com.vocalink.interview.domain.Product;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

public class ProductListState implements State {

    private final List<Product> products;

    public ProductListState() {
        products = Arrays.asList(
                Product.of("CANDY", 10),
                Product.of("SNACK", 50),
                Product.of("NUTS", 75),
                Product.of("Coke", 150),
                Product.of("Bottle Water", 100)
        );
    }

    @Override
    public void render(PrintStream out) {
        out.println("Please select the item you wish to purchase:");
        for (Product product : products) {
            out.println(product.getName() + " (" + product.getId() + ")");
        }
    }

    @Override
    public State handleCommand(String command) {
        if (command.startsWith("keypad")) {
            String productId = command.substring("keypad".length()).trim();

            if (isValidProduct(productId)) {
                return new PurchaseInProgressState(getProductById(productId), 0);
            }
        }

        return this;
    }

    private boolean isValidProduct(String productId) {
        return products.stream()
                .anyMatch(item -> item.getId().equals(productId));
    }

    private Product getProductById(String productId) {
        return products.stream()
                .filter(item -> item.getId().equals(productId))
                .findAny().get();
    }
}
