package com.vocalink.interview.states;

import com.vocalink.interview.domain.Product;

import java.io.PrintStream;

public class PurchaseRefundedState implements State {
    private final Product product;
    private final int refundValue;

    PurchaseRefundedState(Product product, int refundValue) {
        this.product = product;
        this.refundValue = refundValue;
    }

    @Override
    public void render(PrintStream out) {
        out.println("Purchase canceled: "+product.getName());
        out.println("Please pick up your refund: "+refundValue+" pence");
    }

    @Override
    public State handleCommand(String command) {
        return null;
    }
}
