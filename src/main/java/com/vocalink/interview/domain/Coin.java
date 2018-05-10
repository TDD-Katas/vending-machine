package com.vocalink.interview.domain;

import lombok.Value;

@Value(staticConstructor="of")
public class Coin {
    int valueInPence;

    public String getId() {
        return valueInPence + "";
    }
}
