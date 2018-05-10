package com.vocalink.interview.states;

import com.vocalink.interview.domain.Product;

import java.io.PrintStream;

public class PurchaseCompletedState implements State {
    private final Product product;
    private final int change;

    PurchaseCompletedState(Product product, int change) {
        this.product = product;
        this.change = change;
    }

    @Override
    public void render(PrintStream out) {
        out.println("Please collect your item: "+product.getName());
        out.println("Please pick up your change: "+change+" pence");
    }

    @Override
    public State handleCommand(String command) {
        return null;
    }
}
