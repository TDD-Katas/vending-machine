package com.vocalink.interview.states;

import com.vocalink.interview.domain.Coin;
import com.vocalink.interview.domain.Product;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

public class PurchaseInProgressState implements State {
    private final Product product;
    private final int credit;
    private final List<Coin> coins;

    PurchaseInProgressState(Product product, int credit) {
        this.product = product;
        this.credit = credit;

        coins = Arrays.asList(
                Coin.of(1),
                Coin.of(5),
                Coin.of(20),
                Coin.of(50),
                Coin.of(100)
        );
    }

    @Override
    public void render(PrintStream out) {
        int restToPayInPence = product.getPriceInPence() - credit;
        out.println("Insert: " + restToPayInPence + " pence");
    }

    @Override
    public State handleCommand(String command) {
        if (command.startsWith("coin")) {
            String coinId = command.substring("coin".length()).trim();
            if (isValidCoin(coinId)) {
                return addCreditAndComputeNewState(coinId);
            }
        } else if (command.startsWith("refund")) {
            return new PurchaseRefundedState(product, credit);
        }

        return this;
    }

    private State addCreditAndComputeNewState(String coinId) {
        int coinValue = getCoinValueById(coinId);

        int totalInsertedValue = credit + coinValue;
        if (totalInsertedValue >= product.getPriceInPence()) {
            int change = totalInsertedValue - product.getPriceInPence();
            return new PurchaseCompletedState(product, change);
        } else {
            return new PurchaseInProgressState(product, totalInsertedValue);
        }
    }

    private boolean isValidCoin(String coinId) {
        return coins.stream()
                .anyMatch(coin -> coin.getId().equals(coinId));
    }

    private int getCoinValueById(String coinId) {
        return coins.stream()
                .filter(coin -> coin.getId().equals(coinId))
                .findFirst()
                .map(Coin::getValueInPence).get();
    }
}
