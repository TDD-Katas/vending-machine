package com.vocalink.interview.domain;

import lombok.Value;

@Value(staticConstructor="of")
public class Product {
    String name;
    int priceInPence;

    public String getId() {
        return priceInPence+"";
    }
}
