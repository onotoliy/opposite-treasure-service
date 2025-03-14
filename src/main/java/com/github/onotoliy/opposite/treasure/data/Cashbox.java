package com.github.onotoliy.opposite.treasure.data;

import java.math.BigDecimal;
import java.time.Instant;

public record Cashbox(
        BigDecimal deposit,
        Instant lastUpdateDate
) {
    @Override
    public String toString() {
        return "{\"deposit\": \"" + deposit + "\", \"lastUpdateDate\": \"" + lastUpdateDate + "\"}";
    }
}
